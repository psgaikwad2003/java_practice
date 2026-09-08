public class PalindromeString {

    /**
     * Checks if a string is a palindrome using two pointers.
     * Time Complexity: O(n), Space Complexity: O(1)
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

    public static void main(String[] args) {
        System.out.println("=== Palindrome Word Check (Two-Pointer) ===");
        String[] words = {"Radar", "Level", "Madam", "Java", "noon", "hello"};
        for (String word : words) {
            System.out.printf("'%s' is palindrome? %s%n", word, isPalindrome(word) ? "YES" : "NO");
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
