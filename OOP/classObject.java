import java.util.ArrayList;
import java.util.List;

public class classObject {

    private int rollno;
    private String name;
    private String department;
    private double gpa;

    public classObject() {
        this(0, "Unknown", "General", 0.0);
    }

    public classObject(int rollno, String name) {
        this(rollno, name, "Computer Science", 0.0);
    }

    public classObject(int rollno, String name, String department, double gpa) {
        setRollno(rollno);
        setName(name);
        setDepartment(department);
        setGpa(gpa);
    }

    public classObject(classObject other) {
        if (other != null) {
            this.rollno = other.rollno;
            this.name = other.name;
            this.department = other.department;
            this.gpa = other.gpa;
        }
    }

    public int getRollno() { return rollno; }
    public void setRollno(int rollno) {
        if (rollno < 0) throw new IllegalArgumentException("Roll number cannot be negative.");
        this.rollno = rollno;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = (name == null || name.isBlank()) ? "Unnamed" : name.trim();
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        this.department = (department == null || department.isBlank()) ? "Undeclared" : department.trim();
    }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        this.gpa = gpa;
    }

    public void display() {
        System.out.printf("Roll No: %-4d | Name: %-15s | Dept: %-18s | GPA: %.2f%n", rollno, name, department, gpa);
    }

    @Override
    public String toString() {
        return "Student[rollno=" + rollno + ", name='" + name + "', dept='" + department + "', gpa=" + gpa + "]";
    }

    public static void main(String[] args) {
        System.out.println("=== Class & Object Encapsulation Demo ===");

        classObject s1 = new classObject();
        s1.setRollno(101);
        s1.setName("John Doe");
        s1.setDepartment("Computer Science");
        s1.setGpa(3.85);

        classObject s2 = new classObject(102, "Alice Smith", "Data Science", 3.92);
        classObject s3 = new classObject(s2);
        s3.setRollno(103);
        s3.setName("Bob Miller");

        List<classObject> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);

        for (classObject s : students) {
            s.display();
        }
    }
}
