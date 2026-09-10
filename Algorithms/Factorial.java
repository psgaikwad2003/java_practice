import java.math.BigInteger;

public class Factorial {

    /**
     * Calculates factorial iteratively.
     * Valid for n in range [0, 20] before 64-bit long overflow.
     *
     * @param n the non-negative integer whose factorial is computed
     * @return n! as a long
     * @throws IllegalArgumentException if n is negative
     */
    public static long factorialIterative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers: " + n);
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Calculates factorial recursively.
     *
     * @param n the non-negative integer whose factorial is computed
     * @return n! as a long
     * @throws IllegalArgumentException if n is negative
     */
    public static long factorialRecursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers: " + n);
        }
        if (n <= 1) {
            return 1;
        }
        return n * factorialRecursive(n - 1);
    }

    /**
     * Calculates factorial for arbitrary size using BigInteger to prevent numeric overflow.
     *
     * @param n the non-negative integer whose factorial is computed
     * @return n! as a BigInteger
     * @throws IllegalArgumentException if n is negative
     */
    public static BigInteger factorialBigInteger(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers: " + n);
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Counts the number of trailing zeros in n! using Legendre's formula.
     * Trailing zeros are produced by factors of 10 = 2 × 5, and since
     * factors of 2 are more abundant, we count factors of 5.
     * Time Complexity: O(log n)
     *
     * @param n the non-negative integer
     * @return the number of trailing zeros in n!
     * @throws IllegalArgumentException if n is negative
     */
    public static long trailingZerosInFactorial(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative.");
        long count = 0;
        for (long power = 5; power <= n; power *= 5) {
            count += n / power;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Factorial Calculation & Overflow Handling ===");

        int[] sampleValues = {0, 1, 5, 10, 20};
        for (int n : sampleValues) {
            System.out.printf("Factorial of %2d (Iterative): %d%n", n, factorialIterative(n));
            System.out.printf("Factorial of %2d (Recursive): %d%n", n, factorialRecursive(n));
        }

        System.out.println("\n=== Large Factorials with BigInteger ===");
        int[] largeValues = {25, 50, 100};
        for (int n : largeValues) {
            System.out.println(n + "! = " + factorialBigInteger(n));
        }

        System.out.println("\n=== Trailing Zeros in n! ===");
        int[] zeroTests = {5, 10, 25, 50, 100};
        for (int n : zeroTests) {
            System.out.printf("%3d! has %d trailing zero(s)%n", n, trailingZerosInFactorial(n));
        }
    }
}
