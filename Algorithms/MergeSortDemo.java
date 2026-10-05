import java.util.Arrays;

public class MergeSortDemo {

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {

            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0;

        int k = left;

        while (i < n1 && j < n2) {

            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

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

        int[] withDuplicates = {5, -2, 2, -8, 5, 0, 2, 9, -2};
        System.out.println("Duplicates & Negatives Before: " + Arrays.toString(withDuplicates));
        sort(withDuplicates);
        System.out.println("Duplicates & Negatives After:  " + Arrays.toString(withDuplicates));

        int[] alreadySorted = {1, 2, 3, 4, 5, 6};
        System.out.println("\nAlready Sorted Before:         " + Arrays.toString(alreadySorted));
        sort(alreadySorted);
        System.out.println("Already Sorted After:          " + Arrays.toString(alreadySorted));

        int[] reverseSorted = {90, 75, 50, 30, 10, -5};
        System.out.println("\nReverse Sorted Before:         " + Arrays.toString(reverseSorted));
        sort(reverseSorted);
        System.out.println("Reverse Sorted After:          " + Arrays.toString(reverseSorted));
    }
}
