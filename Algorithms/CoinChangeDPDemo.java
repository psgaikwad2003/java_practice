import java.util.*;

public class CoinChangeDPDemo {

    public static int minCoins(int[] coins, int amount) {
        if (amount < 0 || coins == null || coins.length == 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }

        int[] dp = new int[amount + 1];

        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static List<Integer> minCoinsWithPath(int[] coins, int amount) {
        if (amount <= 0 || coins == null || coins.length == 0) {
            return Collections.emptyList();
        }

        int[] dp = new int[amount + 1];
        int[] lastCoin = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        Arrays.fill(lastCoin, -1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    lastCoin[i] = coin;
                }
            }
        }

        if (dp[amount] > amount) {
            return Collections.emptyList();
        }

        List<Integer> result = new ArrayList<>();
        int curr = amount;
        while (curr > 0) {
            int coin = lastCoin[curr];
            result.add(coin);
            curr -= coin;
        }

        return result;
    }

    public static int totalCombinations(int[] coins, int amount) {
        if (amount < 0 || coins == null) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }

        int[] dp = new int[amount + 1];
        dp[0] = 1;

        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }

        return dp[amount];
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     DYNAMIC PROGRAMMING: COIN CHANGE     ");
        System.out.println("==========================================");

        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        System.out.println("\n[1] Minimum Coins Problem (Coin Change I):");
        System.out.println("    Coins available: " + Arrays.toString(coins1));
        System.out.println("    Target Amount: " + amount1);
        int minCount = minCoins(coins1, amount1);
        List<Integer> path = minCoinsWithPath(coins1, amount1);
        System.out.println("    Minimum coins needed: " + minCount);
        System.out.println("    Reconstructed Coin Selection: " + path);

        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("\n[2] Edge Case - Unreachable Amount:");
        System.out.println("    Coins: " + Arrays.toString(coins2) + ", Target: " + amount2);
        System.out.println("    Result: " + minCoins(coins2, amount2) + " (Expected: -1)");

        int[] coins3 = {1, 2, 5};
        int amount3 = 5;
        System.out.println("\n[3] Total Unique Combinations (Coin Change II):");
        System.out.println("    Coins: " + Arrays.toString(coins3) + ", Target: " + amount3);
        int combos = totalCombinations(coins3, amount3);
        System.out.println("    Total Combinations: " + combos);
        System.out.println("    Combinations are:");
        System.out.println("      - 5");
        System.out.println("      - 2 + 2 + 1");
        System.out.println("      - 2 + 1 + 1 + 1");
        System.out.println("      - 1 + 1 + 1 + 1 + 1");

        int[] coins4 = {186, 419, 83, 408};
        int amount4 = 6249;
        System.out.println("\n[4] Large Benchmark Case:");
        System.out.println("    Target: " + amount4);
        System.out.println("    Min Coins: " + minCoins(coins4, amount4));
        System.out.println("    Actual Coins Used: " + minCoinsWithPath(coins4, amount4));

        System.out.println("\nAll Coin Change DP tests completed successfully.");
    }
}
