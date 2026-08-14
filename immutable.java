// Commit 4: Override equals(), hashCode(), and toString()
// Immutable objects are often used as keys in Maps or stored in Sets.
// Overriding these methods is essential for correct behavior.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class immutable {
    public static void main(String[] args) {

        Student s1 = new Student.Builder("Priya", 21)
                .branch("Computer Science")
                .addSubject("Math")
                .addSubject("Physics")
                .build();

        Student s2 = new Student.Builder("Priya", 21)
                .branch("Computer Science")
                .addSubject("Math")
                .addSubject("Physics")
                .build();

        Student s3 = new Student.Builder("Rahul", 22)
                .branch("Mechanical")
                .addSubject("Thermodynamics")
                .build();

        System.out.println("=== equals(), hashCode(), toString() Demo ===");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s3: " + s3);

        System.out.println("\ns1.equals(s2) : " + s1.equals(s2)); // true
        System.out.println("s1.equals(s3) : " + s1.equals(s3)); // false

        System.out.println("\nhashCode(s1)  : " + s1.hashCode());
        System.out.println("hashCode(s2)  : " + s2.hashCode()); // same as s1
        System.out.println("hashCode(s3)  : " + s3.hashCode()); // different
    }
}

final class Student {

    private final int age;
    private final String name;
    private final String branch;
    private final List<String> subjects;

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

    // ======= equals() =======
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;               // same reference
        if (!(obj instanceof Student)) return false; // type check
        Student other = (Student) obj;
        return age == other.age
            && Objects.equals(name,     other.name)
            && Objects.equals(branch,   other.branch)
            && Objects.equals(subjects, other.subjects);
    }

    // ======= hashCode() =======
    @Override
    public int hashCode() {
        return Objects.hash(age, name, branch, subjects);
    }

    // ======= toString() =======
    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age
             + ", branch='" + branch + "', subjects=" + subjects + "}";
    }

    // =================== Builder ===================
    public static class Builder {
        private final String name;
        private final int age;
        private String branch = "Undeclared";
        private List<String> subjects = new ArrayList<>();

        public Builder(String name, int age) {
            this.name = name;
            this.age  = age;
        }

        public Builder branch(String branch) {
            this.branch = branch;
            return this;
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
