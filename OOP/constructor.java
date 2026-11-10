import java.util.ArrayList;
import java.util.List;

public class constructor {

    private int id;
    private String name;
    private String department;
    private List<String> skills;

    // Default constructor chaining
    public constructor() {
        this(0, "Guest", "General", new ArrayList<>());
    }

    // Two-parameter constructor chaining
    public constructor(int id, String name) {
        this(id, name, "Engineering", new ArrayList<>());
    }

    // Master parameterized constructor
    public constructor(int id, String name, String department, List<String> skills) {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative.");
        this.id = id;
        this.name = (name == null || name.isBlank()) ? "Anonymous" : name.trim();
        this.department = department;
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
    }

    // Deep copy constructor
    public constructor(constructor other) {
        if (other == null) throw new IllegalArgumentException("Source object cannot be null.");
        this.id = other.id;
        this.name = other.name;
        this.department = other.department;
        // Deep copy of mutable list
        this.skills = new ArrayList<>(other.skills);
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.isBlank()) {
            this.skills.add(skill.trim());
        }
    }

    public void display() {
        System.out.printf("ID: %-4d | Name: %-12s | Dept: %-14s | Skills: %s%n", id, name, department, skills);
    }

    public static void main(String[] args) {
        System.out.println("=== Constructor Chaining & Deep Copy Demo ===");

        constructor s1 = new constructor();
        constructor s2 = new constructor(101, "John");
        s2.addSkill("Java");
        s2.addSkill("SQL");

        constructor s3 = new constructor(102, "David", "Architecture", List.of("Microservices", "Docker", "AWS"));

        // Copy constructor deep-copy verification
        constructor s4 = new constructor(s2);
        s4.addSkill("Kubernetes");

        System.out.println("Default Constructor:");
        s1.display();

        System.out.println("\nParameterized Constructor:");
        s2.display();
        s3.display();

        System.out.println("\nDeep Copy Verification (Modifying s4 does not alter s2):");
        System.out.print("Original s2: "); s2.display();
        System.out.print("Cloned   s4: "); s4.display();
    }
}
