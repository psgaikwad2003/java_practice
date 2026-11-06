import java.util.Arrays;

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

    public static SortMetrics bubbleSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            metrics.passes++;

            for (int j = 0; j < n - i - 1; j++) {
                metrics.comparisons++;
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    metrics.swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        return metrics;
    }

    public static SortMetrics cocktailShakerSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        boolean swapped = true;
        int start = 0;
        int end = arr.length - 1;

        while (swapped) {
            swapped = false;
            metrics.passes++;

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

    public static void bubbleSortRecursive(int[] arr, int n) {
        if (arr == null || n <= 1) return;

        boolean swapped = false;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                swap(arr, i, i + 1);
                swapped = true;
            }
        }

        if (!swapped) return;
        bubbleSortRecursive(arr, n - 1);
    }

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
        int[] numbers = {50, 40, 30, 20, 10};
        System.out.println("Original array: " + Arrays.toString(numbers));
        SortMetrics metrics1 = bubbleSort(numbers);
        System.out.println("Sorted array  : " + Arrays.toString(numbers));
        System.out.println("Metrics       : " + metrics1);

        int[] sorted = {10, 20, 30, 40, 50};
        System.out.println("\nSorted input check: " + Arrays.toString(sorted));
        SortMetrics metrics2 = bubbleSort(sorted);
        System.out.println("Early exit metrics: " + metrics2);

        int[] turtles = {2, 3, 4, 5, 6, 1};
        System.out.println("\nCocktail input: " + Arrays.toString(turtles));
        SortMetrics metricsCocktail = cocktailShakerSort(turtles);
        System.out.println("Cocktail sorted: " + Arrays.toString(turtles));
        System.out.println("Metrics        : " + metricsCocktail);

        int[] recArr = {64, 34, 25, 12, 22, 11, 90};
        bubbleSortRecursive(recArr, recArr.length);
        System.out.println("\nRecursive sorted: " + Arrays.toString(recArr));

        int[] descArr = {15, 3, 98, 24, 51, 6};
        bubbleSortDescending(descArr);
        System.out.println("Descending sorted: " + Arrays.toString(descArr));
    }
}
