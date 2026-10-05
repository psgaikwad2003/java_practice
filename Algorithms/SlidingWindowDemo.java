import java.util.*;

public class SlidingWindowDemo {

    public static int maxSubarraySumOfSizeK(int[] arr, int k) {
        if (arr == null || arr.length < k || k <= 0) {
            throw new IllegalArgumentException("Invalid input array or window size k.");
        }

        int windowSum = 0;

        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }

        int maxSum = windowSum;

        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }

    public static int lengthOfLongestSubstringWithoutRepeating(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLength = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char current = s.charAt(right);

            if (lastSeen.containsKey(current)) {
                left = Math.max(left, lastSeen.get(current) + 1);
            }

            lastSeen.put(current, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    public static int minSubArrayLen(int target, int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;
        int currentSum = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            currentSum += nums[right];

            while (currentSum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                currentSum -= nums[left];
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

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

            sCount[s.charAt(i) - 'a']++;
            sCount[s.charAt(i - windowSize) - 'a']--;

            if (Arrays.equals(pCount, sCount)) {
                result.add(i - windowSize + 1);
            }
        }

        return result;
    }

    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resIdx = 0;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                result[resIdx++] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

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

    public static String minWindowSubstring(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }

        int required = targetMap.size();
        int formed = 0;
        Map<Character, Integer> windowCounts = new HashMap<>();

        int[] ans = {-1, 0, 0};
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

        int[] nums1 = {2, 1, 5, 1, 3, 2};
        int k = 3;
        System.out.println("\n[1] Maximum Sum Subarray of Size K:");
        System.out.println("    Array: " + Arrays.toString(nums1) + ", k = " + k);
        System.out.println("    Max Sum: " + maxSubarraySumOfSizeK(nums1, k));

        String s = "abcabcbb";
        System.out.println("\n[2] Longest Substring Without Repeating Characters:");
        System.out.println("    Input: \"" + s + "\"");
        System.out.println("    Length: " + lengthOfLongestSubstringWithoutRepeating(s));

        String s2 = "pwwkew";
        System.out.println("    Input: \"" + s2 + "\"");
        System.out.println("    Length: " + lengthOfLongestSubstringWithoutRepeating(s2));

        int target = 7;
        int[] nums3 = {2, 3, 1, 2, 4, 3};
        System.out.println("\n[3] Minimum Size Subarray Sum (>= " + target + "):");
        System.out.println("    Array: " + Arrays.toString(nums3));
        System.out.println("    Min Length: " + minSubArrayLen(target, nums3));

        String text = "cbaebabacd";
        String pattern = "abc";
        System.out.println("\n[4] Find All Anagram Starting Indices:");
        System.out.println("    Text: \"" + text + "\", Pattern: \"" + pattern + "\"");
        System.out.println("    Indices: " + findAnagrams(text, pattern));

        int[] maxArr = {1, 3, -1, -3, 5, 3, 6, 7};
        int winK = 3;
        System.out.println("\n[5] Sliding Window Maximum (k = " + winK + "):");
        System.out.println("    Array: " + Arrays.toString(maxArr));
        System.out.println("    Window Maxes: " + Arrays.toString(maxSlidingWindow(maxArr, winK)));

        int[] binArr = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
        int flips = 2;
        System.out.println("\n[6] Max Consecutive Ones after at most " + flips + " flips:");
        System.out.println("    Array: " + Arrays.toString(binArr));
        System.out.println("    Max Length: " + longestOnes(binArr, flips));

        String sourceStr = "ADOBECODEBANC";
        String targetPattern = "ABC";
        System.out.println("\n[7] Minimum Window Substring:");
        System.out.println("    Source: \"" + sourceStr + "\", Target: \"" + targetPattern + "\"");
        System.out.println("    Minimum Window: \"" + minWindowSubstring(sourceStr, targetPattern) + "\" (Expected: \"BANC\")");

        System.out.println("\nAll Sliding Window demonstrations completed successfully.");
    }
}
