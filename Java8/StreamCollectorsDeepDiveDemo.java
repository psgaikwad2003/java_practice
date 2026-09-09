import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Advanced Deep Dive into Java Streams & Collectors API (Java 8 - Java 21).
 *
 * Concepts covered:
 * 1. Multi-level Grouping (groupingBy with nested downstream collectors).
 * 2. Downstream Aggregations: mapping, averagingDouble, summingDouble, counting.
 * 3. Partitioning: Boolean partitioning (partitioningBy) with downstream summary.
 * 4. Java 12+ Teeing Collector: Performing two concurrent downstream collections
 *    in a single pass and combining their outputs.
 * 5. Custom Collector from scratch: Building an Immutable Frequency Distribution
 *    using Collector.of(supplier, accumulator, combiner, finisher).
 */
public class StreamCollectorsDeepDiveDemo {

    public enum Department {
        ENGINEERING, MARKETING, SALES, FINANCE
    }

    public static class Employee {
        private final String name;
        private final Department department;
        private final String role;
        private final double salary;
        private final int yearsOfExperience;

        public Employee(String name, Department department, String role, double salary, int yearsOfExperience) {
            this.name = name;
            this.department = department;
            this.role = role;
            this.salary = salary;
            this.yearsOfExperience = yearsOfExperience;
        }

        public String getName() { return name; }
        public Department getDepartment() { return department; }
        public String getRole() { return role; }
        public double getSalary() { return salary; }
        public int getYearsOfExperience() { return yearsOfExperience; }

        @Override
        public String toString() {
            return String.format("%s (%s, $%.0f)", name, role, salary);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  STREAM COLLECTORS DEEP DIVE DEMO        ");
        System.out.println("==========================================");

        List<Employee> roster = List.of(
            new Employee("Alice", Department.ENGINEERING, "Staff Engineer", 145_000, 8),
            new Employee("Bob", Department.ENGINEERING, "Senior Engineer", 120_000, 5),
            new Employee("Charlie", Department.ENGINEERING, "Junior Engineer", 85_000, 2),
            new Employee("Diana", Department.SALES, "Account Executive", 95_000, 4),
            new Employee("Evan", Department.SALES, "Sales Lead", 130_000, 9),
            new Employee("Fiona", Department.MARKETING, "Growth Strategist", 90_000, 3),
            new Employee("George", Department.MARKETING, "Creative Director", 135_000, 10),
            new Employee("Hannah", Department.FINANCE, "Financial Analyst", 105_000, 6),
            new Employee("Ian", Department.FINANCE, "CFO", 210_000, 15)
        );

        // Demo 1: Multi-Level Grouping (Department -> List of Employee Names)
        System.out.println("\n[1] Department -> Employee Names (groupingBy + mapping):");
        Map<Department, List<String>> deptNames = roster.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.mapping(Employee::getName, Collectors.toList())
            ));

        deptNames.forEach((dept, names) ->
            System.out.println("    " + dept + ": " + names));

        // Demo 2: Downstream Aggregations: Average and Total Salary per Department
        System.out.println("\n[2] Salary Analytics per Department:");
        Map<Department, Double> avgSalaryPerDept = roster.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingDouble(Employee::getSalary)
            ));

        avgSalaryPerDept.forEach((dept, avg) ->
            System.out.printf("    %s -> Avg Salary: $%,.2f%n", dept, avg));

        // Demo 3: Highest Paid Employee per Department (collectingAndThen + maxBy)
        System.out.println("\n[3] Highest Paid Employee per Department:");
        Map<Department, Employee> highestEarnerPerDept = roster.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)),
                    Optional::get
                )
            ));

        highestEarnerPerDept.forEach((dept, emp) ->
            System.out.printf("    %s -> Top Earner: %s ($%,.0f)%n", dept, emp.getName(), emp.getSalary()));

        // Demo 4: Boolean Partitioning (Senior Staff >= 5 yrs vs Junior Staff < 5 yrs)
        System.out.println("\n[4] Partitioning by Experience (>= 5 Years):");
        Map<Boolean, List<Employee>> experiencePartition = roster.stream()
            .collect(Collectors.partitioningBy(e -> e.getYearsOfExperience() >= 5));

        System.out.println("    Senior Staff (>= 5 yrs): " + experiencePartition.get(true));
        System.out.println("    Junior Staff (< 5 yrs):  " + experiencePartition.get(false));

        // Demo 5: Java 12+ Teeing Collector (Single-pass Min and Max Salary disparity)
        System.out.println("\n[5] Single-Pass Min/Max Salary Spread using Collectors.teeing():");
        record SalarySpread(double min, double max, double spread) {}

        SalarySpread spread = roster.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparingDouble(Employee::getSalary)),
                Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)),
                (minOpt, maxOpt) -> {
                    double min = minOpt.map(Employee::getSalary).orElse(0.0);
                    double max = maxOpt.map(Employee::getSalary).orElse(0.0);
                    return new SalarySpread(min, max, max - min);
                }
            ));

        System.out.printf("    Min Salary: $%,.0f%n", spread.min());
        System.out.printf("    Max Salary: $%,.0f%n", spread.max());
        System.out.printf("    Disparity Spread: $%,.0f%n", spread.spread());

        // Demo 6: Custom Collector built from scratch using Collector.of()
        System.out.println("\n[6] Custom Collector: Word/Token Frequency Map:");
        List<String> skills = List.of("Java", "Spring", "Kafka", "Java", "Docker", "Java", "Kubernetes", "Kafka");

        Collector<String, Map<String, Integer>, Map<String, Integer>> frequencyCollector = Collector.of(
            HashMap::new,                                // Supplier
            (map, item) -> map.merge(item, 1, Integer::sum), // Accumulator
            (map1, map2) -> { map2.forEach((k, v) -> map1.merge(k, v, Integer::sum)); return map1; }, // Combiner
            Collections::unmodifiableMap,                // Finisher
            Collector.Characteristics.UNORDERED
        );

        Map<String, Integer> freq = skills.stream().collect(frequencyCollector);
        System.out.println("    Skills Frequency Map: " + freq);

        System.out.println("\nAll Stream Collectors Deep Dive demonstrations completed successfully.");
    }
}
