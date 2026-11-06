import java.util.Arrays;

/**
 * Insertion Sort Algorithm Suite
 *
 * Implements multiple variations of the incremental insertion paradigm:
 * 1. Standard Insertion Sort - Adaptive O(n) best-case for nearly sorted inputs
 * 2. Binary Insertion Sort - Uses binary search to reduce comparisons to O(n log n)
 * 3. Recursive Insertion Sort
 * 4. Descending Order Insertion Sort
 *
 * Complexity:
 * - Time: Best O(n), Average O(n^2), Worst O(n^2)
 * - Space: O(1) auxiliary (in-place & stable)
 */
public class insertionSort {

    public static class SortMetrics {
        public int comparisons = 0;
        public int shifts = 0;

        @Override
        public String toString() {
            return String.format("Comparisons: %d | Element Shifts: %d", comparisons, shifts);
        }
    }

    // -------------------------------------------------------------
    // 1. Standard Insertion Sort
    // -------------------------------------------------------------
    public static SortMetrics sort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0) {
                metrics.comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    metrics.shifts++;
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }

        return metrics;
    }

    // -------------------------------------------------------------
    // 2. Binary Insertion Sort
    // Reduces comparison complexity from O(n) to O(log n) per element
    // -------------------------------------------------------------
    public static SortMetrics binaryInsertionSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            // Binary search for insertion position in arr[0..i-1]
            int insertPos = binarySearchPosition(arr, key, 0, i - 1, metrics);

            // Shift elements to make room for key
            for (int j = i - 1; j >= insertPos; j--) {
                arr[j + 1] = arr[j];
                metrics.shifts++;
            }
            arr[insertPos] = key;
        }

        return metrics;
    }

    private static int binarySearchPosition(int[] arr, int key, int low, int high, SortMetrics metrics) {
        while (low <= high) {
            int mid = low + (high - low) / 2;
            metrics.comparisons++;
            if (arr[mid] > key) {
                high = mid - 1;
            } else {
                low = mid + 1; // Maintains stability: inserts after equal elements
            }
        }
        return low;
    }

    // -------------------------------------------------------------
    // 3. Recursive Insertion Sort
    // -------------------------------------------------------------
    public static void insertionSortRecursive(int[] arr, int n) {
        if (arr == null || n <= 1) return;

        // Sort first n-1 elements
        insertionSortRecursive(arr, n - 1);

        // Insert last element at its correct position in sorted array
        int last = arr[n - 1];
        int j = n - 2;

        while (j >= 0 && arr[j] > last) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = last;
    }

    // -------------------------------------------------------------
    // 4. Descending Order Insertion Sort
    // -------------------------------------------------------------
    public static void insertionSortDescending(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] < key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("              INSERTION SORT ALGORITHM SUITE                 ");
        System.out.println("=============================================================");

        // Demo 1: Original array
        int[] numbers = {12, 11, 13, 5, 6};
        System.out.println("\nDemo 1: Standard Insertion Sort (Original Test Input)");
        System.out.println("Array before sorting: " + Arrays.toString(numbers));
        SortMetrics metrics1 = sort(numbers);
        System.out.println("Array after sorting : " + Arrays.toString(numbers));
        System.out.println("Performance metrics : " + metrics1);

        // Demo 2: Binary Insertion Sort (Logarithmic comparisons)
        int[] binaryArr = {37, 23, 0, 17, 12, 72, 31, 46, 100, 88, 54};
        System.out.println("\nDemo 2: Binary Insertion Sort (O(log n) Binary Search Position)");
        System.out.println("Initial array: " + Arrays.toString(binaryArr));
        SortMetrics metricsBinary = binaryInsertionSort(binaryArr);
        System.out.println("Sorted array : " + Arrays.toString(binaryArr));
        System.out.println("Performance  : " + metricsBinary);

        // Demo 3: Adaptive Performance on Nearly-Sorted vs Reversed Arrays
        int[] nearlySorted = {1, 2, 4, 3, 5, 6, 8, 7};
        int[] reversed = {8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("\nDemo 3: Adaptive Behavior Comparison");
        System.out.println("Nearly sorted input: " + Arrays.toString(nearlySorted));
        SortMetrics mNear = sort(nearlySorted);
        System.out.println("Nearly sorted stats: " + mNear + " (Near-linear O(n)!)");

        System.out.println("\nReverse sorted input: " + Arrays.toString(reversed));
        SortMetrics mRev = sort(reversed);
        System.out.println("Reverse sorted stats: " + mRev + " (Worst case O(n^2))");

        // Demo 4: Recursive Insertion Sort
        int[] recArr = {9, 7, 5, 11, 12, 2, 14, 3};
        System.out.println("\nDemo 4: Recursive Insertion Sort");
        System.out.println("Initial array: " + Arrays.toString(recArr));
        insertionSortRecursive(recArr, recArr.length);
        System.out.println("Sorted array : " + Arrays.toString(recArr));

        // Demo 5: Descending Order
        int[] descArr = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 6, 8};
        System.out.println("\nDemo 5: Descending Insertion Sort");
        System.out.println("Initial array: " + Arrays.toString(descArr));
        insertionSortDescending(descArr);
        System.out.println("Sorted array : " + Arrays.toString(descArr));

        System.out.println("\n=============================================================");
    }
}
