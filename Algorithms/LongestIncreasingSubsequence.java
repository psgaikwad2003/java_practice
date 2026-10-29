import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LongestIncreasingSubsequence {

    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] tails = new int[nums.length];
        int size = 0;

        for (int x : nums) {
            int i = 0, j = size;
            while (i < j) {
                int mid = i + (j - i) / 2;
                if (tails[mid] < x) {
                    i = mid + 1;
                } else {
                    j = mid;
                }
            }
            tails[i] = x;
            if (i == size) size++;
        }

        return size;
    }

    public static List<Integer> findLIS(int[] nums) {
        if (nums == null || nums.length == 0) return Collections.emptyList();

        int n = nums.length;
        int[] tails = new int[n];
        int[] tailIndices = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int size = 0;

        for (int i = 0; i < n; i++) {
            int x = nums[i];
            int low = 0, high = size;
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < x) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = x;
            tailIndices[low] = i;
            if (low > 0) {
                parent[i] = tailIndices[low - 1];
            }
            if (low == size) size++;
        }

        List<Integer> result = new ArrayList<>();
        int curr = tailIndices[size - 1];
        while (curr != -1) {
            result.add(nums[curr]);
            curr = parent[curr];
        }
        Collections.reverse(result);
        return result;
    }

    public static int lengthOfLIS_DP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxLen = 1;

        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        int[] arr1 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("Array: " + Arrays.toString(arr1));
        System.out.println("LIS length (Binary Search): " + lengthOfLIS(arr1));
        System.out.println("LIS length (DP): " + lengthOfLIS_DP(arr1));
        System.out.println("LIS sequence: " + findLIS(arr1));

        int[] arr2 = {0, 1, 0, 3, 2, 3};
        System.out.println("\nArray: " + Arrays.toString(arr2));
        System.out.println("LIS length: " + lengthOfLIS(arr2));
        System.out.println("LIS sequence: " + findLIS(arr2));

        int[] arr3 = {7, 7, 7, 7};
        System.out.println("\nArray: " + Arrays.toString(arr3));
        System.out.println("LIS length: " + lengthOfLIS(arr3));
        System.out.println("LIS sequence: " + findLIS(arr3));
    }
}
