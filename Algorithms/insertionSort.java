import java.util.Arrays;

public class insertionSort {

    public static class SortMetrics {
        public int comparisons = 0;
        public int shifts = 0;

        @Override
        public String toString() {
            return String.format("Comparisons: %d | Element Shifts: %d", comparisons, shifts);
        }
    }

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

    public static SortMetrics binaryInsertionSort(int[] arr) {
        SortMetrics metrics = new SortMetrics();
        if (arr == null || arr.length <= 1) return metrics;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int insertPos = binarySearchPosition(arr, key, 0, i - 1, metrics);

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
                low = mid + 1;
            }
        }
        return low;
    }

    public static void insertionSortRecursive(int[] arr, int n) {
        if (arr == null || n <= 1) return;

        insertionSortRecursive(arr, n - 1);

        int last = arr[n - 1];
        int j = n - 2;

        while (j >= 0 && arr[j] > last) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = last;
    }

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
        int[] numbers = {12, 11, 13, 5, 6};
        System.out.println("Array before sorting: " + Arrays.toString(numbers));
        SortMetrics metrics1 = sort(numbers);
        System.out.println("Array after sorting : " + Arrays.toString(numbers));
        System.out.println("Metrics             : " + metrics1);

        int[] binaryArr = {37, 23, 0, 17, 12, 72, 31, 46, 100, 88, 54};
        System.out.println("\nBinary insertion input: " + Arrays.toString(binaryArr));
        SortMetrics metricsBinary = binaryInsertionSort(binaryArr);
        System.out.println("Binary insertion sort : " + Arrays.toString(binaryArr));
        System.out.println("Metrics               : " + metricsBinary);

        int[] nearlySorted = {1, 2, 4, 3, 5, 6, 8, 7};
        int[] reversed = {8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("\nNearly sorted: " + Arrays.toString(nearlySorted));
        System.out.println("Metrics      : " + sort(nearlySorted));
        System.out.println("Reverse sorted: " + Arrays.toString(reversed));
        System.out.println("Metrics       : " + sort(reversed));

        int[] recArr = {9, 7, 5, 11, 12, 2, 14, 3};
        insertionSortRecursive(recArr, recArr.length);
        System.out.println("\nRecursive sorted: " + Arrays.toString(recArr));

        int[] descArr = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 6, 8};
        insertionSortDescending(descArr);
        System.out.println("Descending sorted: " + Arrays.toString(descArr));
    }
}
