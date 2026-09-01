import java.util.*;
import java.util.stream.Collectors;


public class StreamsDemo {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(101, "Alice", "IT", 85000, 28, "Female"),
            new Employee(102, "Bob", "HR", 55000, 32, "Male"),
            new Employee(103, "Charlie", "IT", 95000, 35, "Male"),
            new Employee(104, "Diana", "Finance", 72000, 26, "Female"),
            new Employee(105, "Ethan", "IT", 60000, 24, "Male"),
            new Employee(106, "Fiona", "Finance", 88000, 31, "Female"),
            new Employee(107, "George", "HR", 52000, 29, "Male")
        );

        System.out.println("==================================================");
        System.out.println(" 1. Filter IT Employees earning > 70,000");
        System.out.println("==================================================");
        List<Employee> highEarnersIT = employees.stream()
            .filter(e -> e.getDepartment().equalsIgnoreCase("IT") && e.getSalary() > 70000)
            .collect(Collectors.toList());
        highEarnersIT.forEach(System.out::println);

        System.out.println("\n==================================================");
        System.out.println(" 2. Group Employees by Department");
        System.out.println("==================================================");
        Map<String, List<Employee>> employeesByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        employeesByDept.forEach((dept, empList) -> {
            System.out.println(dept + " (" + empList.size() + " employees):");
            empList.forEach(e -> System.out.println("   - " + e.getName() + " ($" + e.getSalary() + ")"));
        });

        System.out.println("\n==================================================");
        System.out.println(" 3. Find Employee with Highest Salary");
        System.out.println("==================================================");
        employees.stream()
            .max(Comparator.comparingDouble(Employee::getSalary))
            .ifPresent(highest -> System.out.println("Highest Paid: " + highest));

        System.out.println("\n==================================================");
        System.out.println(" 4. Average Salary per Department");
        System.out.println("==================================================");
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingDouble(Employee::getSalary)
            ));
        avgSalaryByDept.forEach((dept, avgSal) -> 
            System.out.printf("%-10s : $%.2f%n", dept, avgSal)
        );

        System.out.println("\n==================================================");
        System.out.println(" 5. Sort Employees by Salary Descending, then Name Ascending");
        System.out.println("==================================================");
        List<Employee> sortedEmployees = employees.stream()
            .sorted(Comparator.comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName))
            .collect(Collectors.toList());
        sortedEmployees.forEach(System.out::println);

        System.out.println("\n==================================================");
        System.out.println(" 6. Partition Employees by Age (> 30)");
        System.out.println("==================================================");
        Map<Boolean, List<Employee>> partitionedByAge = employees.stream()
            .collect(Collectors.partitioningBy(e -> e.getAge() > 30));
        System.out.println("Senior (> 30): " + partitionedByAge.get(true).size() + " employees");
        System.out.println("Junior (<= 30): " + partitionedByAge.get(false).size() + " employees");

        System.out.println("\n==================================================");
        System.out.println(" 7. Salary Summary Statistics");
        System.out.println("==================================================");
        DoubleSummaryStatistics stats = employees.stream()
            .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println("Total Count: " + stats.getCount());
        System.out.printf("Min Salary : $%.2f%n", stats.getMin());
        System.out.printf("Max Salary : $%.2f%n", stats.getMax());
        System.out.printf("Avg Salary : $%.2f%n", stats.getAverage());
        System.out.printf("Total Sum  : $%.2f%n", stats.getSum());

        System.out.println("\n==================================================");
        System.out.println(" 8. Joined Names per Department");
        System.out.println("==================================================");
        Map<String, String> deptNamesJoined = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.mapping(Employee::getName, Collectors.joining(", "))
            ));
        deptNamesJoined.forEach((dept, names) -> System.out.println(dept + ": " + names));
    }
}


class Employee {
    private final int id;
    private final String name;
    private final String department;
    private final double salary;
    private final int age;
    private final String gender;

    public Employee(int id, String name, String department, double salary, int age, String gender) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.age = age;
        this.gender = gender;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public int getAge() { return age; }
    public String getGender() { return gender; }

    @Override
    public String toString() {
        return String.format("Employee[ID=%d, Name='%s', Dept='%s', Salary=%.2f, Age=%d]",
            id, name, department, salary, age);
    }
}
