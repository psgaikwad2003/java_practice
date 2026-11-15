import java.util.Arrays;
import java.util.Objects;

public class PalindromeArray {

    public static boolean isPalindrome(int[] arr) {
        if (arr == null) return false;
        int start = 0;
        int end = arr.length - 1;

        while (start < end) {
            if (arr[start] != arr[end]) return false;
            start++;
            end--;
        }
        return true;
    }

    public static boolean isPalindromeRecursive(int[] arr, int left, int right) {
        if (arr == null) return false;
        if (left >= right) return true;
        if (arr[left] != arr[right]) return false;
        return isPalindromeRecursive(arr, left + 1, right - 1);
    }

    public static <T> boolean isPalindromeGeneric(T[] arr) {
        if (arr == null) return false;
        int start = 0;
        int end = arr.length - 1;

        while (start < end) {
            if (!Objects.equals(arr[start], arr[end])) return false;
            start++;
            end--;
        }
        return true;
    }

    public static int[] longestPalindromicSubarray(int[] arr) {
        if (arr == null || arr.length == 0) return new int[0];
        int maxLen = 1;
        int bestStart = 0;

        for (int i = 0; i < arr.length; i++) {
            // Odd length expansion
            int l = i, r = i;
            while (l >= 0 && r < arr.length && arr[l] == arr[r]) {
                if (r - l + 1 > maxLen) {
                    maxLen = r - l + 1;
                    bestStart = l;
                }
                l--;
                r++;
            }

            // Even length expansion
            l = i; r = i + 1;
            while (l >= 0 && r < arr.length && arr[l] == arr[r]) {
                if (r - l + 1 > maxLen) {
                    maxLen = r - l + 1;
                    bestStart = l;
                }
                l--;
                r++;
            }
        }
        return Arrays.copyOfRange(arr, bestStart, bestStart + maxLen);
    }

    public static void main(String[] args) {
        System.out.println("=== Palindrome Array Verification ===");

        int[] p1 = {1, 2, 3, 2, 1};
        int[] p2 = {1, 2, 3, 4, 5};

        System.out.println("Array " + Arrays.toString(p1) + " is palindrome: " + isPalindrome(p1));
        System.out.println("Array " + Arrays.toString(p2) + " is palindrome: " + isPalindrome(p2));

        String[] words = {"kayak", "deified", "rotator", "deified", "kayak"};
        System.out.println("String array is palindrome: " + isPalindromeGeneric(words));

        int[] sample = {10, 4, 2, 3, 2, 4, 99};
        System.out.println("\nLongest Palindromic Subarray in " + Arrays.toString(sample) + ":");
        System.out.println("Result: " + Arrays.toString(longestPalindromicSubarray(sample)));
    }
}
