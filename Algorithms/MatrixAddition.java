public class MatrixAddition {

    public static int[][] add(int[][] a, int[][] b) {
        validateSameDimensions(a, b);
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public static int[][] subtract(int[][] a, int[][] b) {
        validateSameDimensions(a, b);
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    public static int[][] scalarMultiply(int[][] matrix, int scalar) {
        if (matrix == null) throw new IllegalArgumentException("Matrix cannot be null");
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }
        return result;
    }

    private static void validateSameDimensions(int[][] a, int[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("Matrices cannot be null or empty.");
        }
        if (a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException(String.format(
                "Dimension mismatch: Matrix A (%dx%d) vs Matrix B (%dx%d)",
                a.length, a[0].length, b.length, b[0].length
            ));
        }
    }

    public static void printMatrix(String title, int[][] m) {
        System.out.println(title + ":");
        for (int[] row : m) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] a = {{1, 3, 4}, {2, 4, 3}, {3, 4, 5}};
        int[][] b = {{1, 3, 4}, {2, 4, 3}, {1, 2, 4}};

        printMatrix("Matrix A", a);
        printMatrix("Matrix B", b);

        int[][] sum = add(a, b);
        printMatrix("A + B (Addition)", sum);

        int[][] diff = subtract(a, b);
        printMatrix("A - B (Subtraction)", diff);

        int[][] scaled = scalarMultiply(a, 3);
        printMatrix("3 * A (Scalar)", scaled);
    }
}
