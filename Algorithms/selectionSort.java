import java.util.Arrays;

/**
 * Selection Sort Algorithm Suite
 *
 * Implements multiple variations of the selection-sort paradigm:
 * 1. Standard Selection Sort - Minimizes swaps (at most n-1 swaps total)
 * 2. Bidirectional Selection Sort (Double Selection) - Finds both min and max per pass (halves iterations)
 * 3. Stable Selection Sort - Uses array shifting rather than direct swapping to maintain stability
 * 4. Recursive Selection Sort
 * 5. Descending Order Selection Sort
 *
 * Complexity:
 * - Time: O(n^2) in all cases (Best, Average, Worst)
 * - Swaps: O(n) (optimal write performance)
 * - Space: O(1) auxiliary
 */
public class selectionSort {

    public static class SortMetrics {
        public int comparisons = 0;
        public int swaps = 0;
        public int passes = 0;

        @Override
        public String toString() {
            return String.format("Passes: %d | Comparisons: %d | Swaps: %d", passes, comparisons, swaps);
        }
    }

    // -------------------------------------------------------------
    // 1. Standard Selection Sort (Swap Minimization)
    // -------------------------------------------------------------
    public static SortMetrics sort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            metrics.passes++;
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                metrics.comparisons++;
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Only swap if min element is not already at current position
            if (minIndex != i) {
                swap(arr, i, minIndex);
                metrics.swaps++;
            }
        }

        return metrics;
    }

    // -------------------------------------------------------------
    // 2. Bidirectional Selection Sort (Double Selection Sort)
    // Simultaneously finds min and max in a single pass
    // -------------------------------------------------------------
    public static SortMetrics bidirectionalSelectionSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        int start = 0;
        int end = arr.length - 1;

        while (start < end) {
            metrics.passes++;
            int minIndex = start;
            int maxIndex = start;

            for (int i = start; i <= end; i++) {
                metrics.comparisons += 2;
                if (arr[i] < arr[minIndex]) {
                    minIndex = i;
                }
                if (arr[i] > arr[maxIndex]) {
                    maxIndex = i;
                }
            }

            // Move minimum to the start
            if (minIndex != start) {
                swap(arr, start, minIndex);
                metrics.swaps++;
                // If the maximum element was at start, its position changed to minIndex
                if (maxIndex == start) {
                    maxIndex = minIndex;
                }
            }

            // Move maximum to the end
            if (maxIndex != end) {
                swap(arr, end, maxIndex);
                metrics.swaps++;
            }

            start++;
            end--;
        }

        return metrics;
    }

    // -------------------------------------------------------------
    // 3. Stable Selection Sort
    // Shifts elements rightward instead of swapping to preserve stability
    // -------------------------------------------------------------
    public static void stableSelectionSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Shift elements right and insert minimum at index i
            int key = arr[minIndex];
            while (minIndex > i) {
                arr[minIndex] = arr[minIndex - 1];
                minIndex--;
            }
            arr[i] = key;
        }
    }

    // -------------------------------------------------------------
    // 4. Recursive Selection Sort
    // -------------------------------------------------------------
    public static void selectionSortRecursive(int[] arr, int startIndex) {
        if (arr == null || startIndex >= arr.length - 1) return;

        int minIndex = startIndex;
        for (int i = startIndex + 1; i < arr.length; i++) {
            if (arr[i] < arr[minIndex]) {
                minIndex = i;
            }
        }

        if (minIndex != startIndex) {
            swap(arr, startIndex, minIndex);
        }

        selectionSortRecursive(arr, startIndex + 1);
    }

    // -------------------------------------------------------------
    // 5. Descending Order Selection Sort
    // -------------------------------------------------------------
    public static void selectionSortDescending(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] > arr[maxIndex]) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                swap(arr, i, maxIndex);
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("              SELECTION SORT ALGORITHM SUITE                 ");
        System.out.println("=============================================================");

        // Demo 1: Original array
        int[] numbers = {64, 25, 12, 22, 11};
        System.out.println("\nDemo 1: Standard Selection Sort (Original Test Input)");
        System.out.println("Array before sorting: " + Arrays.toString(numbers));
        SortMetrics metrics1 = sort(numbers);
        System.out.println("Array after sorting : " + Arrays.toString(numbers));
        System.out.println("Performance metrics : " + metrics1 + " (Max n-1 swaps guaranteed)");

        // Demo 2: Bidirectional Selection Sort (Double Selection)
        int[] dualArr = {29, 10, 14, 37, 13, 89, 4, 33};
        System.out.println("\nDemo 2: Bidirectional Dual-Pivot Selection Sort");
        System.out.println("Initial array: " + Arrays.toString(dualArr));
        SortMetrics metrics2 = bidirectionalSelectionSort(dualArr);
        System.out.println("Sorted array : " + Arrays.toString(dualArr));
        System.out.println("Performance  : " + metrics2 + " (Half the outer iterations!)");

        // Demo 3: Stable Selection Sort
        int[] duplicateArr = {4, 5, 3, 2, 4, 1};
        System.out.println("\nDemo 3: Stable Selection Sort (Preserves Duplicate Order)");
        System.out.println("Initial array: " + Arrays.toString(duplicateArr));
        stableSelectionSort(duplicateArr);
        System.out.println("Sorted array : " + Arrays.toString(duplicateArr));

        // Demo 4: Recursive Selection Sort
        int[] recArr = {90, 70, 50, 80, 60, 10, 40};
        System.out.println("\nDemo 4: Recursive Selection Sort");
        System.out.println("Initial array: " + Arrays.toString(recArr));
        selectionSortRecursive(recArr, 0);
        System.out.println("Sorted array : " + Arrays.toString(recArr));

        // Demo 5: Descending Order Selection Sort
        int[] descArr = {5, 2, 9, 1, 5, 6};
        System.out.println("\nDemo 5: Descending Selection Sort");
        System.out.println("Initial array: " + Arrays.toString(descArr));
        selectionSortDescending(descArr);
        System.out.println("Sorted array : " + Arrays.toString(descArr));

        System.out.println("\n=============================================================");
    }
}
