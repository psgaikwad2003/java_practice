import java.util.Arrays;
import java.util.OptionalInt;

public class SecondLargest {

    /**
     * Finds the second largest distinct element in an array in single-pass O(n) time.
     * Returns OptionalInt.empty() if no distinct second largest exists (e.g., length < 2 or all elements equal).
     */
    public static OptionalInt findSecondLargest(int[] arr) {
        if (arr == null || arr.length < 2) {
            return OptionalInt.empty();
        }

        Integer largest = null;
        Integer secondLargest = null;

        for (int num : arr) {
            if (largest == null || num > largest) {
                secondLargest = largest;
                largest = num;
            } else if (num < largest && (secondLargest == null || num > secondLargest)) {
                secondLargest = num;
            }
        }

        return (secondLargest != null) ? OptionalInt.of(secondLargest) : OptionalInt.empty();
    }

    /**
     * Finds the second smallest distinct element in an array in single-pass O(n) time.
     */
    public static OptionalInt findSecondSmallest(int[] arr) {
        if (arr == null || arr.length < 2) {
            return OptionalInt.empty();
        }

        Integer smallest = null;
        Integer secondSmallest = null;

        for (int num : arr) {
            if (smallest == null || num < smallest) {
                secondSmallest = smallest;
                smallest = num;
            } else if (num > smallest && (secondSmallest == null || num < secondSmallest)) {
                secondSmallest = num;
            }
        }

        return (secondSmallest != null) ? OptionalInt.of(secondSmallest) : OptionalInt.empty();
    }

    public static void main(String[] args) {
        System.out.println("=== Second Largest & Smallest Number Analysis ===");

        int[][] testCases = {
            {45, 78, 12, 90, 67, 90, 23},    // Duplicate largest (90, 90) -> second largest is 78
            {-10, -5, -20, -3, -50},         // All negative values
            {10, 10, 10},                    // All identical values (no second largest)
            {42},                            // Single element
            {100, 200}                       // Two distinct elements
        };

        for (int[] arr : testCases) {
            System.out.println("\nArray: " + Arrays.toString(arr));
            OptionalInt secondLargest = findSecondLargest(arr);
            OptionalInt secondSmallest = findSecondSmallest(arr);

            System.out.println("  Second Largest:  " + (secondLargest.isPresent() ? secondLargest.getAsInt() : "None"));
            System.out.println("  Second Smallest: " + (secondSmallest.isPresent() ? secondSmallest.getAsInt() : "None"));
        }
    }
}