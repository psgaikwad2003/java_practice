import java.util.ArrayList;
import java.util.List;

public class ArmstrongNumber {

    public static int countDigits(long num) {
        if (num == 0) return 1;
        int count = 0;
        long temp = Math.abs(num);
        while (temp > 0) {
            count++;
            temp /= 10;
        }
        return count;
    }

    public static long intPow(long base, int exp) {
        long result = 1;
        long b = base;
        int e = exp;
        while (e > 0) {
            if ((e & 1) == 1) {
                result *= b;
            }
            b *= b;
            e >>= 1;
        }
        return result;
    }

    public static boolean isArmstrong(long number) {
        if (number < 0) return false;

        int power = countDigits(number);
        long original = number;
        long sum = 0;

        while (original > 0) {
            long digit = original % 10;
            sum += intPow(digit, power);
            original /= 10;
        }

        return sum == number;
    }

    public static List<Long> findArmstrongNumbers(long start, long end) {
        List<Long> list = new ArrayList<>();
        for (long i = Math.max(0, start); i <= end; i++) {
            if (isArmstrong(i)) {
                list.add(i);
            }
        }
        return list;
    }

    public static long nextArmstrong(long from) {
        long candidate = Math.max(0, from) + 1;
        while (!isArmstrong(candidate)) {
            candidate++;
        }
        return candidate;
    }

    public static String getArmstrongBreakdown(long number) {
        if (number < 0) return number + " is negative, hence not an Armstrong number.";
        int power = countDigits(number);
        String s = String.valueOf(number);
        List<String> terms = new ArrayList<>();
        List<Long> evaluated = new ArrayList<>();
        long total = 0;

        for (int i = 0; i < s.length(); i++) {
            long d = s.charAt(i) - '0';
            long termVal = intPow(d, power);
            terms.add(d + "^" + power);
            evaluated.add(termVal);
            total += termVal;
        }

        String termsStr = String.join(" + ", terms);
        String evalStr = evaluated.stream().map(String::valueOf).reduce((a, b) -> a + " + " + b).orElse("0");
        boolean matches = (total == number);
        return String.format("%d: %s = %s = %d -> %s",
            number, termsStr, evalStr, total, matches ? "VALID ARMSTRONG" : "NOT ARMSTRONG");
    }

    public static void main(String[] args) {
        System.out.println("=== Generalized Armstrong (Narcissistic) Number Checker ===");

        long[] testCases = {0, 1, 9, 153, 370, 371, 407, 1634, 8208, 9474, 54748, 100};
        for (long n : testCases) {
            System.out.printf("%6d is Armstrong? %s%n", n, isArmstrong(n) ? "YES" : "NO");
        }

        System.out.println("\n=== Detailed Mathematical Breakdown ===");
        long[] sampleDecompositions = {153, 370, 1634, 9474, 123};
        for (long n : sampleDecompositions) {
            System.out.println("  " + getArmstrongBreakdown(n));
        }

        System.out.println("\n=== All Armstrong Numbers in Range [0, 10000] ===");
        List<Long> found = findArmstrongNumbers(0, 10000);
        System.out.println(found);
        System.out.println("Total found: " + found.size());

        System.out.println("\n=== Next Armstrong Number After... ===");
        long[] seeds = {0, 9, 100, 400, 1000};
        for (long seed : seeds) {
            System.out.printf("Next Armstrong after %5d -> %d%n", seed, nextArmstrong(seed));
        }
    }
}
