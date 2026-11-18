import java.util.Arrays;

public class student_grade_analayzer {

    public static double calculateMedian(int[] marks) {
        if (marks == null || marks.length == 0) return 0.0;
        int[] copy = Arrays.copyOf(marks, marks.length);
        Arrays.sort(copy);
        int n = copy.length;
        if (n % 2 == 1) {
            return copy[n / 2];
        } else {
            return (copy[(n / 2) - 1] + copy[n / 2]) / 2.0;
        }
    }

    public static double calculateStandardDeviation(int[] marks, double average) {
        if (marks == null || marks.length <= 1) return 0.0;
        double sumSq = 0;
        for (int m : marks) {
            sumSq += Math.pow(m - average, 2);
        }
        return Math.sqrt(sumSq / marks.length);
    }

    public static double toGpaScale(double percentage) {
        if (percentage >= 90) return 4.0;
        if (percentage >= 80) return 3.5;
        if (percentage >= 70) return 3.0;
        if (percentage >= 60) return 2.5;
        if (percentage >= 50) return 2.0;
        return 0.0;
    }

    public static void main(String[] args) {
        System.out.println("=== Student Grade & Performance Analyzer ===");

        int[] marks = {78, 80, 92, 60, 35, 88, 95, 42, 67, 85};

        int total = 0;
        int highest = marks[0];
        int lowest = marks[0];
        int pass = 0;
        int fail = 0;

        for (int mark : marks) {
            total += mark;
            if (mark > highest) highest = mark;
            if (mark < lowest) lowest = mark;
            if (mark >= 40) pass++; else fail++;
        }

        double average = (double) total / marks.length;
        double median = calculateMedian(marks);
        double stdDev = calculateStandardDeviation(marks, average);
        double gpa = toGpaScale(average);

        System.out.println("Total Marks         : " + total);
        System.out.printf("Average Score       : %.2f%n", average);
        System.out.printf("Median Score        : %.2f%n", median);
        System.out.printf("Standard Deviation  : %.2f%n", stdDev);
        System.out.printf("GPA (4.0 Scale)     : %.2f%n", gpa);
        System.out.println("Highest Mark        : " + highest);
        System.out.println("Lowest Mark         : " + lowest);
        System.out.println("Passed Students     : " + pass);
        System.out.println("Failed Students     : " + fail);

        String letterGrade = (average >= 90) ? "A+"
                           : (average >= 80) ? "A"
                           : (average >= 70) ? "B"
                           : (average >= 60) ? "C"
                           : "F";
        System.out.println("Overall Cohort Grade: " + letterGrade);
    }
}
