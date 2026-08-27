import java.util.*;
import java.util.stream.*;

public class OptionalAndStreamsAdvancedDemo {

    record Product(String name, String category, double price, int stock) {}

    // Custom exception for domain logic
    static class ProductNotFoundException extends RuntimeException {
        ProductNotFoundException(String msg) { super(msg); }
    }

    // Simulate a product repository
    static class ProductRepository {
        private final List<Product> products = new ArrayList<>(List.of(
            new Product("Laptop",        "Electronics", 999.99,  15),
            new Product("Mouse",         "Electronics", 29.99,  150),
            new Product("Keyboard",      "Electronics", 79.99,   80),
            new Product("Headphones",    "Electronics", 149.99,  40),
            new Product("Desk Chair",    "Furniture",   349.99,  12),
            new Product("Standing Desk", "Furniture",   799.99,   5),
            new Product("Notebook",      "Stationery",   4.99,  500),
            new Product("Pen Set",       "Stationery",   9.99,  300),
            new Product("Monitor",       "Electronics", 399.99,  20),
            new Product("Webcam",        "Electronics",  89.99,  60)
        ));

        Optional<Product> findByName(String name) {
            return products.stream()
                    .filter(p -> p.name().equalsIgnoreCase(name))
                    .findFirst();
        }

        Optional<Product> findCheapestInCategory(String category) {
            return products.stream()
                    .filter(p -> p.category().equalsIgnoreCase(category))
                    .min(Comparator.comparingDouble(Product::price));
        }

        List<Product> getAll() { return products; }
    }

    public static void main(String[] args) {
        System.out.println("=== Optional & Advanced Streams Demo ===\n");

        ProductRepository repo = new ProductRepository();

        // ── Section 1: Optional basics ─────────────────────────────────────
        System.out.println("--- Optional Basics ---");

        Optional<Product> found = repo.findByName("Laptop");
        found.ifPresent(p -> System.out.println("Found: " + p.name() + " @ $" + p.price()));

        Optional<Product> notFound = repo.findByName("Toaster");
        System.out.println("Toaster present: " + notFound.isPresent());

        // orElse / orElseGet / orElseThrow
        Product product = notFound.orElse(new Product("Unknown", "N/A", 0.0, 0));
        System.out.println("orElse result: " + product.name());

        Product computed = notFound.orElseGet(() -> new Product("Default", "N/A", 1.0, 0));
        System.out.println("orElseGet result: " + computed.name());

        try {
            repo.findByName("Blender").orElseThrow(() -> new ProductNotFoundException("Blender not found"));
        } catch (ProductNotFoundException e) {
            System.out.println("orElseThrow caught: " + e.getMessage());
        }

        // map and flatMap on Optional
        String productName = repo.findByName("Monitor")
                .map(p -> p.name() + " (stock: " + p.stock() + ")")
                .orElse("not available");
        System.out.println("Optional.map: " + productName);

        // filter on Optional
        Optional<Product> expensiveLaptop = repo.findByName("Laptop")
                .filter(p -> p.price() > 500);
        System.out.println("Laptop > $500: " + expensiveLaptop.isPresent());

        Optional<Product> cheapLaptop = repo.findByName("Laptop")
                .filter(p -> p.price() < 100);
        System.out.println("Laptop < $100: " + cheapLaptop.isPresent());

        // ── Section 2: Advanced Stream operations ──────────────────────────
        System.out.println("\n--- Advanced Stream Operations ---");

        List<Product> all = repo.getAll();

        // flatMap (simulate nested data)
        List<List<String>> tagGroups = List.of(
            List.of("tech", "premium"),
            List.of("budget", "sale"),
            List.of("office", "tech")
        );
        List<String> allTags = tagGroups.stream()
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("FlatMap (all tags): " + allTags);

        // Collectors.partitioningBy
        Map<Boolean, List<Product>> partitioned = all.stream()
                .collect(Collectors.partitioningBy(p -> p.price() >= 100));
        System.out.println("Products >= $100 : " + partitioned.get(true).stream().map(Product::name).collect(Collectors.joining(", ")));
        System.out.println("Products < $100  : " + partitioned.get(false).stream().map(Product::name).collect(Collectors.joining(", ")));

        // Collectors.groupingBy with downstream collector
        Map<String, Double> avgPriceByCategory = all.stream()
                .collect(Collectors.groupingBy(Product::category,
                         Collectors.averagingDouble(Product::price)));
        System.out.println("\nAvg price by category:");
        avgPriceByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.printf("  %-15s $%.2f%n", e.getKey(), e.getValue()));

        // Collectors.counting
        Map<String, Long> countByCategory = all.stream()
                .collect(Collectors.groupingBy(Product::category, Collectors.counting()));
        System.out.println("Count by category: " + countByCategory);

        // Collectors.joining
        String electronicsNames = all.stream()
                .filter(p -> p.category().equals("Electronics"))
                .map(Product::name)
                .sorted()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Electronics: " + electronicsNames);

        // reduce
        double totalInventoryValue = all.stream()
                .reduce(0.0, (acc, p) -> acc + p.price() * p.stock(), Double::sum);
        System.out.printf("Total inventory value: $%.2f%n", totalInventoryValue);

        // Statistics
        DoubleSummaryStatistics stats = all.stream()
                .mapToDouble(Product::price)
                .summaryStatistics();
        System.out.printf("Price stats -> min: $%.2f, max: $%.2f, avg: $%.2f%n",
                stats.getMin(), stats.getMax(), stats.getAverage());

        // takeWhile / dropWhile (Java 9+)
        System.out.println("\n--- takeWhile / dropWhile ---");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> taken = numbers.stream().takeWhile(n -> n < 6).collect(Collectors.toList());
        List<Integer> dropped = numbers.stream().dropWhile(n -> n < 6).collect(Collectors.toList());
        System.out.println("takeWhile (< 6): " + taken);
        System.out.println("dropWhile (< 6): " + dropped);

        // Stream.iterate and Stream.generate
        System.out.println("\n--- Stream.iterate & generate ---");
        List<Integer> powers = Stream.iterate(1, n -> n * 2)
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("Powers of 2: " + powers);

        List<Double> randoms = Stream.generate(Math::random)
                .limit(5)
                .map(d -> Math.round(d * 100.0) / 100.0)
                .collect(Collectors.toList());
        System.out.println("Random values: " + randoms);

        // Cheapest in each category via Optional
        System.out.println("\n--- Cheapest Product Per Category ---");
        all.stream()
           .map(Product::category)
           .distinct()
           .sorted()
           .forEach(cat -> repo.findCheapestInCategory(cat)
               .ifPresent(p -> System.out.printf("  %-15s -> %s ($%.2f)%n", cat, p.name(), p.price())));

        System.out.println("\n=== Demo Complete ===");
    }
}
