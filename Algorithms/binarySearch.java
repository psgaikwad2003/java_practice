import java.util.Arrays;

public class binarySearch {

    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int binarySearchRecursive(int[] arr, int low, int high, int target) {
        if (arr == null || low > high) return -1;

        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;

        if (arr[mid] > target) {
            return binarySearchRecursive(arr, low, mid - 1, target);
        }
        return binarySearchRecursive(arr, mid + 1, high, target);
    }

    public static int findFirstOccurrence(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                result = mid;
                high = mid - 1;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static int findLastOccurrence(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                result = mid;
                low = mid + 1;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static int[] searchRange(int[] arr, int target) {
        int first = findFirstOccurrence(arr, target);
        if (first == -1) return new int[]{-1, -1};
        int last = findLastOccurrence(arr, target);
        return new int[]{first, last};
    }

    public static int countOccurrences(int[] arr, int target) {
        int[] range = searchRange(arr, target);
        if (range[0] == -1) return 0;
        return range[1] - range[0] + 1;
    }

    public static int searchInsertPosition(int[] arr, int target) {
        if (arr == null || arr.length == 0) return 0;
        int low = 0, high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    public static int[] findFloorAndCeil(int[] arr, int target) {
        if (arr == null || arr.length == 0) return new int[]{-1, -1};
        int low = 0, high = arr.length - 1;
        int floor = -1, ceil = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                return new int[]{arr[mid], arr[mid]};
            } else if (arr[mid] < target) {
                floor = arr[mid];
                low = mid + 1;
            } else {
                ceil = arr[mid];
                high = mid - 1;
            }
        }
        return new int[]{floor, ceil};
    }

    public static void main(String[] args) {
        int[] arr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int target = 23;

        System.out.println("Array : " + Arrays.toString(arr));
        System.out.println("Target: " + target);
        System.out.println("Iterative index : " + binarySearch(arr, target));
        System.out.println("Recursive index : " + binarySearchRecursive(arr, 0, arr.length - 1, target));
        System.out.println("Missing target  : " + binarySearch(arr, 50));

        int[] dupArr = {1, 2, 4, 4, 4, 4, 7, 9, 12};
        System.out.println("\nDuplicate array : " + Arrays.toString(dupArr));
        System.out.println("First occurrence of 4: " + findFirstOccurrence(dupArr, 4));
        System.out.println("Last occurrence of 4 : " + findLastOccurrence(dupArr, 4));
        System.out.println("Search range [first, last]: " + Arrays.toString(searchRange(dupArr, 4)));
        System.out.println("Count occurrences    : " + countOccurrences(dupArr, 4));

        int[] insertArr = {1, 3, 5, 6};
        System.out.println("\nInsert 5 in [1,3,5,6] -> index " + searchInsertPosition(insertArr, 5));
        System.out.println("Insert 2 in [1,3,5,6] -> index " + searchInsertPosition(insertArr, 2));

        int[] fcArr = {1, 2, 8, 10, 10, 12, 19};
        int[] fc = findFloorAndCeil(fcArr, 5);
        System.out.println("\nFloor and Ceil of 5 in " + Arrays.toString(fcArr) + " -> Floor: " + fc[0] + ", Ceil: " + fc[1]);
    }
}
