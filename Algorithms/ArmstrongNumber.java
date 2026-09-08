import java.util.ArrayList;
import java.util.List;

public class ArmstrongNumber {

    /**
     * Counts the number of digits in a positive integer.
     */
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

    /**
     * Checks if a number is an Armstrong (Narcissistic) number for any N digits.
     * A number is Armstrong if the sum of its own digits each raised to the power
     * of the number of digits equals the number itself.
     */
    public static boolean isArmstrong(long number) {
        if (number < 0) return false;

        int power = countDigits(number);
        long original = number;
        long sum = 0;

        while (original > 0) {
            long digit = original % 10;
            sum += Math.round(Math.pow(digit, power));
            original /= 10;
        }

        return sum == number;
    }

    /**
     * Finds all Armstrong numbers within an inclusive range [start, end].
     */
    public static List<Long> findArmstrongNumbers(long start, long end) {
        List<Long> list = new ArrayList<>();
        for (long i = Math.max(0, start); i <= end; i++) {
            if (isArmstrong(i)) {
                list.add(i);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println("=== Generalized Armstrong (Narcissistic) Number Checker ===");

        long[] testCases = {0, 1, 9, 153, 370, 371, 407, 1634, 8208, 9474, 54748, 100};
        for (long n : testCases) {
            System.out.printf("%6d is Armstrong? %s%n", n, isArmstrong(n) ? "YES" : "NO");
        }

        System.out.println("\n=== All Armstrong Numbers in Range [0, 10000] ===");
        List<Long> found = findArmstrongNumbers(0, 10000);
        System.out.println(found);
        System.out.println("Total found: " + found.size());
    }
}
