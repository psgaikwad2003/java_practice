package Algorithms;

import java.util.Arrays;

public class LongestCommonSubsequenceDemo {

    public static int lcsMemo(String text1, String text2) {
        if (text1 == null || text2 == null) return 0;
        int m = text1.length();
        int n = text2.length();
        int[][] memo = new int[m + 1][n + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return memoHelper(text1, text2, m, n, memo);
    }

    private static int memoHelper(String s1, String s2, int i, int j, int[][] memo) {
        if (i == 0 || j == 0) return 0;
        if (memo[i][j] != -1) return memo[i][j];

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            memo[i][j] = 1 + memoHelper(s1, s2, i - 1, j - 1, memo);
        } else {
            memo[i][j] = Math.max(
                memoHelper(s1, s2, i - 1, j, memo),
                memoHelper(s1, s2, i, j - 1, memo)
            );
        }
        return memo[i][j];
    }

    public static int lcsTabulation(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) return 0;
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    public static int lcsSpaceOptimized(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) return 0;

        if (text1.length() < text2.length()) {
            String temp = text1;
            text1 = text2;
            text2 = temp;
        }

        int m = text1.length();
        int n = text2.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = 1 + prev[j - 1];
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            System.arraycopy(curr, 0, prev, 0, n + 1);
        }
        return prev[n];
    }

    public static String getLCSString(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) return "";
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcs.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Longest Common Subsequence (LCS) Demo ===");

        String[][] testCases = {
            {"abcde", "ace"},
            {"abc", "abc"},
            {"abc", "def"},
            {"AGGTAB", "GXTXAYB"},
            {"DYNAMICPROGRAMMING", "ALGORITHM"}
        };

        for (String[] tc : testCases) {
            String s1 = tc[0];
            String s2 = tc[1];
            int lenMemo = lcsMemo(s1, s2);
            int lenTab = lcsTabulation(s1, s2);
            int lenOpt = lcsSpaceOptimized(s1, s2);
            String lcsStr = getLCSString(s1, s2);

            System.out.printf("s1: \"%s\", s2: \"%s\"%n", s1, s2);
            System.out.printf("  -> Length (Memo): %d | Tabulation: %d | Optimized: %d%n", lenMemo, lenTab, lenOpt);
            System.out.printf("  -> Actual Subsequence: \"%s\"%n%n", lcsStr);
        }
    }
}
