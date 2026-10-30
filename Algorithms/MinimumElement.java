import java.util.Arrays;

public class MinimumElement {

    public static int findMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int findMinIndex(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }
        int minIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static int[] findMinAndMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }

        int min, max;
        int i;
        int n = arr.length;

        if (n % 2 == 0) {
            if (arr[0] < arr[1]) {
                min = arr[0];
                max = arr[1];
            } else {
                min = arr[1];
                max = arr[0];
            }
            i = 2;
        } else {
            min = arr[0];
            max = arr[0];
            i = 1;
        }

        while (i < n - 1) {
            if (arr[i] < arr[i + 1]) {
                if (arr[i] < min) min = arr[i];
                if (arr[i + 1] > max) max = arr[i + 1];
            } else {
                if (arr[i + 1] < min) min = arr[i + 1];
                if (arr[i] > max) max = arr[i];
            }
            i += 2;
        }

        return new int[]{min, max};
    }

    public static int findMinRecursive(int[] arr, int left, int right) {
        if (left == right) return arr[left];
        int mid = left + (right - left) / 2;
        int leftMin = findMinRecursive(arr, left, mid);
        int rightMin = findMinRecursive(arr, mid + 1, right);
        return Math.min(leftMin, rightMin);
    }

    public static int findKthSmallest(int[] arr, int k) {
        if (arr == null || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Invalid value of k: " + k);
        }
        int[] copy = arr.clone();
        return quickSelect(copy, 0, copy.length - 1, k - 1);
    }

    private static int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) return nums[left];

        int pivotIndex = partition(nums, left, right);
        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        return i;
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {25, 10, 45, 60, 15, 90, 35};
        System.out.println("Array: " + Arrays.toString(arr));

        int min = findMin(arr);
        int minIdx = findMinIndex(arr);
        System.out.println("Minimum element: " + min + " at index: " + minIdx);

        int[] minMax = findMinAndMax(arr);
        System.out.println("Min and Max (Tournament method): [Min: " + minMax[0] + ", Max: " + minMax[1] + "]");

        int recMin = findMinRecursive(arr, 0, arr.length - 1);
        System.out.println("Minimum element (Divide and Conquer): " + recMin);

        for (int k = 1; k <= 3; k++) {
            System.out.println(k + "-th smallest element: " + findKthSmallest(arr, k));
        }

        int[] edgeArray = {-50, 12, -75, 0, 100, -100};
        System.out.println("\nArray with negative numbers: " + Arrays.toString(edgeArray));
        int[] edgeMinMax = findMinAndMax(edgeArray);
        System.out.println("Min: " + edgeMinMax[0] + ", Max: " + edgeMinMax[1]);
    }
}
