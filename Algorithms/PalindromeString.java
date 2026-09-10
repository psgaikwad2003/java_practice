public class PalindromeString {

    /**
     * Checks if a string is a palindrome using two pointers.
     * Case-insensitive comparison. Empty strings are considered palindromes.
     * Time Complexity: O(n), Space Complexity: O(1)
     *
     * @param str the string to check
     * @return true if str is a palindrome, false otherwise
     */
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

    /**
     * Checks if a phrase is a palindrome, considering only alphanumeric characters and ignoring cases.
     * Classic LeetCode #125 pattern.
     *
     * @param s the phrase to check
     * @return true if the phrase is a palindrome when ignoring non-alphanumeric chars
     */
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

    /**
     * Checks if a string is a palindrome using StringBuilder reversal.
     * Simple but uses O(n) extra space.
     * Time Complexity: O(n), Space Complexity: O(n)
     *
     * @param str the string to check
     * @return true if the string equals its reverse (case-sensitive)
     */
    public static boolean isPalindromeReverse(String str) {
        if (str == null) return false;
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equalsIgnoreCase(reversed);
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
    }
}

