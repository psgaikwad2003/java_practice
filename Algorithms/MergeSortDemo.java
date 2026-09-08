import java.util.Arrays;

/**
 * Demonstrates the Merge Sort algorithm using the Divide-and-Conquer paradigm.
 * 
 * Characteristics:
 * - Time Complexity: O(n log n) in all cases (Best, Average, Worst).
 * - Space Complexity: O(n) auxiliary space for temporary arrays.
 * - Stability: Stable (preserves relative order of equal elements).
 */
public class MergeSortDemo {

    /**
     * Public API to initiate merge sort on the entire array.
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    /**
     * Recursively divides the array into two halves until single elements remain.
     */
    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            // Find middle index using overflow-safe formula
            int mid = left + (right - left) / 2;

            // Sort first and second halves
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            // Merge the sorted halves
            merge(arr, left, mid, right);
        }
    }

    /**
     * Merges two sorted subarrays arr[left..mid] and arr[mid+1..right].
     */
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Temporary arrays
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Copy data to temporary arrays
        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        // Initial indices of first and second subarrays
        int i = 0, j = 0;
        // Initial index of merged subarray
        int k = left;

        while (i < n1 && j < n2) {
            // Using <= ensures stability for equal elements
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArr[] if any
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArr[] if any
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("           Merge Sort Algorithm Demonstration     ");
        System.out.println("==================================================");

        int[] sample = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Original array: " + Arrays.toString(sample));
        sort(sample);
        System.out.println("Sorted array:   " + Arrays.toString(sample));

        System.out.println("\n=== Test Cases ===");

        // Test 1: Array with duplicates and negatives
        int[] withDuplicates = {5, -2, 2, -8, 5, 0, 2, 9, -2};
        System.out.println("Duplicates & Negatives Before: " + Arrays.toString(withDuplicates));
        sort(withDuplicates);
        System.out.println("Duplicates & Negatives After:  " + Arrays.toString(withDuplicates));

        // Test 2: Already sorted array
        int[] alreadySorted = {1, 2, 3, 4, 5, 6};
        System.out.println("\nAlready Sorted Before:         " + Arrays.toString(alreadySorted));
        sort(alreadySorted);
        System.out.println("Already Sorted After:          " + Arrays.toString(alreadySorted));

        // Test 3: Reverse sorted array
        int[] reverseSorted = {90, 75, 50, 30, 10, -5};
        System.out.println("\nReverse Sorted Before:         " + Arrays.toString(reverseSorted));
        sort(reverseSorted);
        System.out.println("Reverse Sorted After:          " + Arrays.toString(reverseSorted));
    }
}
