package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMPStringMatchingDemo {

    public static int[] computeLPSArray(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }

        int m = pattern.length();
        int[] lps = new int[m];
        int length = 0;
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

    public static List<Integer> searchAll(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return occurrences;
        }

        int[] lps = computeLPSArray(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == pattern.length()) {

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

    public static int searchFirst(String text, String pattern) {
        List<Integer> matches = searchAll(text, pattern);
        return matches.isEmpty() ? -1 : matches.get(0);
    }

    public static void main(String[] args) {
        System.out.println("=== Knuth-Morris-Pratt (KMP) String Matching Demo ===\n");

        String samplePattern = "ABABCABAB";
        int[] lps = computeLPSArray(samplePattern);
        System.out.println("Pattern: " + samplePattern);
        System.out.println("LPS Array: " + Arrays.toString(lps));
        System.out.println();

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
