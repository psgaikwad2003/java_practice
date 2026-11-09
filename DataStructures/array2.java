import java.util.Arrays;

public class array2 {

    public static double[] calculateRowAverages(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return new double[0];
        double[] averages = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int sum = 0;
            for (int val : matrix[i]) sum += val;
            averages[i] = matrix[i].length > 0 ? (double) sum / matrix[i].length : 0.0;
        }
        return averages;
    }

    public static double[] calculateColAverages(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return new double[0];
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] averages = new double[cols];

        for (int j = 0; j < cols; j++) {
            int sum = 0;
            for (int i = 0; i < rows; i++) {
                sum += matrix[i][j];
            }
            averages[j] = (double) sum / rows;
        }
        return averages;
    }

    public static int[][] transpose(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return new int[0][0];
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transposed = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 2D Gradebook & Matrix Operations ===");

        int[][] studentMarks = {
            {90, 85, 78}, // Student 1
            {82, 79, 94}, // Student 2
            {68, 72, 75}, // Student 3
            {95, 91, 89}  // Student 4
        };

        System.out.println("Student Marks Matrix (Rows=Students, Cols=Subjects):");
        printMatrix(studentMarks);

        double[] rowAvg = calculateRowAverages(studentMarks);
        System.out.println("\nStudent Averages:");
        for (int i = 0; i < rowAvg.length; i++) {
            System.out.printf("Student %d: %.2f%n", i + 1, rowAvg[i]);
        }

        double[] colAvg = calculateColAverages(studentMarks);
        System.out.println("\nSubject Averages:");
        for (int j = 0; j < colAvg.length; j++) {
            System.out.printf("Subject %d: %.2f%n", j + 1, colAvg[j]);
        }

        System.out.println("\nTransposed Matrix (Rows=Subjects, Cols=Students):");
        printMatrix(transpose(studentMarks));
    }
}
