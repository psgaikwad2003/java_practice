import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetsGenerator {

    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        Arrays.sort(nums);
        backtrackWithDup(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackWithDup(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;
            current.add(nums[i]);
            backtrackWithDup(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static List<List<Integer>> subsetsBitmask(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        int n = nums.length;
        int total = 1 << n;

        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            result.add(subset);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        System.out.println("Subsets of " + Arrays.toString(arr1) + " (Backtracking):");
        System.out.println(subsets(arr1));

        System.out.println("\nSubsets of " + Arrays.toString(arr1) + " (Bitmask):");
        System.out.println(subsetsBitmask(arr1));

        int[] arr2 = {1, 2, 2};
        System.out.println("\nSubsets with duplicates of " + Arrays.toString(arr2) + ":");
        System.out.println(subsetsWithDup(arr2));
    }
}
