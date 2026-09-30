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

    /**
     * Problem 5: Sliding Window Maximum (LeetCode #239).
     * Computes the maximum value in every sliding window of size k in O(n) time
     * using a Monotonic Decreasing Deque storing array indices.
     *
     * @param nums input array
     * @param k    window size
     * @return array of maximums for each window
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resIdx = 0;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // Remove elements outside of current window [i - k + 1, i]
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            // Maintain monotonic decreasing order: remove smaller elements from back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            // Record maximum for valid windows
            if (i >= k - 1) {
                result[resIdx++] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    /**
     * Problem 6: Max Consecutive Ones III (LeetCode #1004).
     * Given a binary array and integer k, return the maximum number of consecutive 1's
     * if you can flip at most k 0's.
     *
     * @param nums binary array
     * @param k    allowed number of flips
     * @return maximum window length
     */
    public static int longestOnes(int[] nums, int k) {
        if (nums == null || nums.length == 0) return 0;
        int left = 0;
        int zeroCount = 0;
        int maxLen = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeroCount++;
            }
            while (zeroCount > k) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    /**
     * Problem 7: Minimum Window Substring (LeetCode #76).
     * Finds the minimum window substring in s which contains all characters in t in O(n) time.
     *
     * @param s source string
     * @param t target template string
     * @return smallest substring of s containing all characters in t, or "" if none
     */
    public static String minWindowSubstring(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }

        int required = targetMap.size();
        int formed = 0;
        Map<Character, Integer> windowCounts = new HashMap<>();

        int[] ans = {-1, 0, 0}; // length, left, right
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowCounts.put(c, windowCounts.getOrDefault(c, 0) + 1);

            if (targetMap.containsKey(c) && windowCounts.get(c).intValue() == targetMap.get(c).intValue()) {
                formed++;
            }

            while (left <= right && formed == required) {
                c = s.charAt(left);
                if (ans[0] == -1 || (right - left + 1) < ans[0]) {
                    ans[0] = right - left + 1;
                    ans[1] = left;
                    ans[2] = right;
                }

                windowCounts.put(c, windowCounts.get(c) - 1);
                if (targetMap.containsKey(c) && windowCounts.get(c) < targetMap.get(c)) {
                    formed--;
                }
                left++;
            }
        }

        return ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1);
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

        // Demo 5: Sliding Window Maximum
        int[] maxArr = {1, 3, -1, -3, 5, 3, 6, 7};
        int winK = 3;
        System.out.println("\n[5] Sliding Window Maximum (k = " + winK + "):");
        System.out.println("    Array: " + Arrays.toString(maxArr));
        System.out.println("    Window Maxes: " + Arrays.toString(maxSlidingWindow(maxArr, winK)));

        // Demo 6: Max Consecutive Ones with K Flips
        int[] binArr = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
        int flips = 2;
        System.out.println("\n[6] Max Consecutive Ones after at most " + flips + " flips:");
        System.out.println("    Array: " + Arrays.toString(binArr));
        System.out.println("    Max Length: " + longestOnes(binArr, flips));

        // Demo 7: Minimum Window Substring
        String sourceStr = "ADOBECODEBANC";
        String targetPattern = "ABC";
        System.out.println("\n[7] Minimum Window Substring:");
        System.out.println("    Source: \"" + sourceStr + "\", Target: \"" + targetPattern + "\"");
        System.out.println("    Minimum Window: \"" + minWindowSubstring(sourceStr, targetPattern) + "\" (Expected: \"BANC\")");

        System.out.println("\nAll Sliding Window demonstrations completed successfully.");
    }
}
