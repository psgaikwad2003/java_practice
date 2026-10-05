package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoPointersTechniqueDemo {

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

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {

            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(List.of(nums[i], nums[left], nums[right]));

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

    public static int removeDuplicatesAllowTwo(int[] nums) {
        if (nums == null) return 0;
        if (nums.length <= 2) return nums.length;

        int slow = 2;
        for (int fast = 2; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow - 2]) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        return slow;
    }

    public static void main(String[] args) {
        System.out.println("=== Two Pointers Technique Demonstrations ===\n");

        int[] sortedArr = {2, 7, 11, 15};
        int target = 9;
        System.out.println("1. Two Sum II (Sorted):");
        System.out.println("   Array: " + Arrays.toString(sortedArr) + ", Target: " + target);
        System.out.println("   Result (1-indexed): " + Arrays.toString(twoSumSorted(sortedArr, target)));

        int[] threeSumArr = {-1, 0, 1, 2, -1, -4};
        System.out.println("\n2. 3Sum (Unique triplets summing to 0):");
        System.out.println("   Array: " + Arrays.toString(threeSumArr));
        System.out.println("   Triplets: " + threeSum(threeSumArr));

        int[] containers = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("\n3. Container With Most Water:");
        System.out.println("   Heights: " + Arrays.toString(containers));
        System.out.println("   Max Water Area: " + maxWaterArea(containers));

        int[] elevation = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println("\n4. Trapping Rain Water:");
        System.out.println("   Elevation: " + Arrays.toString(elevation));
        System.out.println("   Trapped Water: " + trapRainWater(elevation) + " units");

        int[] dupArr = {1, 1, 1, 2, 2, 3};
        System.out.println("\n5. Remove Duplicates Allowing At Most Two Occurrences:");
        System.out.println("   Original: " + Arrays.toString(dupArr));
        int newLen = removeDuplicatesAllowTwo(dupArr);
        System.out.println("   New Length: " + newLen + ", Valid Elements: " + Arrays.toString(Arrays.copyOf(dupArr, newLen)));

        int[] colors = {2, 0, 2, 1, 1, 0};
        System.out.println("\n6. Sort Colors (Dutch National Flag 0, 1, 2):");
        System.out.println("   Before: " + Arrays.toString(colors));
        sortColors(colors);
        System.out.println("   After:  " + Arrays.toString(colors));
    }

    public static void sortColors(int[] nums) {
        if (nums == null || nums.length <= 1) return;
        int low = 0, mid = 0, high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                int temp = nums[low];
                nums[low] = nums[mid];
                nums[mid] = temp;
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                int temp = nums[mid];
                nums[mid] = nums[high];
                nums[high] = temp;
                high--;
            }
        }
    }
}
