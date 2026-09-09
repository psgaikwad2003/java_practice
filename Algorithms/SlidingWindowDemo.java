import java.util.*;

/**
 * Demonstrates the Sliding Window algorithmic technique.
 *
 * Scenarios covered:
 * 1. Fixed-Size Window: Maximum sum subarray of size K.
 * 2. Dynamic-Size Window: Longest substring without repeating characters.
 * 3. Dynamic-Size Window: Minimum size subarray with sum >= target.
 * 4. Window with Frequency Map: Find all anagrams of a pattern in a string.
 *
 * Complexity:
 * - Time: O(n) across all variants (each element visited at most twice).
 * - Space: O(1) for numeric arrays, O(k) for hash tables/frequency arrays.
 */
public class SlidingWindowDemo {

    /**
     * Problem 1: Fixed-size window.
     * Finds the maximum sum of any contiguous subarray of size k.
     */
    public static int maxSubarraySumOfSizeK(int[] arr, int k) {
        if (arr == null || arr.length < k || k <= 0) {
            throw new IllegalArgumentException("Invalid input array or window size k.");
        }

        int windowSum = 0;
        // Compute sum of first window
        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }

        int maxSum = windowSum;
        // Slide the window forward
        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k]; // Add incoming, remove outgoing
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }

    /**
     * Problem 2: Dynamic-size window.
     * Finds the length of the longest substring without repeating characters.
     */
    public static int lengthOfLongestSubstringWithoutRepeating(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        // Map character to its most recent index
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLength = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char current = s.charAt(right);

            // If seen and inside current window, shrink window from left
            if (lastSeen.containsKey(current)) {
                left = Math.max(left, lastSeen.get(current) + 1);
            }

            lastSeen.put(current, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    /**
     * Problem 3: Dynamic-size window.
     * Finds the minimal length of a contiguous subarray of which the sum >= target.
     * Returns 0 if no such subarray exists.
     */
    public static int minSubArrayLen(int target, int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;
        int currentSum = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            currentSum += nums[right];

            // Contract the window as long as currentSum >= target
            while (currentSum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                currentSum -= nums[left];
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    /**
     * Problem 4: Sliding window with character frequency.
     * Finds all starting indices of anagrams of pattern p in string s.
     */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }

        int[] pCount = new int[26];
        int[] sCount = new int[26];

        for (int i = 0; i < p.length(); i++) {
            pCount[p.charAt(i) - 'a']++;
            sCount[s.charAt(i) - 'a']++;
        }

        if (Arrays.equals(pCount, sCount)) {
            result.add(0);
        }

        int windowSize = p.length();
        for (int i = windowSize; i < s.length(); i++) {
            // Slide: add new character, remove oldest character
            sCount[s.charAt(i) - 'a']++;
            sCount[s.charAt(i - windowSize) - 'a']--;

            if (Arrays.equals(pCount, sCount)) {
                result.add(i - windowSize + 1);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  SLIDING WINDOW ALGORITHMIC PATTERNS     ");
        System.out.println("==========================================");

        // Demo 1: Fixed Size Window
        int[] nums1 = {2, 1, 5, 1, 3, 2};
        int k = 3;
        System.out.println("\n[1] Maximum Sum Subarray of Size K:");
        System.out.println("    Array: " + Arrays.toString(nums1) + ", k = " + k);
        System.out.println("    Max Sum: " + maxSubarraySumOfSizeK(nums1, k)); // Expected: 9 (5+1+3)

        // Demo 2: Longest Substring Without Repeating Characters
        String s = "abcabcbb";
        System.out.println("\n[2] Longest Substring Without Repeating Characters:");
        System.out.println("    Input: \"" + s + "\"");
        System.out.println("    Length: " + lengthOfLongestSubstringWithoutRepeating(s)); // Expected: 3 ("abc")

        String s2 = "pwwkew";
        System.out.println("    Input: \"" + s2 + "\"");
        System.out.println("    Length: " + lengthOfLongestSubstringWithoutRepeating(s2)); // Expected: 3 ("wke")

        // Demo 3: Min Subarray with Sum >= Target
        int target = 7;
        int[] nums3 = {2, 3, 1, 2, 4, 3};
        System.out.println("\n[3] Minimum Size Subarray Sum (>= " + target + "):");
        System.out.println("    Array: " + Arrays.toString(nums3));
        System.out.println("    Min Length: " + minSubArrayLen(target, nums3)); // Expected: 2 ([4, 3])

        // Demo 4: Find All Anagrams
        String text = "cbaebabacd";
        String pattern = "abc";
        System.out.println("\n[4] Find All Anagram Starting Indices:");
        System.out.println("    Text: \"" + text + "\", Pattern: \"" + pattern + "\"");
        System.out.println("    Indices: " + findAnagrams(text, pattern)); // Expected: [0, 6]
        System.out.println("\nAll Sliding Window demonstrations completed successfully.");
    }
}
