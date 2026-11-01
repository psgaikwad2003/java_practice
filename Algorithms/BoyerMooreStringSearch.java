import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Boyer-Moore String Search Algorithm
 *
 * Implements high-performance pattern matching scanning the pattern from right to left:
 * 1. Bad Character Heuristic - Computes skip table based on mismatched text character
 * 2. Good Suffix Heuristic - Computes shift table based on matched pattern suffix
 * 3. Full Hybrid Boyer-Moore - Takes Math.max(badCharShift, goodSuffixShift)
 *
 * Time Complexity:
 * - Best / Average: O(N / M) sub-linear
 * - Worst: O(N * M) (or O(N + M) with Galil rule)
 * Space Complexity: O(M + AlphabetSize)
 */
public class BoyerMooreStringSearch {

    private static final int ALPHABET_SIZE = 256;

    public static class SearchResult {
        public final List<Integer> matchIndices;
        public final int characterComparisons;
        public final String method;

        public SearchResult(List<Integer> matchIndices, int characterComparisons, String method) {
            this.matchIndices = matchIndices;
            this.characterComparisons = characterComparisons;
            this.method = method;
        }

        @Override
        public String toString() {
            return String.format("%s -> Matches: %s | Comparisons: %d", method, matchIndices, characterComparisons);
        }
    }

    // -------------------------------------------------------------
    // Bad Character Preprocessing
    // Stores the rightmost index of each character in pattern
    // -------------------------------------------------------------
    public static int[] buildBadCharTable(String pattern) {
        int[] badChar = new int[ALPHABET_SIZE];
        Arrays.fill(badChar, -1);
        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i;
        }
        return badChar;
    }

    // -------------------------------------------------------------
    // Good Suffix Preprocessing
    // -------------------------------------------------------------
    private static int[] computeSuffixLengths(String pattern) {
        int m = pattern.length();
        int[] suffix = new int[m];
        suffix[m - 1] = m;

        int f = 0, g = m - 1;
        for (int i = m - 2; i >= 0; i--) {
            if (i > g && suffix[i + m - 1 - f] < i - g) {
                suffix[i] = suffix[i + m - 1 - f];
            } else {
                if (i < g) g = i;
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f)) {
                    g--;
                }
                suffix[i] = f - g;
            }
        }
        return suffix;
    }

    public static int[] buildGoodSuffixTable(String pattern) {
        int m = pattern.length();
        int[] goodSuffix = new int[m];
        int[] suffix = computeSuffixLengths(pattern);

        // Case 1: Pattern contains another occurrence of matching suffix
        Arrays.fill(goodSuffix, m);
        int j = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (; j < m - 1 - i; j++) {
                    if (goodSuffix[j] == m) {
                        goodSuffix[j] = m - 1 - i;
                    }
                }
            }
        }

        // Case 2: Matching prefix of pattern aligns with suffix
        for (int i = 0; i <= m - 2; i++) {
            goodSuffix[m - 1 - suffix[i]] = m - 1 - i;
        }

        return goodSuffix;
    }

    // -------------------------------------------------------------
    // 1. Boyer-Moore Search (Bad Character Rule Only)
    // -------------------------------------------------------------
    public static SearchResult searchBadCharOnly(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new SearchResult(matches, 0, "Boyer-Moore (Bad Char Only)");
        }

        int n = text.length();
        int m = pattern.length();
        int[] badChar = buildBadCharTable(pattern);

        int shift = 0;
        int comparisons = 0;

        while (shift <= n - m) {
            int j = m - 1;

            while (j >= 0) {
                comparisons++;
                if (pattern.charAt(j) != text.charAt(shift + j)) {
                    break;
                }
                j--;
            }

            if (j < 0) {
                matches.add(shift);
                shift += (shift + m < n) ? m - badChar[text.charAt(shift + m)] : 1;
            } else {
                shift += Math.max(1, j - badChar[text.charAt(shift + j)]);
            }
        }

        return new SearchResult(matches, comparisons, "Boyer-Moore (Bad Char Only)");
    }

    // -------------------------------------------------------------
    // 2. Full Boyer-Moore Search (Bad Character + Good Suffix)
    // -------------------------------------------------------------
    public static SearchResult search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new SearchResult(matches, 0, "Full Boyer-Moore");
        }

        int n = text.length();
        int m = pattern.length();
        int[] badChar = buildBadCharTable(pattern);
        int[] goodSuffix = buildGoodSuffixTable(pattern);

        int shift = 0;
        int comparisons = 0;

        while (shift <= n - m) {
            int j = m - 1;

            while (j >= 0) {
                comparisons++;
                if (pattern.charAt(j) != text.charAt(shift + j)) {
                    break;
                }
                j--;
            }

            if (j < 0) {
                matches.add(shift);
                shift += goodSuffix[0];
            } else {
                int badCharShift = j - badChar[text.charAt(shift + j)];
                int goodSuffixShift = goodSuffix[j];
                shift += Math.max(1, Math.max(badCharShift, goodSuffixShift));
            }
        }

        return new SearchResult(matches, comparisons, "Full Boyer-Moore");
    }

    // Baseline Naive Search for comparison
    public static SearchResult searchNaive(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new SearchResult(matches, 0, "Naive Search");
        }

        int n = text.length();
        int m = pattern.length();
        int comparisons = 0;

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                matches.add(i);
            }
        }

        return new SearchResult(matches, comparisons, "Naive Search");
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("            BOYER-MOORE STRING SEARCH ALGORITHM              ");
        System.out.println("=============================================================");

        // Test Case 1: Standard Search with Multiple Occurrences
        String text1 = "ABAAABCDABCDEFAABCDABCE";
        String pattern1 = "ABCDABCE";
        System.out.println("\nTest Case 1:");
        System.out.println("Text   : " + text1);
        System.out.println("Pattern: " + pattern1);

        SearchResult resNaive1 = searchNaive(text1, pattern1);
        SearchResult resBadChar1 = searchBadCharOnly(text1, pattern1);
        SearchResult resFull1 = search(text1, pattern1);

        System.out.println("  " + resNaive1);
        System.out.println("  " + resBadChar1);
        System.out.println("  " + resFull1);

        // Test Case 2: Natural English Text
        String text2 = "WHICH_FINALLY_HALTS._AT_THAT_POINT_WE_CAN_FIND_THAT_NEEDLE";
        String pattern2 = "NEEDLE";
        System.out.println("\nTest Case 2: Natural Text");
        System.out.println("Text   : " + text2);
        System.out.println("Pattern: " + pattern2);

        SearchResult resNaive2 = searchNaive(text2, pattern2);
        SearchResult resFull2 = search(text2, pattern2);

        System.out.println("  " + resNaive2);
        System.out.println("  " + resFull2);
        double savings = (1.0 - (double) resFull2.characterComparisons / resNaive2.characterComparisons) * 100.0;
        System.out.printf("  Comparisons Saved by Boyer-Moore: %.2f%%\n", savings);

        // Test Case 3: Overlapping repetitive pattern
        String text3 = "AAAAAAAAAAAAAAAAAA";
        String pattern3 = "AAAA";
        System.out.println("\nTest Case 3: Repetitive Stream");
        System.out.println("Text   : " + text3);
        System.out.println("Pattern: " + pattern3);

        SearchResult resNaive3 = searchNaive(text3, pattern3);
        SearchResult resFull3 = search(text3, pattern3);

        System.out.println("  " + resNaive3);
        System.out.println("  " + resFull3);
        System.out.println("  Results Match: " + resNaive3.matchIndices.equals(resFull3.matchIndices));

        System.out.println("\n=============================================================");
    }
}
