package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the Two-Pointers algorithmic paradigm with four classic LeetCode problems:
 * 1. Two Sum II (Input Array Is Sorted): O(n) time, O(1) space.
 * 2. 3Sum (All unique triplets summing to zero): O(n^2) time, O(1) auxiliary space.
 * 3. Container With Most Water: O(n) time, O(1) space.
 * 4. Trapping Rain Water: O(n) time, O(1) space.
 */
public class TwoPointersTechniqueDemo {

    /**
     * 1. Two Sum in a sorted array (1-indexed result).
     *
     * @param numbers sorted array in non-decreasing order
     * @param target target sum
     * @return 1-based indices [index1, index2], or empty if no pair exists
     */
    public static int[] twoSumSorted(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2) return new int[0];
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[0];
    }

    /**
     * 2. 3Sum: Finds all unique triplets [a, b, c] such that a + b + c = 0.
     * Skips duplicates to ensure result contains only distinct triplets.
     *
     * @param nums array of integers
     * @return list of unique triplets
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate outer elements
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(List.of(nums[i], nums[left], nums[right]));
                    // Skip duplicates for left and right
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    /**
     * 3. Container With Most Water.
     * Calculates the maximum area of water a container can hold between two vertical lines.
     *
     * @param height heights of vertical lines
     * @return maximum water container area
     */
    public static int maxWaterArea(int[] height) {
        if (height == null || height.length < 2) return 0;
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            int width = right - left;
            int currentArea = Math.min(height[left], height[right]) * width;
            maxArea = Math.max(maxArea, currentArea);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    /**
     * 4. Trapping Rain Water using Two Pointers.
     * Time: O(n), Space: O(1) optimal.
     *
     * @param height elevation map
     * @return total units of trapped rain water
     */
    public static int trapRainWater(int[] height) {
        if (height == null || height.length < 3) return 0;
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int totalWater = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    totalWater += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    totalWater += rightMax - height[right];
                }
                right--;
            }
        }
        return totalWater;
    }

    public static void main(String[] args) {
        System.out.println("=== Two Pointers Technique Demonstrations ===\n");

        // 1. Two Sum Sorted
        int[] sortedArr = {2, 7, 11, 15};
        int target = 9;
        System.out.println("1. Two Sum II (Sorted):");
        System.out.println("   Array: " + Arrays.toString(sortedArr) + ", Target: " + target);
        System.out.println("   Result (1-indexed): " + Arrays.toString(twoSumSorted(sortedArr, target)));

        // 2. 3Sum
        int[] threeSumArr = {-1, 0, 1, 2, -1, -4};
        System.out.println("\n2. 3Sum (Unique triplets summing to 0):");
        System.out.println("   Array: " + Arrays.toString(threeSumArr));
        System.out.println("   Triplets: " + threeSum(threeSumArr));

        // 3. Container With Most Water
        int[] containers = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("\n3. Container With Most Water:");
        System.out.println("   Heights: " + Arrays.toString(containers));
        System.out.println("   Max Water Area: " + maxWaterArea(containers));

        // 4. Trapping Rain Water
        int[] elevation = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println("\n4. Trapping Rain Water:");
        System.out.println("   Elevation: " + Arrays.toString(elevation));
        System.out.println("   Trapped Water: " + trapRainWater(elevation) + " units");
    }
}
