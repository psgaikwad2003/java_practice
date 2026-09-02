
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class immutable {

    public static void main(String[] args) {

        
        Student original = new Student.Builder("Priya", 21)
                .branch("Computer Science")
                .addSubject("Math")
                .addSubject("Physics")
                .addSubject("Chemistry")
                .build();

        
        Student updated = original.withName("Priya Sharma").withAge(22);

        System.out.println("=== Final Immutable Student Demo ===");
        System.out.println("Original : " + original);
        System.out.println("Updated  : " + updated);

        System.out.println("\noriginal.equals(updated)  : " + original.equals(updated)); 
        System.out.println("original hash : " + original.hashCode());
        System.out.println("updated  hash : " + updated.hashCode());
    }
}


final class Student {

    
    private final int age;

    
    private final String name;

    
    private final String branch;

    
    private final List<String> subjects;

    
    private Student(Builder builder) {
        this.age = builder.age;
        this.name = builder.name;
        this.branch = builder.branch;
        this.subjects = Collections.unmodifiableList(new ArrayList<>(builder.subjects));
    }

    

    
    public int getAge() {
        return age;
    }

    
    public String getName() {
        return name;
    }

    
    public String getBranch() {
        return branch;
    }

    
    public List<String> getSubjects() {
        return subjects;
    }

    

    
    public Student withName(String newName) {
        return new Builder(newName, this.age)
                .branch(this.branch)
                .subjects(this.subjects)
                .build();
    }

    
    public Student withAge(int newAge) {
        return new Builder(this.name, newAge)
                .branch(this.branch)
                .subjects(this.subjects)
                .build();
    }

    

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

    

    
    public static class Builder {

        private final String name;
        private final int age;
        private String branch = "Undeclared";
        private List<String> subjects = new ArrayList<>();

        
        public Builder(String name, int age) {
            this.name = name;
            this.age = age;
        }

        
        public Builder branch(String branch) {
            this.branch = branch;
            return this;
        }

        
        public Builder addSubject(String subject) {
            this.subjects.add(subject);
            return this;
        }

        
        public Builder subjects(List<String> subjects) {
            this.subjects = new ArrayList<>(subjects);
            return this;
        }

        
        public Student build() {
            return new Student(this);
        }
    }
}
