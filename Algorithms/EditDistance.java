import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditDistance {

    public static int minDistance(String word1, String word2) {
        if (word1 == null || word2 == null) return 0;
        int m = word1.length();
        int n = word2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        return dp[m][n];
    }

    public static int minDistanceSpaceOptimized(String word1, String word2) {
        if (word1 == null || word2 == null) return 0;
        if (word1.length() < word2.length()) {
            return minDistanceSpaceOptimized(word2, word1);
        }

        int m = word1.length();
        int n = word2.length();
        int[] dp = new int[n + 1];

        for (int j = 0; j <= n; j++) dp[j] = j;

        for (int i = 1; i <= m; i++) {
            int prev = dp[0];
            dp[0] = i;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = 1 + Math.min(prev, Math.min(dp[j], dp[j - 1]));
                }
                prev = temp;
            }
        }
        return dp[n];
    }

    public static List<String> getEditOperations(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        List<String> ops = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && word1.charAt(i - 1) == word2.charAt(j - 1)) {
                ops.add("Keep '" + word1.charAt(i - 1) + "'");
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.add("Substitute '" + word1.charAt(i - 1) + "' with '" + word2.charAt(j - 1) + "'");
                i--;
                j--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.add("Insert '" + word2.charAt(j - 1) + "'");
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add("Delete '" + word1.charAt(i - 1) + "'");
                i--;
            }
        }
        Collections.reverse(ops);
        return ops;
    }

    public static void main(String[] args) {
        String w1 = "horse", w2 = "ros";
        System.out.println("Transforming '" + w1 + "' -> '" + w2 + "':");
        System.out.println("Distance (2D DP): " + minDistance(w1, w2));
        System.out.println("Distance (Space Optimized): " + minDistanceSpaceOptimized(w1, w2));
        System.out.println("Operations: " + getEditOperations(w1, w2));

        String w3 = "intention", w4 = "execution";
        System.out.println("\nTransforming '" + w3 + "' -> '" + w4 + "':");
        System.out.println("Distance: " + minDistance(w3, w4));
        System.out.println("Operations: " + getEditOperations(w3, w4));
    }
}
