import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

public class QuickSortDemo {

    private static final Random RANDOM = new Random();

    public static void sortLomuto(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSortLomuto(arr, 0, arr.length - 1);
    }

    private static void quickSortLomuto(int[] arr, int low, int high) {
        if (low < high) {
            int pi = randomizedLomutoPartition(arr, low, high);
            quickSortLomuto(arr, low, pi - 1);
            quickSortLomuto(arr, pi + 1, high);
        }
    }

    private static int randomizedLomutoPartition(int[] arr, int low, int high) {
        int randomPivot = low + RANDOM.nextInt(high - low + 1);
        swap(arr, randomPivot, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    public static void sortHoare(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSortHoare(arr, 0, arr.length - 1);
    }

    private static void quickSortHoare(int[] arr, int low, int high) {
        if (low < high) {
            int pi = hoarePartition(arr, low, high);
            quickSortHoare(arr, low, pi);
            quickSortHoare(arr, pi + 1, high);
        }
    }

    private static int hoarePartition(int[] arr, int low, int high) {
        int pivot = arr[low + (high - low) / 2];
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);

            do {
                j--;
            } while (arr[j] > pivot);

            if (i >= j) return j;
            swap(arr, i, j);
        }
    }

    public static void sortMedianOfThree(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSortMedian(arr, 0, arr.length - 1);
    }

    private static void quickSortMedian(int[] arr, int low, int high) {
        if (low >= high) return;

        if (high - low <= 2) {
            manualSortSmall(arr, low, high);
            return;
        }

        int mid = low + (high - low) / 2;

        if (arr[low] > arr[mid]) swap(arr, low, mid);
        if (arr[low] > arr[high]) swap(arr, low, high);
        if (arr[mid] > arr[high]) swap(arr, mid, high);

        swap(arr, mid, high - 1);
        int pivot = arr[high - 1];

        int i = low;
        int j = high - 1;

        while (true) {
            while (arr[++i] < pivot);
            while (arr[--j] > pivot);
            if (i >= j) break;
            swap(arr, i, j);
        }

        swap(arr, i, high - 1);
        quickSortMedian(arr, low, i - 1);
        quickSortMedian(arr, i + 1, high);
    }

    private static void manualSortSmall(int[] arr, int low, int high) {
        int size = high - low + 1;
        if (size <= 1) return;
        if (size == 2) {
            if (arr[low] > arr[high]) swap(arr, low, high);
            return;
        }
        if (arr[low] > arr[high - 1]) swap(arr, low, high - 1);
        if (arr[low] > arr[high]) swap(arr, low, high);
        if (arr[high - 1] > arr[high]) swap(arr, high - 1, high);
    }

    // 3-way Dutch National Flag partition for duplicate-heavy data
    public static void sort3Way(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort3Way(arr, 0, arr.length - 1);
    }

    private static void quickSort3Way(int[] arr, int low, int high) {
        if (low >= high) return;

        int pivot = arr[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (arr[i] > pivot) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        quickSort3Way(arr, low, lt - 1);
        quickSort3Way(arr, gt + 1, high);
    }

    public static void sortIterative(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        stack.push(arr.length - 1);

        while (!stack.isEmpty()) {
            int high = stack.pop();
            int low = stack.pop();

            if (low < high) {
                int pi = randomizedLomutoPartition(arr, low, high);

                if (pi - 1 > low) {
                    stack.push(low);
                    stack.push(pi - 1);
                }
                if (pi + 1 < high) {
                    stack.push(pi + 1);
                    stack.push(high);
                }
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        System.out.println("Unsorted array: " + Arrays.toString(arr));
        sortLomuto(arr);
        System.out.println("Lomuto sorted : " + Arrays.toString(arr));

        int[] hoareArr = {84, 12, 45, 96, 33, 18, 77, 2, 59};
        sortHoare(hoareArr);
        System.out.println("\nHoare sorted  : " + Arrays.toString(hoareArr));

        int[] sortedArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sortMedianOfThree(sortedArr);
        System.out.println("\nMedian-of-3   : " + Arrays.toString(sortedArr));

        int[] dupArr = {2, 0, 2, 1, 1, 0, 2, 1, 0, 2, 0, 1};
        sort3Way(dupArr);
        System.out.println("\n3-Way sorted  : " + Arrays.toString(dupArr));

        int[] iterArr = {55, 4, 92, 18, 7, 63, 21, 39, 100, 3};
        sortIterative(iterArr);
        System.out.println("\nIterative sort: " + Arrays.toString(iterArr));
    }
}
