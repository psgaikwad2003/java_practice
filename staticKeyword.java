
class Student {
    static String collegeName = "ABC College"; 
    static int totalStudents = 0;              

    int rollNo;
    String name;
    double marks;

    Student(int rollNo, String name, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
        totalStudents++; 
    }

    
    static void showCollegeInfo() {
        System.out.println("College Name  : " + collegeName);
        System.out.println("Total Students: " + totalStudents);
    }

    void display() {
        System.out.println("Roll No: " + rollNo + " | Name: " + name + " | Marks: " + marks);
    }

    String getGrade() {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else return "Fail";
    }
}

public class staticKeyword {
    public static void main(String[] args) {

        Student s1 = new Student(1, "Pranav", 92.5);
        Student s2 = new Student(2, "Riya", 78.0);
        Student s3 = new Student(3, "Aman", 55.0);

        System.out.println("========== Student Records ==========");
        Student.showCollegeInfo();
        System.out.println("-------------------------------------");

        for (Student s : new Student[]{s1, s2, s3}) {
            s.display();
            System.out.println("Grade: " + s.getGrade());
            System.out.println();
        }

        System.out.println("=====================================");
    }
}
