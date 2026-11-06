import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

/**
 * QuickSort Algorithm Suite
 *
 * Implements modern partitioning and optimization schemes:
 * 1. Randomized Lomuto Partition - Guards against worst-case O(n^2) on sorted inputs
 * 2. Hoare's Partition Scheme - Bidirectional scanning with ~3x fewer swaps
 * 3. Median-of-Three Partitioning - Robust pivot selection (low, mid, high)
 * 4. 3-Way Partitioning (Dutch National Flag) - O(n) linear performance on duplicate-heavy inputs
 * 5. Iterative QuickSort - Stack-based implementation preventing recursion stack overflow
 *
 * Complexity:
 * - Time: Best O(n log n), Average O(n log n), Worst O(n^2) (mitigated by randomized/median-of-three)
 * - 3-Way Duplicate Time: O(n)
 * - Space: O(log n) call stack (in-place)
 */
public class QuickSortDemo {

    private static final Random RANDOM = new Random();

    // -------------------------------------------------------------
    // 1. Randomized Lomuto Partition QuickSort
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // 2. Hoare's Partition QuickSort (Fewer Swaps than Lomuto)
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // 3. Median-of-Three Partition QuickSort
    // Selects median among arr[low], arr[mid], arr[high]
    // -------------------------------------------------------------
    public static void sortMedianOfThree(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSortMedian(arr, 0, arr.length - 1);
    }

    private static void quickSortMedian(int[] arr, int low, int high) {
        if (low >= high) return;

        // Base case for small subarrays
        if (high - low <= 2) {
            manualSortSmall(arr, low, high);
            return;
        }

        int mid = low + (high - low) / 2;

        // Sort low, mid, high to find median
        if (arr[low] > arr[mid]) swap(arr, low, mid);
        if (arr[low] > arr[high]) swap(arr, low, high);
        if (arr[mid] > arr[high]) swap(arr, mid, high);

        // Place median at high - 1 as sentinel pivot
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

        swap(arr, i, high - 1); // Restore pivot
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
        // size == 3
        if (arr[low] > arr[high - 1]) swap(arr, low, high - 1);
        if (arr[low] > arr[high]) swap(arr, low, high);
        if (arr[high - 1] > arr[high]) swap(arr, high - 1, high);
    }

    // -------------------------------------------------------------
    // 4. 3-Way Partitioning (Dutch National Flag Algorithm)
    // Optimal O(n) for arrays with large numbers of duplicate keys
    // -------------------------------------------------------------
    public static void sort3Way(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort3Way(arr, 0, arr.length - 1);
    }

    private static void quickSort3Way(int[] arr, int low, int high) {
        if (low >= high) return;

        int pivot = arr[low];
        int lt = low;      // arr[low..lt-1] < pivot
        int gt = high;     // arr[gt+1..high] > pivot
        int i = low + 1;   // arr[lt..i-1] == pivot

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

        // arr[lt..gt] are all equal to pivot
        quickSort3Way(arr, low, lt - 1);
        quickSort3Way(arr, gt + 1, high);
    }

    // -------------------------------------------------------------
    // 5. Iterative QuickSort (Stack-based, No Recursion)
    // -------------------------------------------------------------
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

                // Push sub-arrays to stack
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
        System.out.println("=============================================================");
        System.out.println("                 QUICKSORT ALGORITHM SUITE                   ");
        System.out.println("=============================================================");

        // Demo 1: Original Test Case
        int[] arr = {10, 7, 8, 9, 1, 5};
        System.out.println("\nDemo 1: Randomized Lomuto QuickSort (Original Array)");
        System.out.println("Unsorted array: " + Arrays.toString(arr));
        sortLomuto(arr);
        System.out.println("Sorted array  : " + Arrays.toString(arr));

        // Demo 2: Hoare's Partitioning
        int[] hoareArr = {84, 12, 45, 96, 33, 18, 77, 2, 59};
        System.out.println("\nDemo 2: Hoare's Partition Scheme (Bidirectional Scanning)");
        System.out.println("Unsorted array: " + Arrays.toString(hoareArr));
        sortHoare(hoareArr);
        System.out.println("Sorted array  : " + Arrays.toString(hoareArr));

        // Demo 3: Median-of-Three Partitioning on Pre-sorted Array
        int[] sortedArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("\nDemo 3: Median-of-Three Partitioning (Resistant to Pre-Sorted Worst Case)");
        System.out.println("Input array   : " + Arrays.toString(sortedArr));
        sortMedianOfThree(sortedArr);
        System.out.println("Sorted array  : " + Arrays.toString(sortedArr));

        // Demo 4: 3-Way Partitioning on Duplicate-Heavy Array (Dutch National Flag)
        int[] dupArr = {2, 0, 2, 1, 1, 0, 2, 1, 0, 2, 0, 1};
        System.out.println("\nDemo 4: 3-Way QuickSort (Dutch National Flag on Duplicate Elements)");
        System.out.println("Unsorted array: " + Arrays.toString(dupArr));
        sort3Way(dupArr);
        System.out.println("3-Way sorted  : " + Arrays.toString(dupArr) + " (O(n) linear performance)");

        // Demo 5: Iterative QuickSort
        int[] iterArr = {55, 4, 92, 18, 7, 63, 21, 39, 100, 3};
        System.out.println("\nDemo 5: Iterative QuickSort (Explicit Stack, Stack-Overflow Safe)");
        System.out.println("Unsorted array: " + Arrays.toString(iterArr));
        sortIterative(iterArr);
        System.out.println("Sorted array  : " + Arrays.toString(iterArr));

        System.out.println("\n=============================================================");
    }
}
