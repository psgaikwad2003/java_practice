package Algorithms;

import java.util.Arrays;

/**
 * Demonstrates Kadane's Algorithm and its variations for solving maximum subarray problems in linear time.
 * 
 * Features:
 * 1. Standard Kadane's Algorithm for Maximum Subarray Sum: O(n) time, O(1) space.
 * 2. Kadane's with Subarray Boundaries: Identifies the exact start and end indices.
 * 3. Maximum Circular Subarray Sum: Handles wrapped subarrays.
 * 4. Minimum Subarray Sum: Companion dual for minimum ranges.
 */
public class KadaneAlgorithmDemo {

    /**
     * Result container representing maximum subarray sum and its range.
     */
    public record SubarrayResult(int maxSum, int startIndex, int endIndex, int[] subarray) {
        @Override
        public String toString() {
            return String.format("Max Sum: %d, Indices: [%d..%d], Subarray: %s",
                    maxSum, startIndex, endIndex, Arrays.toString(subarray));
        }
    }

    /**
     * Classic Kadane's algorithm to find maximum contiguous subarray sum.
     * Works correctly even if all numbers in the array are negative.
     *
     * @param nums array of integers
     * @return maximum subarray sum
     */
    public static int maxSubArraySum(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int maxSoFar = nums[0];
        int currentMax = nums[0];

        for (int i = 1; i < nums.length; i++) {
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            maxSoFar = Math.max(maxSoFar, currentMax);
        }
        return maxSoFar;
    }

    /**
     * Finds the maximum contiguous subarray sum and captures the exact start and end indices.
     *
     * @param nums array of integers
     * @return SubarrayResult containing max sum, start index, end index, and copied subarray
     */
    public static SubarrayResult findMaxSubarray(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int maxSoFar = nums[0];
        int currentMax = nums[0];
        int start = 0;
        int end = 0;
        int tempStart = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > currentMax + nums[i]) {
                currentMax = nums[i];
                tempStart = i;
            } else {
                currentMax += nums[i];
            }

            if (currentMax > maxSoFar) {
                maxSoFar = currentMax;
                start = tempStart;
                end = i;
            }
        }

        int[] sub = Arrays.copyOfRange(nums, start, end + 1);
        return new SubarrayResult(maxSoFar, start, end, sub);
    }

    /**
     * Calculates the maximum subarray sum in a circular array (elements wrap around).
     *
     * @param nums array of integers
     * @return maximum circular subarray sum
     */
    public static int maxCircularSubarraySum(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int totalSum = 0;
        int maxKadane = nums[0];
        int currentMax = 0;
        int minKadane = nums[0];
        int currentMin = 0;

        for (int num : nums) {
            totalSum += num;

            // Standard Kadane for maximum
            currentMax = Math.max(num, currentMax + num);
            maxKadane = Math.max(maxKadane, currentMax);

            // Kadane inverted for minimum
            currentMin = Math.min(num, currentMin + num);
            minKadane = Math.min(minKadane, currentMin);
        }

        // If all elements are negative, maxKadane is the answer (totalSum - minKadane would be 0 for empty array)
        if (maxKadane < 0) {
            return maxKadane;
        }

        return Math.max(maxKadane, totalSum - minKadane);
    }

    public static void main(String[] args) {
        System.out.println("=== Kadane's Algorithm Demonstration ===\n");

        int[][] testArrays = {
            {-2, 1, -3, 4, -1, 2, 1, -5, 4},
            {1},
            {5, 4, -1, 7, 8},
            {-8, -3, -6, -2, -5, -4},
            {2, -1, 2, 3, -9}
        };

        for (int[] arr : testArrays) {
            System.out.println("Input: " + Arrays.toString(arr));
            int simpleSum = maxSubArraySum(arr);
            SubarrayResult detailed = findMaxSubarray(arr);
            int circularSum = maxCircularSubarraySum(arr);

            System.out.println("  Max Sum: " + simpleSum);
            System.out.println("  Detailed: " + detailed);
            System.out.println("  Max Circular Sum: " + circularSum);
            System.out.println();
        }

        // Specific circular wrap-around demonstration
        int[] circularTest = {5, -3, 5};
        System.out.println("Circular Showcase: " + Arrays.toString(circularTest));
        System.out.println("  Linear Max: " + maxSubArraySum(circularTest));
        System.out.println("  Circular Max: " + maxCircularSubarraySum(circularTest) + " (wraps 5 + 5 = 10)");
    }
}
