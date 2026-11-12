import java.util.OptionalInt;

public class ArrayOutOfBound {

    public static OptionalInt safeGet(int[] arr, int index) {
        if (arr == null || index < 0 || index >= arr.length) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(arr[index]);
    }

    public static OptionalInt safeGet2D(int[][] matrix, int row, int col) {
        if (matrix == null || row < 0 || row >= matrix.length) return OptionalInt.empty();
        if (matrix[row] == null || col < 0 || col >= matrix[row].length) return OptionalInt.empty();
        return OptionalInt.of(matrix[row][col]);
    }

    public static void main(String[] args) {
        System.out.println("=== Array Index Bounds Safety Demonstrations ===");

        int[] numbers = {10, 20, 30, 40, 50};

        // Standard try-catch approach
        try {
            System.out.println("Accessing index 10: " + numbers[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("[Handled] Index out of bounds: " + e.getMessage());
        }

        // Negative index handling
        try {
            System.out.println("Accessing index -1: " + numbers[-1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("[Handled] Negative index: " + e.getMessage());
        }

        // Functional safe retrieval
        int[] queryIndices = {0, 2, 4, 5, -2, 10};
        System.out.println("\nSafe queries on array of length " + numbers.length + ":");
        for (int idx : queryIndices) {
            OptionalInt val = safeGet(numbers, idx);
            System.out.printf("Index %3d -> %s%n", idx, val.isPresent() ? String.valueOf(val.getAsInt()) : "OUT_OF_BOUNDS");
        }

        // 2D Matrix boundary guard
        int[][] grid = {
            {1, 2, 3},
            {4, 5, 6}
        };
        System.out.println("\n2D Safe access grid[1][2]: " + safeGet2D(grid, 1, 2));
        System.out.println("2D Safe access grid[2][0]: " + safeGet2D(grid, 2, 0));
    }
}
