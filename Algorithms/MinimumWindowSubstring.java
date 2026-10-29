import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {

    public static String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length() || t.isEmpty()) {
            return "";
        }

        Map<Character, Integer> targetCount = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetCount.put(c, targetCount.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> windowCount = new HashMap<>();
        int have = 0, need = targetCount.size();
        int minLen = Integer.MAX_VALUE;
        int startIndex = -1;

        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            char rChar = s.charAt(right);
            windowCount.put(rChar, windowCount.getOrDefault(rChar, 0) + 1);

            if (targetCount.containsKey(rChar) && windowCount.get(rChar).intValue() == targetCount.get(rChar).intValue()) {
                have++;
            }

            while (have == need) {
                int currentLen = right - left + 1;
                if (currentLen < minLen) {
                    minLen = currentLen;
                    startIndex = left;
                }

                char lChar = s.charAt(left);
                windowCount.put(lChar, windowCount.get(lChar) - 1);
                if (targetCount.containsKey(lChar) && windowCount.get(lChar) < targetCount.get(lChar)) {
                    have--;
                }
                left++;
            }
        }

        return startIndex == -1 ? "" : s.substring(startIndex, startIndex + minLen);
    }

    public static String minWindowAscii(String s, String t) {
        if (s == null || t == null || s.length() < t.length() || t.isEmpty()) {
            return "";
        }

        int[] targetMap = new int[128];
        int required = 0;
        for (char c : t.toCharArray()) {
            if (targetMap[c] == 0) required++;
            targetMap[c]++;
        }

        int[] windowMap = new int[128];
        int formed = 0;
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char r = s.charAt(right);
            windowMap[r]++;
            if (targetMap[r] > 0 && windowMap[r] == targetMap[r]) {
                formed++;
            }

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char l = s.charAt(left);
                windowMap[l]--;
                if (targetMap[l] > 0 && windowMap[l] < targetMap[l]) {
                    formed--;
                }
                left++;
            }
        }

        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        String s1 = "ADOBECODEBANC", t1 = "ABC";
        System.out.println("s: \"" + s1 + "\", t: \"" + t1 + "\"");
        System.out.println("Min Window (HashMap): \"" + minWindow(s1, t1) + "\"");
        System.out.println("Min Window (ASCII Array): \"" + minWindowAscii(s1, t1) + "\"");

        String s2 = "a", t2 = "a";
        System.out.println("\ns: \"" + s2 + "\", t: \"" + t2 + "\"");
        System.out.println("Min Window: \"" + minWindow(s2, t2) + "\"");

        String s3 = "a", t3 = "aa";
        System.out.println("\ns: \"" + s3 + "\", t: \"" + t3 + "\"");
        System.out.println("Min Window: \"" + minWindow(s3, t3) + "\"");
    }
}
