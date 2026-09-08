import java.util.Arrays;

public class LCMCalculator {

    /**
     * Helper method to compute GCD using Euclidean algorithm.
     */
    private static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Computes the Least Common Multiple (LCM) of two numbers using LCM(a, b) = (|a * b|) / GCD(a, b).
     * Divides before multiplying to prevent premature integer overflow.
     */
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        a = Math.abs(a);
        b = Math.abs(b);
        return (a / gcd(a, b)) * b;
    }

    /**
     * Computes the LCM of an array or variable arguments of numbers.
     */
    public static long lcm(long... numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty.");
        }
        long result = Math.abs(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            result = lcm(result, numbers[i]);
            if (result == 0) return 0;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Least Common Multiple (LCM) Calculations ===");

        long n1 = 72, n2 = 120;
        System.out.printf("LCM of %d and %d: %d%n", n1, n2, lcm(n1, n2));

        long large1 = 1000000L, large2 = 1000001L;
        System.out.printf("LCM of large numbers (%d, %d): %d%n", large1, large2, lcm(large1, large2));

        System.out.println("\n=== LCM with Zero & Negative Values ===");
        System.out.println("LCM of 15 and 0: " + lcm(15, 0));
        System.out.println("LCM of -12 and 18: " + lcm(-12, 18));

        System.out.println("\n=== LCM of Multiple Numbers ===");
        long[] values = {12, 18, 24, 30};
        System.out.println("Values: " + Arrays.toString(values));
        System.out.println("Combined LCM: " + lcm(values));
    }
}
