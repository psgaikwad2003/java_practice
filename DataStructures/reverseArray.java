import java.util.Arrays;

public class reverseArray {

    /**
     * Reverses an integer array in-place using the two-pointer technique.
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public static void reverse(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        reverse(arr, 0, arr.length - 1);
    }

    /**
     * Reverses a specific subarray in-place between start and end indices (inclusive).
     */
    public static void reverse(int[] arr, int start, int end) {
        if (arr == null) return;
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * Generic in-place reversal for object arrays.
     */
    public static <T> void reverse(T[] arr) {
        if (arr == null || arr.length <= 1) return;
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            T temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * Rotates an array to the right by k positions using the three-reversal algorithm.
     */
    public static void rotateRight(int[] arr, int k) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;
        k = k % n;
        if (k < 0) k += n;

        // 1. Reverse the entire array
        reverse(arr, 0, n - 1);
        // 2. Reverse the first k elements
        reverse(arr, 0, k - 1);
        // 3. Reverse the remaining elements
        reverse(arr, k, n - 1);
    }

    public static void main(String[] args) {
        System.out.println("=== In-Place Array Reversal (Two-Pointer) ===");
        int[] numbers = {10, 20, 30, 40, 50};
        System.out.println("Original: " + Arrays.toString(numbers));
        reverse(numbers);
        System.out.println("Reversed: " + Arrays.toString(numbers));

        System.out.println("\n=== Generic Object Array Reversal ===");
        String[] words = {"Java", "Python", "Go", "Rust", "Kotlin"};
        System.out.println("Original: " + Arrays.toString(words));
        reverse(words);
        System.out.println("Reversed: " + Arrays.toString(words));

        System.out.println("\n=== Application: Array Rotation using Reversals ===");
        int[] rotateDemo = {1, 2, 3, 4, 5, 6, 7};
        int k = 3;
        System.out.println("Original:          " + Arrays.toString(rotateDemo));
        rotateRight(rotateDemo, k);
        System.out.println("Rotated by " + k + " steps: " + Arrays.toString(rotateDemo));
    }
}
