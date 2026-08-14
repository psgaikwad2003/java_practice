// Commit 2: Defensive Copying for Mutable Fields
// Problem: if an immutable class holds a mutable object (like List),
// callers could modify it externally and break immutability.
// Solution: Use defensive copying in constructor and getter.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class immutable {
    public static void main(String[] args) {
        List<String> subjects = new ArrayList<>();
        subjects.add("Math");
        subjects.add("Physics");
        subjects.add("Chemistry");

        Student student = new Student(21, "Priya", "Computer Science", subjects);

        // Modifying the original list does NOT affect the immutable object
        subjects.add("Biology"); // This should NOT affect student's subjects

        System.out.println("=== Immutable Student with Defensive Copy ===");
        System.out.println("Name     : " + student.getName());
        System.out.println("Age      : " + student.getAge());
        System.out.println("Branch   : " + student.getBranch());
        System.out.println("Subjects : " + student.getSubjects());
        System.out.println("\nExternal list modified, but student subjects unchanged!");
        System.out.println("Subjects still: " + student.getSubjects());
    }
}

final class Student {

    private final int age;
    private final String name;
    private final String branch;
    private final List<String> subjects; // mutable — needs defensive copy

    public Student(int age, String name, String branch, List<String> subjects) {
        this.age    = age;
        this.name   = name;
        this.branch = branch;
        // Defensive copy: create a new list so external changes don't affect us
        this.subjects = new ArrayList<>(subjects);
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

    // Return an unmodifiable view so caller cannot mutate our internal list
    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }
}
