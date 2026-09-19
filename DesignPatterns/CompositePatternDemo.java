package DesignPatterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates the Composite Design Pattern (Structural Pattern).
 * 
 * Intent: Compose objects into tree structures to represent part-whole hierarchies.
 * Composite lets clients treat individual objects (leaves) and compositions
 * of objects (composites) uniformly.
 * 
 * Included Examples:
 * 1. File System Hierarchy: Files (Leaf) and Directories (Composite) with recursive
 *    size calculations, formatting tree prints, and keyword searches.
 * 2. Enterprise Organization Tree: Individual Contributors (Leaf) and Managers/Departments
 *    (Composite) with aggregate salary rollups and headcount calculations.
 */
public class CompositePatternDemo {

    // =========================================================================
    // Example 1: File System Tree Structure (Leaves & Composite Nodes)
    // =========================================================================

    /**
     * Component Interface for File System entries.
     */
    public interface FileSystemNode {
        String getName();
        long getSizeInBytes();
        void display(String indent);
        List<FileSystemNode> search(String query);
    }

    /**
     * Leaf: Represents an individual file with no children.
     */
    public static class FileLeaf implements FileSystemNode {
        private final String name;
        private final long sizeInBytes;

        public FileLeaf(String name, long sizeInBytes) {
            this.name = name;
            this.sizeInBytes = sizeInBytes;
        }

        @Override
        public String getName() { return name; }

        @Override
        public long getSizeInBytes() { return sizeInBytes; }

        @Override
        public void display(String indent) {
            System.out.printf("%s|- [File] %-20s (%d bytes)%n", indent, name, sizeInBytes);
        }

        @Override
        public List<FileSystemNode> search(String query) {
            if (name.toLowerCase().contains(query.toLowerCase())) {
                return Collections.singletonList(this);
            }
            return Collections.emptyList();
        }
    }

    /**
     * Composite: Represents a directory containing other directories and files.
     */
    public static class DirectoryComposite implements FileSystemNode {
        private final String name;
        private final List<FileSystemNode> children = new ArrayList<>();

        public DirectoryComposite(String name) {
            this.name = name;
        }

        public DirectoryComposite add(FileSystemNode node) {
            children.add(node);
            return this;
        }

        public boolean remove(FileSystemNode node) {
            return children.remove(node);
        }

        public List<FileSystemNode> getChildren() {
            return Collections.unmodifiableList(children);
        }

        @Override
        public String getName() { return name; }

        @Override
        public long getSizeInBytes() {
            // Recursive calculation over all child components
            return children.stream().mapToLong(FileSystemNode::getSizeInBytes).sum();
        }

        @Override
        public void display(String indent) {
            System.out.printf("%s+ [Dir] %s/ (Total: %d bytes, Items: %d)%n",
                    indent, name, getSizeInBytes(), children.size());
            for (FileSystemNode child : children) {
                child.display(indent + "   ");
            }
        }

        @Override
        public List<FileSystemNode> search(String query) {
            List<FileSystemNode> matches = new ArrayList<>();
            if (name.toLowerCase().contains(query.toLowerCase())) {
                matches.add(this);
            }
            for (FileSystemNode child : children) {
                matches.addAll(child.search(query));
            }
            return matches;
        }
    }

    // =========================================================================
    // Example 2: Enterprise Organization and Budget Hierarchy
    // =========================================================================

    /**
     * Component Interface for corporate entities.
     */
    public interface CorporateMember {
        String getName();
        String getTitle();
        double getMonthlyCost();
        int getHeadcount();
        void printOrgChart(String indent);
    }

    /**
     * Leaf: Individual contributor (Engineer, Designer, Analyst).
     */
    public static class IndividualContributor implements CorporateMember {
        private final String name;
        private final String title;
        private final double salary;

        public IndividualContributor(String name, String title, double salary) {
            this.name = name;
            this.title = title;
            this.salary = salary;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getTitle() { return title; }

        @Override
        public double getMonthlyCost() { return salary; }

        @Override
        public int getHeadcount() { return 1; }

        @Override
        public void printOrgChart(String indent) {
            System.out.printf("%s* %s (%s) - Monthly: $%,.2f%n", indent, name, title, salary);
        }
    }

