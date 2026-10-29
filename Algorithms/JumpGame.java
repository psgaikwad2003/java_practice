import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JumpGame {

    public static boolean canJump(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        int maxReach = 0;

        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= nums.length - 1) return true;
        }

        return true;
    }

    public static int minJumps(int[] nums) {
        if (nums == null || nums.length <= 1) return 0;
        if (nums[0] == 0) return -1;

        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);

            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
                if (currentEnd >= nums.length - 1) break;
            }
        }

        return currentEnd >= nums.length - 1 ? jumps : -1;
    }

    public static List<Integer> getJumpPath(int[] nums) {
        List<Integer> path = new ArrayList<>();
        if (nums == null || nums.length == 0 || !canJump(nums)) return path;

        int n = nums.length;
        if (n == 1) {
            path.add(0);
            return path;
        }

        int currentEnd = 0;
        int farthest = 0;
        int nextHop = 0;
        path.add(0);

        for (int i = 0; i < n - 1; i++) {
            if (i + nums[i] > farthest) {
                farthest = i + nums[i];
                nextHop = i;
            }

            if (i == currentEnd) {
                if (farthest > currentEnd) {
                    if (path.get(path.size() - 1) != nextHop) {
                        path.add(nextHop);
                    }
                    currentEnd = farthest;
                    if (currentEnd >= n - 1) {
                        path.add(n - 1);
                        break;
                    }
                } else {
                    return new ArrayList<>();
                }
            }
        }

        return path;
    }

    public static void main(String[] args) {
        int[] arr1 = {2, 3, 1, 1, 4};
        System.out.println("Array: " + Arrays.toString(arr1));
        System.out.println("Can jump to end: " + canJump(arr1));
        System.out.println("Min jumps: " + minJumps(arr1));
        System.out.println("Jump path (indices): " + getJumpPath(arr1));

        int[] arr2 = {3, 2, 1, 0, 4};
        System.out.println("\nArray: " + Arrays.toString(arr2));
        System.out.println("Can jump to end: " + canJump(arr2));
        System.out.println("Min jumps: " + minJumps(arr2));

        int[] arr3 = {1, 3, 5, 8, 9, 2, 6, 7, 6, 8, 9};
        System.out.println("\nArray: " + Arrays.toString(arr3));
        System.out.println("Can jump to end: " + canJump(arr3));
        System.out.println("Min jumps: " + minJumps(arr3));
        System.out.println("Jump path (indices): " + getJumpPath(arr3));
    }
}
