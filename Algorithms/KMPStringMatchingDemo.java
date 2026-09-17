package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the Knuth-Morris-Pratt (KMP) string search algorithm.
 * 
 * Preprocesses the pattern into an LPS (Longest Prefix which is also Suffix) array
 * to achieve O(N + M) linear time pattern matching without backtracking in the main text.
 */
public class KMPStringMatchingDemo {

    /**
     * Builds the Longest Prefix Suffix (LPS) array for a given pattern.
     * lps[i] stores the length of the longest proper prefix of pattern[0..i]
     * that is also a suffix of pattern[0..i].
     *
     * @param pattern the pattern string
     * @return the computed LPS integer array
     */
    public static int[] computeLPSArray(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }

        int m = pattern.length();
        int[] lps = new int[m];
        int length = 0; // length of the previous longest prefix suffix
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Finds all starting indices of pattern in the given text using KMP.
     * Time Complexity: O(N + M) where N = text.length(), M = pattern.length()
     * Space Complexity: O(M) for the LPS array
     *
     * @param text the text to search in
     * @param pattern the pattern to find
     * @return List of 0-based starting indices where pattern occurs
     */
    public static List<Integer> searchAll(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return occurrences;
        }

        int[] lps = computeLPSArray(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == pattern.length()) {
                // Found pattern at index (i - j)
                occurrences.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return occurrences;
    }

    /**
     * Finds the first occurrence index of pattern in text, or -1 if not found.
     *
     * @param text source text
     * @param pattern search pattern
     * @return 0-based index of first match, or -1
     */
    public static int searchFirst(String text, String pattern) {
        List<Integer> matches = searchAll(text, pattern);
        return matches.isEmpty() ? -1 : matches.get(0);
    }

    public static void main(String[] args) {
        System.out.println("=== Knuth-Morris-Pratt (KMP) String Matching Demo ===\n");

        // 1. LPS Table demonstration
        String samplePattern = "ABABCABAB";
        int[] lps = computeLPSArray(samplePattern);
        System.out.println("Pattern: " + samplePattern);
        System.out.println("LPS Array: " + Arrays.toString(lps));
        System.out.println();

        // 2. Multi-occurrence searches
        String[][] testCases = {
            {"ABABDABACDABABCABAB", "ABABCABAB"},
            {"AABAACAADAABAABA", "AABA"},
            {"THIS IS A TEST TEXT", "TEST"},
            {"banana", "ana"},
            {"aaaaa", "aa"},
            {"abcdef", "xyz"}
        };

        for (String[] tc : testCases) {
            String txt = tc[0];
            String pat = tc[1];
            List<Integer> results = searchAll(txt, pat);
            int first = searchFirst(txt, pat);

            System.out.printf("Text:    \"%s\"%n", txt);
            System.out.printf("Pattern: \"%s\"%n", pat);
            System.out.printf("Matches found at indices: %s (First at: %d)%n%n", results, first);
        }
    }
}
