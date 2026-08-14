
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class immutable {

    public static void main(String[] args) {

        // Build original student
        Student original = new Student.Builder("Priya", 21)
                .branch("Computer Science")
                .addSubject("Math")
                .addSubject("Physics")
                .addSubject("Chemistry")
                .build();

        // Use wither to produce an updated copy — original stays unchanged
        Student updated = original.withName("Priya Sharma").withAge(22);

        System.out.println("=== Final Immutable Student Demo ===");
        System.out.println("Original : " + original);
        System.out.println("Updated  : " + updated);

        System.out.println("\noriginal.equals(updated)  : " + original.equals(updated)); // false
        System.out.println("original hash : " + original.hashCode());
        System.out.println("updated  hash : " + updated.hashCode());
    }
}

/**
 * An immutable representation of a Student.
 * Once created, its state cannot be altered.
 */
final class Student {

    /** Student's age. */
    private final int age;

    /** Student's full name. */
    private final String name;

    /** Academic branch/department. */
    private final String branch;

    /** List of enrolled subjects — stored as an unmodifiable copy. */
    private final List<String> subjects;

    /**
     * Private constructor — use {@link Builder} to create instances.
     *
     * @param builder the builder containing field values
     */
    private Student(Builder builder) {
        this.age = builder.age;
        this.name = builder.name;
        this.branch = builder.branch;
        this.subjects = Collections.unmodifiableList(new ArrayList<>(builder.subjects));
    }

    // =================== Getters ===================

    /** @return student's age */
    public int getAge() {
        return age;
    }

    /** @return student's name */
    public String getName() {
        return name;
    }

    /** @return student's branch */
    public String getBranch() {
        return branch;
    }

    /** @return unmodifiable list of subjects */
    public List<String> getSubjects() {
        return subjects;
    }

    // =================== Wither Methods ===================

    /**
     * Returns a new {@code Student} with the given name, all other fields
     * unchanged.
     *
     * @param newName the new name
     * @return a new immutable Student
     */
    public Student withName(String newName) {
        return new Builder(newName, this.age)
                .branch(this.branch)
                .subjects(this.subjects)
                .build();
    }

    /**
     * Returns a new {@code Student} with the given age, all other fields unchanged.
     *
     * @param newAge the new age
     * @return a new immutable Student
     */
    public Student withAge(int newAge) {
        return new Builder(this.name, newAge)
                .branch(this.branch)
                .subjects(this.subjects)
                .build();
    }

    // =================== equals / hashCode / toString ===================

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Student))
            return false;
        Student other = (Student) obj;
        return age == other.age
                && Objects.equals(name, other.name)
                && Objects.equals(branch, other.branch)
                && Objects.equals(subjects, other.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, branch, subjects);
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age
                + ", branch='" + branch + "', subjects=" + subjects + "}";
    }

    // =================== Builder ===================

    /**
     * Builder for constructing {@link Student} instances fluently.
     */
    public static class Builder {

        private final String name;
        private final int age;
        private String branch = "Undeclared";
        private List<String> subjects = new ArrayList<>();

        /**
         * Required fields constructor.
         *
         * @param name student's name
         * @param age  student's age
         */
        public Builder(String name, int age) {
            this.name = name;
            this.age = age;
        }

        /** Sets the branch. */
        public Builder branch(String branch) {
            this.branch = branch;
            return this;
        }

        /** Adds a single subject. */
        public Builder addSubject(String subject) {
            this.subjects.add(subject);
            return this;
        }

        /** Replaces the full subjects list (defensive copy applied). */
        public Builder subjects(List<String> subjects) {
            this.subjects = new ArrayList<>(subjects);
            return this;
        }

        /** Builds and returns the immutable {@link Student}. */
        public Student build() {
            return new Student(this);
        }
    }
}
