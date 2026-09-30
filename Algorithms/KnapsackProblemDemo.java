import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates 0/1 Knapsack and Fractional Knapsack dynamic programming & greedy solutions.
 *
 * Enhancements:
 * 1. 2D DP Table Knapsack with item selection backtracking.
 * 2. 1D Array Space-Optimized Knapsack: O(W) auxiliary space.
 * 3. Fractional Knapsack using Greedy approach (value-to-weight ratio).
 */
public class KnapsackProblemDemo {

    /**
     * Item representation with name, weight, and profit.
     */
    public record Item(String name, int weight, double profit) {
        public double ratio() {
            return profit / weight;
        }
    }

    /**
     * Result of 0/1 Knapsack containing max profit and selected items.
     */
    public record KnapsackResult(int maxProfit, List<String> selectedItems) {
        @Override
        public String toString() {
            return String.format("Profit: %d, Items: %s", maxProfit, selectedItems);
        }
    }

    /**
     * 2D DP table approach that computes max profit and reconstructs selected items.
     */
    public static KnapsackResult knapSackWithItems(int capacity, int[] weights, int[] profits, String[] itemNames) {
        int n = profits.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(profits[i - 1] + dp[i - 1][w - weights[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Backtrack to find chosen items
        List<String> items = new ArrayList<>();
        int res = dp[n][capacity];
        int w = capacity;
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i - 1][w]) {
                items.add(itemNames[i - 1]);
                res -= profits[i - 1];
                w -= weights[i - 1];
            }
        }
        Collections.reverse(items);

        return new KnapsackResult(dp[n][capacity], items);
    }

    /**
     * Space-optimized 0/1 Knapsack using a 1D array traversed backwards.
     * Time Complexity: O(n * W), Space Complexity: O(W).
     */
    public static int knapSackSpaceOptimized(int capacity, int[] weights, int[] profits) {
        int[] dp = new int[capacity + 1];

        for (int i = 0; i < profits.length; i++) {
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], profits[i] + dp[w - weights[i]]);
            }
        }

        return dp[capacity];
    }

    /**
     * Fractional Knapsack solved via Greedy strategy (can take fractions of items).
     * Time Complexity: O(n log n) due to sorting by value-to-weight ratio.
     */
    public static double fractionalKnapsack(int capacity, List<Item> items) {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort((a, b) -> Double.compare(b.ratio(), a.ratio()));

        double currentWeight = 0.0;
        double finalValue = 0.0;

        for (Item item : sorted) {
            if (currentWeight + item.weight() <= capacity) {
                currentWeight += item.weight();
                finalValue += item.profit();
            } else {
                double remaining = capacity - currentWeight;
                finalValue += item.profit() * (remaining / item.weight());
                break;
            }
        }

        return finalValue;
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   KNAPSACK ALGORITHMS & OPTIMIZATIONS    ");
        System.out.println("==========================================");

        int[] profits = {60, 100, 120};
        int[] weights = {10, 20, 30};
        String[] names = {"Laptop", "Projector", "Server Unit"};
        int capacity = 50;

        System.out.println("\n[1] 0/1 Knapsack with Backtracking (Selected Items):");
        KnapsackResult result = knapSackWithItems(capacity, weights, profits, names);
        System.out.println("    " + result);

        System.out.println("\n[2] Space-Optimized 1D DP 0/1 Knapsack:");
        int optimizedProfit = knapSackSpaceOptimized(capacity, weights, profits);
        System.out.println("    Max Profit: " + optimizedProfit);

        System.out.println("\n[3] Fractional Knapsack (Greedy Strategy):");
        List<Item> items = List.of(
            new Item("Gold Dust", 10, 60),
            new Item("Silver Ingot", 20, 100),
            new Item("Platinum Bar", 30, 120)
        );
        double fractionalMax = fractionalKnapsack(capacity, items);
        System.out.printf("    Max Fractional Value for Capacity %d: %.2f%n", capacity, fractionalMax);

        System.out.println("\nAll Knapsack demonstrations executed successfully.");
    }
}
