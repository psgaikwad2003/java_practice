// Commit 1: Basic Immutable Class in Java
// An immutable object cannot be changed after it is created.
// Rules: final class, private final fields, no setters, initialize via constructor.

public class immutable {
    public static void main(String[] args) {
        // Creating an immutable Student object
        Student student = new Student(21, "Priya", "Computer Science");

        System.out.println("=== Immutable Student ===");
        System.out.println("Name   : " + student.getName());
        System.out.println("Age    : " + student.getAge());
        System.out.println("Branch : " + student.getBranch());

        // Attempting to "change" creates a new object, original is unchanged
        System.out.println("\nOriginal student remains unchanged.");
    }
}

// 'final' keyword ensures this class cannot be subclassed
final class Student {

    // All fields are private and final
    private final int age;
    private final String name;
    private final String branch;

    // Constructor initializes all fields
    public Student(int age, String name, String branch) {
        this.age    = age;
        this.name   = name;
        this.branch = branch;
    }

    // Only getters — no setters
    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }
}
