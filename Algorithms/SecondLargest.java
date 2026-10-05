import java.util.Arrays;
import java.util.OptionalInt;

public class SecondLargest {

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
            {45, 78, 12, 90, 67, 90, 23},
            {-10, -5, -20, -3, -50},
            {10, 10, 10},
            {42},
            {100, 200}
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
