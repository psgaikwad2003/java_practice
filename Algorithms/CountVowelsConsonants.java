import java.util.LinkedHashMap;
import java.util.Map;

public class CountVowelsConsonants {

    public record TextAnalysisResult(
        int totalChars,
        int vowels,
        int consonants,
        int digits,
        int whitespace,
        int specialChars,
        Map<Character, Integer> vowelBreakdown
    ) {
        public void display() {
            System.out.println("Total Characters   : " + totalChars);
            System.out.println("Vowels Count       : " + vowels);
            System.out.println("Consonants Count   : " + consonants);
            System.out.println("Digits Count       : " + digits);
            System.out.println("Whitespace Count   : " + whitespace);
            System.out.println("Special Characters : " + specialChars);
            System.out.println("Vowel Breakdown    : " + vowelBreakdown);
        }
    }

    public static TextAnalysisResult analyze(String text) {
        if (text == null) {
            return new TextAnalysisResult(0, 0, 0, 0, 0, 0, Map.of());
        }

        int vowels = 0, consonants = 0, digits = 0, whitespace = 0, specials = 0;
        Map<Character, Integer> breakdown = new LinkedHashMap<>();
        for (char v : new char[]{'a', 'e', 'i', 'o', 'u'}) breakdown.put(v, 0);

        String lower = text.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            char ch = lower.charAt(i);

            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                vowels++;
                breakdown.put(ch, breakdown.get(ch) + 1);
            } else if (ch >= 'a' && ch <= 'z') {
                consonants++;
            } else if (Character.isDigit(ch)) {
                digits++;
            } else if (Character.isWhitespace(ch)) {
                whitespace++;
            } else {
                specials++;
            }
        }

        return new TextAnalysisResult(text.length(), vowels, consonants, digits, whitespace, specials, breakdown);
    }

    public static long countVowelsStream(String text) {
        if (text == null) return 0;
        return text.toLowerCase().chars()
            .filter(ch -> "aeiou".indexOf(ch) != -1)
            .count();
    }

    public static void main(String[] args) {
        String sample = "Java 21 is Modern, Fast & Robust!";
        System.out.println("=== Text Analysis: \"" + sample + "\" ===");
        TextAnalysisResult result = analyze(sample);
        result.display();
        System.out.println("Stream verification: " + countVowelsStream(sample) + " vowels found.");
    }
}
