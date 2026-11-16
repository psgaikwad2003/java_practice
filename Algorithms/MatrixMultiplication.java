public class MatrixMultiplication {

    // Standard O(N^3) with cache-friendly i-k-j loop order
    public static int[][] multiply(int[][] a, int[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("Matrices cannot be null or empty.");
        }
        int rowsA = a.length;
        int colsA = a[0].length;
        int rowsB = b.length;
        int colsB = b[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException(String.format(
                "Cannot multiply matrices: A columns (%d) != B rows (%d)", colsA, rowsB
            ));
        }

        int[][] result = new int[rowsA][colsB];

        // Cache-friendly i-k-j ordering minimizes CPU cache misses
        for (int i = 0; i < rowsA; i++) {
            for (int k = 0; k < colsA; k++) {
                int r = a[i][k];
                for (int j = 0; j < colsB; j++) {
                    result[i][j] += r * b[k][j];
                }
            }
        }
        return result;
    }

    // Binary exponentiation for square matrices: O(N^3 * log(power))
    public static int[][] power(int[][] matrix, int p) {
        if (p < 1) throw new IllegalArgumentException("Power must be >= 1");
        int n = matrix.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) result[i][i] = 1; // Identity matrix

        int[][] base = matrix;
        while (p > 0) {
            if ((p & 1) == 1) {
                result = multiply(result, base);
            }
            base = multiply(base, base);
            p >>= 1;
        }
        return result;
    }

    public static void printMatrix(String title, int[][] m) {
        System.out.println(title + ":");
        for (int[] row : m) {
            for (int val : row) {
                System.out.printf("%6d", val);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Cache-Friendly Matrix Multiplication ===");

        int[][] a = {
            {1, 2, 3},
            {4, 5, 6}
        };

        int[][] b = {
            {7, 8},
            {9, 1},
            {2, 3}
        };

        printMatrix("Matrix A (2x3)", a);
        printMatrix("Matrix B (3x2)", b);

        int[][] product = multiply(a, b);
        printMatrix("Product A * B (2x2)", product);

        int[][] square = {
            {1, 1},
            {1, 0}
        };
        printMatrix("\nFibonacci Matrix Base", square);
        printMatrix("Fibonacci Matrix ^ 5", power(square, 5));
    }
}
