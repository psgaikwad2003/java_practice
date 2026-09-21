package DesignPatterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the Visitor Design Pattern (Behavioral Pattern).
 * 
 * Intent: Represent an operation to be performed on the elements of an object
 * structure. Visitor lets you define a new operation without changing the classes
 * of the elements on which it operates, achieving Double Dispatch in Java.
 * 
 * Key Participants:
 * 1. Visitor: Declares a Visit operation for each class of ConcreteElement in the structure.
 * 2. ConcreteVisitor: Implements each operation; accumulates state while traversing.
 * 3. Element: Defines an Accept operation taking a visitor as an argument.
 * 4. ConcreteElement: Implements Accept via double dispatch (e.g., visitor.visit(this)).
 * 5. ObjectStructure: Can enumerate its elements and allow visitors to traverse.
 * 
 * Included Examples:
 * 1. Document AST (Abstract Syntax Tree) Processing: Exports document nodes into
 *    Markdown, HTML5, and computes document metrics (word count, code lines, table cells).
 * 2. E-Commerce Order Fulfillment: Evaluates diversified product items (Physical, Digital,
 *    Perishable) across tax calculation, tariff, and shipping logistics visitors.
 */
public class VisitorPatternDemo {

    // =========================================================================
    // Example 1: Document AST Hierarchy & Multi-Format Exporters
    // =========================================================================

    // Forward declarations of concrete document elements for the Visitor interface
    public interface DocumentVisitor {
        void visit(HeadingNode heading);
        void visit(ParagraphNode paragraph);
        void visit(CodeSnippetNode codeSnippet);
        void visit(TableNode table);
    }

    /**
     * Element interface representing nodes in the document tree.
     */
    public interface DocumentNode {
        void accept(DocumentVisitor visitor);
    }

    /**
     * Concrete Element: Heading.
     */
    public static class HeadingNode implements DocumentNode {
        private final int level;
        private final String text;

        public HeadingNode(int level, String text) {
            this.level = level;
            this.text = text;
        }

        public int getLevel() { return level; }
        public String getText() { return text; }

        @Override
        public void accept(DocumentVisitor visitor) {
            visitor.visit(this); // Double dispatch
        }
    }

    /**
     * Concrete Element: Paragraph.
     */
    public static class ParagraphNode implements DocumentNode {
        private final String content;

        public ParagraphNode(String content) {
            this.content = content;
        }

        public String getContent() { return content; }

