import java.util.Arrays;

public class MedianOfTwoSortedArrays {

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;
        int low = 0, high = m;

        while (low <= high) {
            int partitionX = low + (high - low) / 2;
            int partitionY = (m + n + 1) / 2 - partitionX;

            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = (partitionX == m) ? Integer.MAX_VALUE : nums1[partitionX];

            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == n) ? Integer.MAX_VALUE : nums2[partitionY];

            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                } else {
                    return Math.max(maxLeftX, maxLeftY);
                }
            } else if (maxLeftX > minRightY) {
                high = partitionX - 1;
            } else {
                low = partitionX + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted.");
    }

    public static double findMedianSortedArraysLinear(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int total = m + n;
        int targetIdx2 = total / 2;
        int targetIdx1 = targetIdx2 - 1;

        int i = 0, j = 0, count = 0;
        int v1 = 0, v2 = 0;

        while (count <= targetIdx2) {
            v1 = v2;
            if (i < m && (j >= n || nums1[i] <= nums2[j])) {
                v2 = nums1[i++];
            } else {
                v2 = nums2[j++];
            }
            count++;
        }

        return (total % 2 == 0) ? (v1 + v2) / 2.0 : v2;
    }

    public static void main(String[] args) {
        int[] a1 = {1, 3};
        int[] b1 = {2};
        System.out.println("Array 1: " + Arrays.toString(a1));
        System.out.println("Array 2: " + Arrays.toString(b1));
        System.out.println("Median (Binary Search O(log(min(m,n)))): " + findMedianSortedArrays(a1, b1));
        System.out.println("Median (Linear Check): " + findMedianSortedArraysLinear(a1, b1));

        int[] a2 = {1, 2};
        int[] b2 = {3, 4};
        System.out.println("\nArray 1: " + Arrays.toString(a2));
        System.out.println("Array 2: " + Arrays.toString(b2));
        System.out.println("Median (Binary Search): " + findMedianSortedArrays(a2, b2));
        System.out.println("Median (Linear Check): " + findMedianSortedArraysLinear(a2, b2));

        int[] a3 = {};
        int[] b3 = {2, 3};
        System.out.println("\nArray 1: " + Arrays.toString(a3));
        System.out.println("Array 2: " + Arrays.toString(b3));
        System.out.println("Median: " + findMedianSortedArrays(a3, b3));
    }
}
