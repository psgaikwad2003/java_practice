import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnagramCheck {

    public static boolean isAnagramSort(String str1, String str2) {
        if (str1 == null || str2 == null) return false;
        if (str1.length() != str2.length()) return false;

        char[] a1 = str1.toLowerCase().toCharArray();
        char[] a2 = str2.toLowerCase().toCharArray();
        Arrays.sort(a1);
        Arrays.sort(a2);
        return Arrays.equals(a1, a2);
    }

    public static boolean isAnagramFrequencyArray(String str1, String str2) {
        if (str1 == null || str2 == null) return false;
        if (str1.length() != str2.length()) return false;

        int[] count = new int[26];
        String s1 = str1.toLowerCase();
        String s2 = str2.toLowerCase();

        for (int i = 0; i < s1.length(); i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 >= 'a' && c1 <= 'z') count[c1 - 'a']++;
            if (c2 >= 'a' && c2 <= 'z') count[c2 - 'a']--;
        }

        for (int c : count) {
            if (c != 0) return false;
        }
        return true;
    }

    public static boolean isAnagramUnicode(String str1, String str2) {
        if (str1 == null || str2 == null) return false;
        if (str1.length() != str2.length()) return false;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : str1.toLowerCase().toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        for (char c : str2.toLowerCase().toCharArray()) {
            int current = freq.getOrDefault(c, 0);
            if (current == 0) return false;
            freq.put(c, current - 1);
        }
        return true;
    }

    public static boolean isPhraseAnagram(String phrase1, String phrase2) {
        if (phrase1 == null || phrase2 == null) return false;
        String clean1 = phrase1.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String clean2 = phrase2.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return isAnagramFrequencyArray(clean1, clean2);
    }

    public static void main(String[] args) {
        System.out.println("=== Anagram Check Demonstrations ===");

        String[][] pairs = {
            {"Listen", "Silent"},
            {"Triangle", "Integral"},
            {"Apple", "Pabble"},
            {"Astronomer", "Moon starer"},
            {"A gentleman", "Elegant man"}
        };

        for (String[] pair : pairs) {
            String s1 = pair[0];
            String s2 = pair[1];
            boolean phraseResult = isPhraseAnagram(s1, s2);
            System.out.printf("\"%s\" vs \"%s\" -> Anagram: %s%n", s1, s2, phraseResult ? "YES" : "NO");
        }
    }
}