        @Override
        public void accept(DocumentVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Element: Code Snippet / Block.
     */
    public static class CodeSnippetNode implements DocumentNode {
        private final String language;
        private final List<String> codeLines;

        public CodeSnippetNode(String language, List<String> codeLines) {
            this.language = language;
            this.codeLines = codeLines;
        }

        public String getLanguage() { return language; }
        public List<String> getCodeLines() { return codeLines; }

        @Override
        public void accept(DocumentVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Element: Table.
     */
    public static class TableNode implements DocumentNode {
        private final List<String> headers;
        private final List<List<String>> rows;

        public TableNode(List<String> headers, List<List<String>> rows) {
            this.headers = headers;
            this.rows = rows;
        }

        public List<String> getHeaders() { return headers; }
        public List<List<String>> getRows() { return rows; }

        @Override
        public void accept(DocumentVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Visitor 1: Renders the Document to clean Markdown.
     */
    public static class MarkdownExportVisitor implements DocumentVisitor {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public void visit(HeadingNode heading) {
            sb.append("#".repeat(Math.max(1, heading.getLevel())))
              .append(" ")
              .append(heading.getText())
              .append("\n\n");
        }

        @Override
        public void visit(ParagraphNode paragraph) {
            sb.append(paragraph.getContent()).append("\n\n");
        }

        @Override
        public void visit(CodeSnippetNode codeSnippet) {
            sb.append("```").append(codeSnippet.getLanguage()).append("\n");
            for (String line : codeSnippet.getCodeLines()) {
                sb.append(line).append("\n");
            }
            sb.append("```\n\n");
        }

        @Override
        public void visit(TableNode table) {
            sb.append("| ").append(String.join(" | ", table.getHeaders())).append(" |\n");
            sb.append("| ").append("--- | ".repeat(table.getHeaders().size())).append("\n");
            for (List<String> row : table.getRows()) {
                sb.append("| ").append(String.join(" | ", row)).append(" |\n");
            }
            sb.append("\n");
        }

        public String getRenderedOutput() {
            return sb.toString();
        }
    }

    /**
     * Concrete Visitor 2: Renders the Document to semantic HTML5.
     */
    public static class HtmlExportVisitor implements DocumentVisitor {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public void visit(HeadingNode heading) {
            sb.append(String.format("<h%d>%s</h%d>%n", heading.getLevel(), heading.getText(), heading.getLevel()));
        }

        @Override
        public void visit(ParagraphNode paragraph) {
            sb.append(String.format("<p>%s</p>%n", paragraph.getContent()));
        }

        @Override
        public void visit(CodeSnippetNode codeSnippet) {
            sb.append(String.format("<pre><code class=\"language-%s\">%n", codeSnippet.getLanguage()));
            for (String line : codeSnippet.getCodeLines()) {
                sb.append("  ").append(line.replace("<", "&lt;").replace(">", "&gt;")).append("\n");
            }
            sb.append("</code></pre>\n");
        }

        @Override
        public void visit(TableNode table) {
            sb.append("<table>\n  <thead>\n    <tr>");
            for (String h : table.getHeaders()) {
                sb.append("<th>").append(h).append("</th>");
            }
            sb.append("</tr>\n  </thead>\n  <tbody>\n");
            for (List<String> row : table.getRows()) {
                sb.append("    <tr>");
                for (String cell : row) {
                    sb.append("<td>").append(cell).append("</td>");
                }
                sb.append("</tr>\n");
            }
            sb.append("  </tbody>\n</table>\n");
        }

        public String getRenderedOutput() {
            return sb.toString();
        }
    }

    /**
     * Concrete Visitor 3: Calculates analytical metrics across the document without modifying elements.
     */
    public static class DocumentMetricsVisitor implements DocumentVisitor {
        private int totalWords = 0;
        private int totalHeadings = 0;
        private int totalCodeLines = 0;
        private int totalTableCells = 0;

        @Override
        public void visit(HeadingNode heading) {
            totalHeadings++;
            totalWords += countWords(heading.getText());
        }

        @Override
        public void visit(ParagraphNode paragraph) {
            totalWords += countWords(paragraph.getContent());
        }

        @Override
        public void visit(CodeSnippetNode codeSnippet) {
            totalCodeLines += codeSnippet.getCodeLines().size();
        }

        @Override
        public void visit(TableNode table) {
            int cols = table.getHeaders().size();
            int rows = table.getRows().size();
            totalTableCells += (cols * (rows + 1));
        }

        private int countWords(String text) {
            if (text == null || text.isBlank()) return 0;
            return text.trim().split("\\s+").length;
        }

        public void printReport() {
            System.out.printf("  [Document Metrics Report]%n");
            System.out.printf("  - Headings Count   : %d%n", totalHeadings);
            System.out.printf("  - Prose Word Count : %d words%n", totalWords);
            System.out.printf("  - Code Snippet     : %d lines%n", totalCodeLines);
            System.out.printf("  - Table Total Cells: %d cells%n", totalTableCells);
        }
    }

    // =========================================================================
    // Example 2: E-Commerce Shopping Cart & Tax / Logistics Visitors
    // =========================================================================

    public interface OrderVisitor {
        void visit(PhysicalItem physicalItem);
        void visit(DigitalItem digitalItem);
        void visit(PerishableGroceryItem groceryItem);
    }

    public interface OrderItem {
        void accept(OrderVisitor visitor);
        String getName();
        double getPrice();
    }

    /**
     * Concrete Element: Standard Physical Merchandise.
     */
    public static class PhysicalItem implements OrderItem {
        private final String name;
        private final double price;
        private final double weightKg;

        public PhysicalItem(String name, double price, double weightKg) {
            this.name = name;
            this.price = price;
            this.weightKg = weightKg;
        }

        @Override public String getName() { return name; }
        @Override public double getPrice() { return price; }
        public double getWeightKg() { return weightKg; }

        @Override
        public void accept(OrderVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Element: Digital Download / Software License.
     */
    public static class DigitalItem implements OrderItem {
        private final String name;
        private final double price;
        private final double downloadSizeGb;

        public DigitalItem(String name, double price, double downloadSizeGb) {
            this.name = name;
            this.price = price;
            this.downloadSizeGb = downloadSizeGb;
        }

        @Override public String getName() { return name; }
        @Override public double getPrice() { return price; }
        public double getDownloadSizeGb() { return downloadSizeGb; }

        @Override
        public void accept(OrderVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Element: Perishable Grocery with cold storage requirements.
     */
    public static class PerishableGroceryItem implements OrderItem {
        private final String name;
        private final double price;
        private final double weightKg;
        private final boolean requiresRefrigeration;

        public PerishableGroceryItem(String name, double price, double weightKg, boolean requiresRefrigeration) {
            this.name = name;
            this.price = price;
            this.weightKg = weightKg;
            this.requiresRefrigeration = requiresRefrigeration;
        }

        @Override public String getName() { return name; }
        @Override public double getPrice() { return price; }
        public double getWeightKg() { return weightKg; }
        public boolean isRequiresRefrigeration() { return requiresRefrigeration; }

        @Override
        public void accept(OrderVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Concrete Visitor 1: Sales Tax and Tariff Calculation Visitor.
     */
    public static class TaxCalculationVisitor implements OrderVisitor {
        private double totalTax = 0.0;

        @Override
        public void visit(PhysicalItem physicalItem) {
            // Standard physical goods: 8.5% sales tax
            double tax = physicalItem.getPrice() * 0.085;
            totalTax += tax;
            System.out.printf("  [Tax] %s ($%.2f): Standard VAT 8.5%% = $%.2f%n",
                    physicalItem.getName(), physicalItem.getPrice(), tax);
        }

        @Override
        public void visit(DigitalItem digitalItem) {
            // Digital goods: 5.0% digital services tax
            double tax = digitalItem.getPrice() * 0.05;
            totalTax += tax;
            System.out.printf("  [Tax] %s ($%.2f): Digital Service Tax 5.0%% = $%.2f%n",
                    digitalItem.getName(), digitalItem.getPrice(), tax);
        }

        @Override
        public void visit(PerishableGroceryItem groceryItem) {
            // Essential grocery foods are tax exempt (0%)
            System.out.printf("  [Tax] %s ($%.2f): Essential Grocery = $0.00 (Exempt)%n",
                    groceryItem.getName(), groceryItem.getPrice());
        }

        public double getTotalTax() { return totalTax; }
    }

    /**
     * Concrete Visitor 2: Shipping & Logistics Freight Calculation.
     */
    public static class ShippingLogisticsVisitor implements OrderVisitor {
        private double totalShipping = 0.0;

        @Override
        public void visit(PhysicalItem physicalItem) {
            // $5.00 base + $2.50 per kg
            double cost = 5.00 + (physicalItem.getWeightKg() * 2.50);
            totalShipping += cost;
            System.out.printf("  [Shipping] %s (%.1f kg): Parcel Ground = $%.2f%n",
                    physicalItem.getName(), physicalItem.getWeightKg(), cost);
        }

        @Override
        public void visit(DigitalItem digitalItem) {
            // Instant digital transmission: $0.00 delivery fee
            System.out.printf("  [Shipping] %s (%.1f GB): Instant Digital Delivery = $0.00%n",
                    digitalItem.getName(), digitalItem.getDownloadSizeGb());
        }

        @Override
        public void visit(PerishableGroceryItem groceryItem) {
            // $4.00 base + $3.00/kg + cold container surcharge if refrigeration needed
            double cost = 4.00 + (groceryItem.getWeightKg() * 3.00);
            if (groceryItem.isRequiresRefrigeration()) {
                cost += 6.50; // Cold-chain insulation fee
            }
            totalShipping += cost;
            System.out.printf("  [Shipping] %s (%.1f kg, ColdChain: %s): Freight = $%.2f%n",
                    groceryItem.getName(), groceryItem.getWeightKg(), groceryItem.isRequiresRefrigeration(), cost);
        }

        public double getTotalShipping() { return totalShipping; }
    }

    // =========================================================================
    // Demonstration & Test Runner
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("        VISITOR PATTERN DEMO - DOUBLE DISPATCH ARCHITECTURE      ");
        System.out.println("=================================================================");

        // --- Demo 1: Document AST Structure & Multi-Visitor Processing ---
        System.out.println("\n--- SCENARIO 1: DOCUMENT AST MULTI-FORMAT EXPORTERS & METRICS ---");

        List<DocumentNode> documentNodes = List.of(
                new HeadingNode(1, "Microservice Architectural Blueprint"),
                new ParagraphNode("This comprehensive document details the event-driven communication protocols and service discovery configurations for production deployment."),
                new CodeSnippetNode("java", Arrays.asList(
                        "@RestController",
                        "@RequestMapping(\"/api/v1/orders\")",
                        "public class OrderController {",
                        "    @PostMapping",
                        "    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req) {",
                        "        return ResponseEntity.ok(orderService.placeOrder(req));",
                        "    }",
                        "}"
                )),
                new TableNode(
                        Arrays.asList("Component", "Technology", "Replicas"),
                        Arrays.asList(
                                Arrays.asList("API Gateway", "Spring Cloud", "3"),
                                Arrays.asList("Auth Service", "Keycloak / OAuth2", "2"),
                                Arrays.asList("Event Broker", "Apache Kafka", "5")
                        )
                )
        );

        // 1. Export to Markdown
        System.out.println("[Visitor 1: Markdown Export]");
        MarkdownExportVisitor markdownVisitor = new MarkdownExportVisitor();
        for (DocumentNode node : documentNodes) {
            node.accept(markdownVisitor);
        }
        System.out.print(markdownVisitor.getRenderedOutput());

        // 2. Export to HTML5
        System.out.println("[Visitor 2: HTML5 Export]");
        HtmlExportVisitor htmlVisitor = new HtmlExportVisitor();
        for (DocumentNode node : documentNodes) {
            node.accept(htmlVisitor);
        }
        System.out.print(htmlVisitor.getRenderedOutput());

        // 3. Compute Metrics
        System.out.println("[Visitor 3: Document Analytical Metrics]");
        DocumentMetricsVisitor metricsVisitor = new DocumentMetricsVisitor();
        for (DocumentNode node : documentNodes) {
            node.accept(metricsVisitor);
        }
        metricsVisitor.printReport();

        // --- Demo 2: E-Commerce Shopping Cart Multi-Domain Visitors ---
        System.out.println("\n--- SCENARIO 2: E-COMMERCE CART TAX & SHIPPING LOGISTICS ---");

        List<OrderItem> shoppingCart = List.of(
                new PhysicalItem("Mechanical Keyboard", 149.99, 1.2),
                new DigitalItem("IntelliJ IDEA Ultimate 1-Yr License", 169.00, 1.5),
                new PerishableGroceryItem("Organic Greek Yogurt Pack", 12.50, 0.8, true),
                new PerishableGroceryItem("Artisan Sourdough Loaf", 6.00, 0.5, false)
        );

        double subtotal = shoppingCart.stream().mapToDouble(OrderItem::getPrice).sum();
        System.out.printf("Cart Items: %d items | Subtotal: $%.2f%n%n", shoppingCart.size(), subtotal);

        // Calculate Taxes via TaxCalculationVisitor
        System.out.println("[Applying TaxCalculationVisitor]:");
        TaxCalculationVisitor taxVisitor = new TaxCalculationVisitor();
        for (OrderItem item : shoppingCart) {
            item.accept(taxVisitor);
        }
        System.out.printf("Total Calculated Tax: $%.2f%n%n", taxVisitor.getTotalTax());

        // Calculate Shipping via ShippingLogisticsVisitor
        System.out.println("[Applying ShippingLogisticsVisitor]:");
        ShippingLogisticsVisitor shippingVisitor = new ShippingLogisticsVisitor();
        for (OrderItem item : shoppingCart) {
            item.accept(shippingVisitor);
        }
        System.out.printf("Total Calculated Shipping: $%.2f%n%n", shippingVisitor.getTotalShipping());

        double grandTotal = subtotal + taxVisitor.getTotalTax() + shippingVisitor.getTotalShipping();
        System.out.printf("FINAL INVOICE: Subtotal ($%.2f) + Tax ($%.2f) + Shipping ($%.2f) = GRAND TOTAL: $%.2f%n",
                subtotal, taxVisitor.getTotalTax(), shippingVisitor.getTotalShipping(), grandTotal);

        System.out.println("\n=================================================================");
        System.out.println("                 VISITOR PATTERN COMPLETED                       ");
        System.out.println("=================================================================");
    }
}
