package Algorithms;

import java.util.Arrays;

public class KadaneAlgorithmDemo {

    public record SubarrayResult(int maxSum, int startIndex, int endIndex, int[] subarray) {
        @Override
        public String toString() {
            return String.format("Max Sum: %d, Indices: [%d..%d], Subarray: %s",
                    maxSum, startIndex, endIndex, Arrays.toString(subarray));
        }
    }

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

            currentMax = Math.max(num, currentMax + num);
            maxKadane = Math.max(maxKadane, currentMax);

            currentMin = Math.min(num, currentMin + num);
            minKadane = Math.min(minKadane, currentMin);
        }

        if (maxKadane < 0) {
            return maxKadane;
        }

        return Math.max(maxKadane, totalSum - minKadane);
    }

    public static int maxProductSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int maxProd = nums[0];
        int minProd = nums[0];
        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int x = nums[i];
            if (x < 0) {
                int temp = maxProd;
                maxProd = minProd;
                minProd = temp;
            }

            maxProd = Math.max(x, maxProd * x);
            minProd = Math.min(x, minProd * x);

            result = Math.max(result, maxProd);
        }

        return result;
    }

    public static int maxSubarrayWithOneDeletion(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int n = nums.length;
        if (n == 1) return nums[0];

        int noDelete = nums[0];
        int oneDelete = 0;
        int overallMax = nums[0];

        for (int i = 1; i < n; i++) {
            oneDelete = Math.max(oneDelete + nums[i], noDelete);
            noDelete = Math.max(nums[i], noDelete + nums[i]);
            overallMax = Math.max(overallMax, Math.max(noDelete, oneDelete));
        }

        return overallMax;
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

        int[] circularTest = {5, -3, 5};
        System.out.println("Circular Showcase: " + Arrays.toString(circularTest));
        System.out.println("  Linear Max: " + maxSubArraySum(circularTest));
        System.out.println("  Circular Max: " + maxCircularSubarraySum(circularTest) + " (wraps 5 + 5 = 10)");

        int[] prodArray = {2, 3, -2, 4, -1};
        System.out.println("\nProduct Subarray Showcase: " + Arrays.toString(prodArray));
        System.out.println("  Max Product: " + maxProductSubArray(prodArray));

        int[] deleteArray = {1, -2, 0, 3};
        System.out.println("\nOne Deletion Subarray Showcase: " + Arrays.toString(deleteArray));
        System.out.println("  Max Sum (with at most 1 deletion): " + maxSubarrayWithOneDeletion(deleteArray) + " (deleted -2 to get 1+0+3=4)");
    }
}
