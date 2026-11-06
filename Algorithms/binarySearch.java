import java.util.Arrays;

/**
 * Binary Search Algorithm Suite
 *
 * Implements standard logarithmic search and essential boundary variations:
 * 1. Standard Iterative & Recursive Binary Search - O(log n)
 * 2. Lower Bound / First Occurrence - First index in array with duplicates
 * 3. Upper Bound / Last Occurrence - Last index in array with duplicates
 * 4. Search Range [First, Last] - Count occurrences in O(log n)
 * 5. Search Insert Position - Index where element should be inserted
 * 6. Floor and Ceil Value Finder
 *
 * Complexity:
 * - Time: O(log n)
 * - Space: O(1) auxiliary (iterative) / O(log n) call stack (recursive)
 */
public class binarySearch {

    // -------------------------------------------------------------
    // 1. Standard Iterative Binary Search
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // 2. Standard Recursive Binary Search
    // -------------------------------------------------------------
    public static int binarySearchRecursive(int[] arr, int low, int high, int target) {
        if (arr == null || low > high) return -1;

        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;

        if (arr[mid] > target) {
            return binarySearchRecursive(arr, low, mid - 1, target);
        }
        return binarySearchRecursive(arr, mid + 1, high, target);
    }

    // -------------------------------------------------------------
    // 3. First Occurrence (Lower Bound for exact match)
    // -------------------------------------------------------------
    public static int findFirstOccurrence(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                result = mid;
                high = mid - 1; // Continue searching on the left side
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    // -------------------------------------------------------------
    // 4. Last Occurrence (Upper Bound for exact match)
    // -------------------------------------------------------------
    public static int findLastOccurrence(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                result = mid;
                low = mid + 1; // Continue searching on the right side
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    // -------------------------------------------------------------
    // 5. Search Range [First, Last] and Count Occurrences
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // 6. Search Insert Position (Index where target would fit)
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // 7. Find Floor and Ceil
    // Floor = largest element <= target; Ceil = smallest element >= target
    // -------------------------------------------------------------
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
        System.out.println("=============================================================");
        System.out.println("                 BINARY SEARCH SUITE                         ");
        System.out.println("=============================================================");

        // Demo 1: Original Test Cases
        int[] arr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int target = 23;

        System.out.println("1. Standard Iterative & Recursive Binary Search:");
        System.out.println("  Array: " + Arrays.toString(arr));
        System.out.println("  Target: " + target);
        int resIter = binarySearch(arr, target);
        int resRec = binarySearchRecursive(arr, 0, arr.length - 1, target);
        System.out.println("  Iterative Search Index : " + resIter);
        System.out.println("  Recursive Search Index : " + resRec);

        int missing = 50;
        System.out.println("  Searching missing (50) : " + binarySearch(arr, missing));

        // Demo 2: Duplicates & Range Search [First, Last]
        int[] dupArr = {1, 2, 4, 4, 4, 4, 7, 9, 12};
        int dupTarget = 4;
        System.out.println("\n2. Duplicates and Range Search:");
        System.out.println("  Array : " + Arrays.toString(dupArr));
        System.out.println("  Target: " + dupTarget);
        int first = findFirstOccurrence(dupArr, dupTarget);
        int last = findLastOccurrence(dupArr, dupTarget);
        int[] range = searchRange(dupArr, dupTarget);
        int count = countOccurrences(dupArr, dupTarget);
        System.out.println("  First Occurrence Index : " + first);
        System.out.println("  Last Occurrence Index  : " + last);
        System.out.println("  Search Range [first,last]: " + Arrays.toString(range));
        System.out.println("  Total Count of target  : " + count + " elements");

        // Demo 3: Search Insert Position
        System.out.println("\n3. Search Insert Position:");
        int[] insertArr = {1, 3, 5, 6};
        System.out.println("  Array: " + Arrays.toString(insertArr));
        System.out.println("  Insert 5 -> Index " + searchInsertPosition(insertArr, 5) + " (Found)");
        System.out.println("  Insert 2 -> Index " + searchInsertPosition(insertArr, 2) + " (Between 1 and 3)");
        System.out.println("  Insert 7 -> Index " + searchInsertPosition(insertArr, 7) + " (End of array)");
        System.out.println("  Insert 0 -> Index " + searchInsertPosition(insertArr, 0) + " (Start of array)");

        // Demo 4: Floor and Ceil
        int[] fcArr = {1, 2, 8, 10, 10, 12, 19};
        System.out.println("\n4. Floor and Ceil Determination:");
        System.out.println("  Array: " + Arrays.toString(fcArr));
        int[] fc1 = findFloorAndCeil(fcArr, 5);
        int[] fc2 = findFloorAndCeil(fcArr, 10);
        System.out.println("  Target 5  -> Floor: " + fc1[0] + ", Ceil: " + fc1[1]);
        System.out.println("  Target 10 -> Floor: " + fc2[0] + ", Ceil: " + fc2[1]);

        System.out.println("\n=============================================================");
    }
}
