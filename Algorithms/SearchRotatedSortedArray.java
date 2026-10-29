import java.util.Arrays;

public class SearchRotatedSortedArray {

    public static int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) return mid;

            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }

    public static boolean searchWithDuplicates(int[] nums, int target) {
        if (nums == null || nums.length == 0) return false;
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) return true;

            if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
                low++;
                high--;
            } else if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return false;
    }

    public static int findMin(int[] nums) {
        if (nums == null || nums.length == 0) throw new IllegalArgumentException("Array is empty");
        int low = 0, high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] > nums[high]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return nums[low];
    }

    public static void main(String[] args) {
        int[] distinct = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("Array: " + Arrays.toString(distinct));
        System.out.println("Search 0: index " + search(distinct, 0));
        System.out.println("Search 3: index " + search(distinct, 3));
        System.out.println("Minimum element: " + findMin(distinct));

        int[] duplicates = {2, 5, 6, 0, 0, 1, 2};
        System.out.println("\nArray with duplicates: " + Arrays.toString(duplicates));
        System.out.println("Contains 0: " + searchWithDuplicates(duplicates, 0));
        System.out.println("Contains 3: " + searchWithDuplicates(duplicates, 3));

        int[] shifted = {11, 13, 15, 17};
        System.out.println("\nArray: " + Arrays.toString(shifted));
        System.out.println("Minimum element: " + findMin(shifted));
    }
}