    /**
     * Composite: Manager or Department leading direct reports or sub-teams.
     */
    public static class TeamLeadComposite implements CorporateMember {
        private final String name;
        private final String title;
        private final double baseSalary;
        private final List<CorporateMember> directReports = new ArrayList<>();

        public TeamLeadComposite(String name, String title, double baseSalary) {
            this.name = name;
            this.title = title;
            this.baseSalary = baseSalary;
        }

        public TeamLeadComposite addMember(CorporateMember member) {
            directReports.add(member);
            return this;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getTitle() { return title; }

        @Override
        public double getMonthlyCost() {
            double total = baseSalary;
            for (CorporateMember report : directReports) {
                total += report.getMonthlyCost();
            }
            return total;
        }

        @Override
        public int getHeadcount() {
            int count = 1; // self
            for (CorporateMember report : directReports) {
                count += report.getHeadcount();
            }
            return count;
        }

        @Override
        public void printOrgChart(String indent) {
            System.out.printf("%s# [LEAD] %s (%s) - Team Budget: $%,.2f, Total Staff: %d%n",
                    indent, name, title, getMonthlyCost(), getHeadcount());
            for (CorporateMember report : directReports) {
                report.printOrgChart(indent + "    ");
            }
        }
    }

    // =========================================================================
    // Demonstration and Verification
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("          COMPOSITE DESIGN PATTERN DEMONSTRATION         ");
        System.out.println("=========================================================");

        // --- Demo 1: File System Tree ---
        System.out.println("\n[Demo 1] Constructing and Traversing Nested File System:");
        DirectoryComposite rootDir = new DirectoryComposite("Workspace");
        DirectoryComposite srcDir = new DirectoryComposite("src");
        DirectoryComposite testDir = new DirectoryComposite("test");
        DirectoryComposite docsDir = new DirectoryComposite("docs");

        srcDir.add(new FileLeaf("Main.java", 4200))
              .add(new FileLeaf("Config.json", 1850))
              .add(new FileLeaf("DatabaseClient.java", 8900));

        testDir.add(new FileLeaf("MainTest.java", 3100))
               .add(new FileLeaf("MockDataset.csv", 14500));

        docsDir.add(new FileLeaf("README.md", 2500))
               .add(new FileLeaf("Architecture.png", 245000));

        rootDir.add(srcDir).add(testDir).add(docsDir).add(new FileLeaf(".gitignore", 120));

        // Uniform tree display
        rootDir.display("");

        // Recursive search across arbitrary depth
        System.out.println("\n--- Search Results for query 'java': ---");
        List<FileSystemNode> searchHits = rootDir.search("java");
        for (FileSystemNode hit : searchHits) {
            System.out.printf("  Found: %-22s (Size: %d bytes)%n", hit.getName(), hit.getSizeInBytes());
        }

        // --- Demo 2: Corporate Org Hierarchy ---
        System.out.println("\n[Demo 2] Enterprise Corporate Hierarchy & Rollup Costs:");
        TeamLeadComposite engineeringVP = new TeamLeadComposite("Sophia Chen", "VP of Engineering", 22000.0);
        TeamLeadComposite backendLead = new TeamLeadComposite("Marcus Vance", "Backend Tech Lead", 14500.0);
        TeamLeadComposite frontendLead = new TeamLeadComposite("Elena Rostova", "Frontend Tech Lead", 13800.0);

        backendLead.addMember(new IndividualContributor("Alice Smith", "Sr. Distributed Systems Eng", 11200.0))
                   .addMember(new IndividualContributor("Bob Jones", "Database Reliability Eng", 10500.0));

        frontendLead.addMember(new IndividualContributor("Chloe Diaz", "Sr. UI/UX Engineer", 9800.0))
                    .addMember(new IndividualContributor("David Miller", "Mobile App Specialist", 9200.0));

        engineeringVP.addMember(backendLead).addMember(frontendLead);

        // Print entire organization hierarchy uniformly
        engineeringVP.printOrgChart("");

        System.out.printf("%nSummary for %s's Division:%n", engineeringVP.getName());
        System.out.printf("  Total Headcount : %d people%n", engineeringVP.getHeadcount());
        System.out.printf("  Monthly Payroll : $%,.2f%n", engineeringVP.getMonthlyCost());

        System.out.println("\n[CompositePatternDemo] Execution finished successfully!");
    }
}
