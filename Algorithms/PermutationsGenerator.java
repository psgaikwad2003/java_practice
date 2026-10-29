import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsGenerator {

    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(nums[i]);
            backtrack(nums, used, current, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        Arrays.sort(nums);
        boolean[] used = new boolean[nums.length];
        backtrackUnique(nums, used, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackUnique(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;

            used[i] = true;
            current.add(nums[i]);
            backtrackUnique(nums, used, current, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    public static void nextPermutation(int[] nums) {
        if (nums == null || nums.length <= 1) return;

        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }

        reverse(nums, i + 1, nums.length - 1);
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            swap(nums, start++, end--);
        }
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        System.out.println("Permutations of " + Arrays.toString(arr1) + ":");
        System.out.println(permute(arr1));

        int[] arr2 = {1, 1, 2};
        System.out.println("\nUnique Permutations of " + Arrays.toString(arr2) + ":");
        System.out.println(permuteUnique(arr2));

        int[] arr3 = {1, 2, 3};
        System.out.println("\nNext Permutations sequence starting from " + Arrays.toString(arr3) + ":");
        for (int step = 0; step < 6; step++) {
            nextPermutation(arr3);
            System.out.println("Step " + (step + 1) + ": " + Arrays.toString(arr3));
        }
    }
}
