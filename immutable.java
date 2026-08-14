// Commit 3: Builder Pattern for Immutable Class
// Builder pattern solves the "telescoping constructor" problem.
// It lets you build a complex immutable object step by step.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class immutable {
    public static void main(String[] args) {

        // Using Builder to construct immutable Student (clean and readable)
        Student student = new Student.Builder("Priya", 21)
                .branch("Computer Science")
                .addSubject("Math")
                .addSubject("Physics")
                .addSubject("Chemistry")
                .build();

        System.out.println("=== Immutable Student via Builder Pattern ===");
        System.out.println("Name     : " + student.getName());
        System.out.println("Age      : " + student.getAge());
        System.out.println("Branch   : " + student.getBranch());
        System.out.println("Subjects : " + student.getSubjects());
    }
}

final class Student {

    private final int age;
    private final String name;
    private final String branch;
    private final List<String> subjects;

    // Private constructor — only accessible via Builder
    private Student(Builder builder) {
        this.age      = builder.age;
        this.name     = builder.name;
        this.branch   = builder.branch;
        this.subjects = Collections.unmodifiableList(new ArrayList<>(builder.subjects));
    }

    public int getAge()              { return age; }
    public String getName()          { return name; }
    public String getBranch()        { return branch; }
    public List<String> getSubjects(){ return subjects; }

    // =================== Builder Class ===================
    public static class Builder {

        // Required fields
        private final String name;
        private final int age;

        // Optional fields with defaults
        private String branch    = "Undeclared";
        private List<String> subjects = new ArrayList<>();

        public Builder(String name, int age) {
            this.name = name;
            this.age  = age;
        }

        public Builder branch(String branch) {
            this.branch = branch;
            return this; // method chaining
        }

        public Builder addSubject(String subject) {
            this.subjects.add(subject);
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
