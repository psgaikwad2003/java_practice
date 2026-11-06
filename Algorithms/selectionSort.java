import java.util.Arrays;

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

            if (minIndex != i) {
                swap(arr, i, minIndex);
                metrics.swaps++;
            }
        }

        return metrics;
    }

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

            if (minIndex != start) {
                swap(arr, start, minIndex);
                metrics.swaps++;
                if (maxIndex == start) {
                    maxIndex = minIndex;
                }
            }

            if (maxIndex != end) {
                swap(arr, end, maxIndex);
                metrics.swaps++;
            }

            start++;
            end--;
        }

        return metrics;
    }

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

            int key = arr[minIndex];
            while (minIndex > i) {
                arr[minIndex] = arr[minIndex - 1];
                minIndex--;
            }
            arr[i] = key;
        }
    }

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
        int[] numbers = {64, 25, 12, 22, 11};
        System.out.println("Array before sorting: " + Arrays.toString(numbers));
        SortMetrics metrics1 = sort(numbers);
        System.out.println("Array after sorting : " + Arrays.toString(numbers));
        System.out.println("Metrics             : " + metrics1);

        int[] dualArr = {29, 10, 14, 37, 13, 89, 4, 33};
        System.out.println("\nBidirectional input: " + Arrays.toString(dualArr));
        SortMetrics metrics2 = bidirectionalSelectionSort(dualArr);
        System.out.println("Bidirectional sorted: " + Arrays.toString(dualArr));
        System.out.println("Metrics             : " + metrics2);

        int[] duplicateArr = {4, 5, 3, 2, 4, 1};
        stableSelectionSort(duplicateArr);
        System.out.println("\nStable sorted       : " + Arrays.toString(duplicateArr));

        int[] recArr = {90, 70, 50, 80, 60, 10, 40};
        selectionSortRecursive(recArr, 0);
        System.out.println("Recursive sorted    : " + Arrays.toString(recArr));

        int[] descArr = {5, 2, 9, 1, 5, 6};
        selectionSortDescending(descArr);
        System.out.println("Descending sorted   : " + Arrays.toString(descArr));
    }
}
