public class PalindromeString {

    public static boolean isPalindrome(String str) {
        if (str == null) return false;
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (Character.toLowerCase(str.charAt(left)) != Character.toLowerCase(str.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isPalindromePhrase(String s) {
        if (s == null) return false;
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }

            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isPalindromeReverse(String str) {
        if (str == null) return false;
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equalsIgnoreCase(reversed);
    }

    public static String longestPalindromeSubstring(String s) {
        if (s == null || s.length() <= 1) return s == null ? "" : s;
        int start = 0, end = 0;

        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int maxLen = Math.max(len1, len2);
            if (maxLen > end - start + 1) {
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    public static boolean canPermutePalindrome(String s) {
        if (s == null) return false;
        java.util.Set<Character> oddChars = new java.util.HashSet<>();
        for (char c : s.toLowerCase().toCharArray()) {
            if (!oddChars.add(c)) {
                oddChars.remove(c);
            }
        }
        return oddChars.size() <= 1;
    }

    public static void main(String[] args) {
        System.out.println("=== Palindrome Word Check (Two-Pointer) ===");
        String[] words = {"Radar", "Level", "Madam", "Java", "noon", "hello", "", "a"};
        for (String word : words) {
            System.out.printf("'%s' -> Two-Pointer: %-3s | StringBuilder: %s%n",
                word,
                isPalindrome(word) ? "YES" : "NO",
                isPalindromeReverse(word) ? "YES" : "NO");
        }

        System.out.println("\n=== Palindrome Phrase Check (Alphanumeric Only) ===");
        String[] phrases = {
            "A man, a plan, a canal: Panama",
            "race a car",
            "Was it a car or a cat I saw?",
            "No 'x' in Nixon",
            "Not a palindrome"
        };
        for (String phrase : phrases) {
            System.out.printf("\"%s\" -> %s%n", phrase, isPalindromePhrase(phrase) ? "PALINDROME" : "NOT PALINDROME");
        }

        System.out.println("\n=== Longest Palindromic Substring ===");
        String[] subTests = {"babad", "cbbd", "forgeeksskeegfor", "racecar", "abacdfgdcaba"};
        for (String st : subTests) {
            System.out.printf("Original: '%s' -> Longest Palindromic Substring: '%s'%n",
                st, longestPalindromeSubstring(st));
        }

        System.out.println("\n=== Permutation Can Form Palindrome ===");
        String[] permTests = {"code", "aab", "carerac", "tactcoa", "daily"};
        for (String pt : permTests) {
            System.out.printf("Can '%s' form a palindrome? %s%n",
                pt, canPermutePalindrome(pt) ? "YES" : "NO");
        }
    }
}
