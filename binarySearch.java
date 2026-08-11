// Binary Search - extends linearSearch concept
public class binarySearch {
    public static void main(String[] args) {

        // Array must be sorted for Binary Search
        int[] arr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int target = 23;

        System.out.println("========== Binary Search ==========");
        System.out.print("Array: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println("\nSearching for: " + target);

        int result = binarySearch(arr, target);

        if (result != -1) {
            System.out.println("Element found at index: " + result);
        } else {
            System.out.println("Element not found.");
        }

        // Also search for a missing element
        int missing = 50;
        int result2 = binarySearch(arr, missing);
        System.out.println("\nSearching for: " + missing);
        if (result2 != -1) {
            System.out.println("Element found at index: " + result2);
        } else {
            System.out.println("Element " + missing + " not found.");
        }

        System.out.println("===================================");
    }

    static int binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1; // Not found
    }
}
