import java.util.Arrays;

/**
 * Bubble Sort Algorithm Suite
 *
 * Implements multiple variations of the exchange-sort paradigm:
 * 1. Optimized Bubble Sort - Early termination with swapped flag (O(n) best case)
 * 2. Bidirectional Bubble Sort (Cocktail Shaker Sort) - Alternates left-to-right and right-to-left
 * 3. Recursive Bubble Sort - Recursive pass processing
 * 4. Descending Order Bubble Sort
 *
 * Complexity:
 * - Time: Best O(n), Average O(n^2), Worst O(n^2)
 * - Space: O(1) auxiliary (in-place & stable)
 */
public class bubbleSort {

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
    // 1. Optimized Bubble Sort (Early Exit with Swapped Flag)
    // -------------------------------------------------------------
    public static SortMetrics bubbleSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            metrics.passes++;

            // Last i elements are already sorted
            for (int j = 0; j < n - i - 1; j++) {
                metrics.comparisons++;
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    metrics.swaps++;
                    swapped = true;
                }
            }

            // If no two elements were swapped, array is sorted
            if (!swapped) break;
        }

        return metrics;
    }

    // -------------------------------------------------------------
    // 2. Cocktail Shaker Sort (Bidirectional Bubble Sort)
    // Eliminates "turtles" (small values near the end of array)
    // -------------------------------------------------------------
    public static SortMetrics cocktailShakerSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        boolean swapped = true;
        int start = 0;
        int end = arr.length - 1;

        while (swapped) {
            swapped = false;
            metrics.passes++;

            // Forward pass: bubble largest element to the end
            for (int i = start; i < end; i++) {
                metrics.comparisons++;
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    metrics.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;

            swapped = false;
            end--;

            // Backward pass: bubble smallest element to the beginning
            for (int i = end - 1; i >= start; i--) {
                metrics.comparisons++;
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    metrics.swaps++;
                    swapped = true;
                }
            }

            start++;
        }

        return metrics;
    }

    // -------------------------------------------------------------
    // 3. Recursive Bubble Sort
    // -------------------------------------------------------------
    public static void bubbleSortRecursive(int[] arr, int n) {
        if (arr == null || n <= 1) return;

        boolean swapped = false;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                swap(arr, i, i + 1);
                swapped = true;
            }
        }

        // If no swap occurred, array is already sorted
        if (!swapped) return;

        // Largest element is fixed at index n-1, recurse for remaining
        bubbleSortRecursive(arr, n - 1);
    }

    // -------------------------------------------------------------
    // 4. Descending Order Bubble Sort
    // -------------------------------------------------------------
    public static void bubbleSortDescending(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("                BUBBLE SORT ALGORITHM SUITE                  ");
        System.out.println("=============================================================");

        // Demo 1: Original array sorted in ascending order
        int[] numbers = {50, 40, 30, 20, 10};
        System.out.println("\nDemo 1: Original Reverse-Sorted Input");
        System.out.println("Initial array: " + Arrays.toString(numbers));
        SortMetrics metrics1 = bubbleSort(numbers);
        System.out.println("Sorted array : " + Arrays.toString(numbers));
        System.out.println("Performance  : " + metrics1);

        // Demo 2: Already-sorted array demonstrating early termination O(n)
        int[] sorted = {10, 20, 30, 40, 50};
        System.out.println("\nDemo 2: Already-Sorted Array (Early Termination Check)");
        System.out.println("Initial array: " + Arrays.toString(sorted));
        SortMetrics metrics2 = bubbleSort(sorted);
        System.out.println("Sorted array : " + Arrays.toString(sorted));
        System.out.println("Performance  : " + metrics2 + " (Exited in 1 pass!)");

        // Demo 3: Cocktail Shaker Sort with Turtles
        int[] turtles = {2, 3, 4, 5, 6, 1};
        System.out.println("\nDemo 3: Cocktail Shaker Sort (Bidirectional)");
        System.out.println("Input with turtle element (1 at end): " + Arrays.toString(turtles));
        SortMetrics metricsCocktail = cocktailShakerSort(turtles);
        System.out.println("Cocktail sorted: " + Arrays.toString(turtles));
        System.out.println("Performance    : " + metricsCocktail);

        // Demo 4: Recursive Bubble Sort
        int[] recArr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("\nDemo 4: Recursive Bubble Sort");
        System.out.println("Initial array: " + Arrays.toString(recArr));
        bubbleSortRecursive(recArr, recArr.length);
        System.out.println("Sorted array : " + Arrays.toString(recArr));

        // Demo 5: Descending Order
        int[] descArr = {15, 3, 98, 24, 51, 6};
        System.out.println("\nDemo 5: Descending Order Bubble Sort");
        System.out.println("Initial array: " + Arrays.toString(descArr));
        bubbleSortDescending(descArr);
        System.out.println("Sorted array : " + Arrays.toString(descArr));

        System.out.println("\n=============================================================");
    }
}
