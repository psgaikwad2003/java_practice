import java.util.Arrays;

public class GCDCalculator {

    /**
     * Computes the Greatest Common Divisor (GCD) using the Euclidean Algorithm (Iterative).
     * Time Complexity: O(log(min(a, b)))
     */
    public static long gcd(long a, long b) {
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
     * Computes the Greatest Common Divisor (GCD) using the Euclidean Algorithm (Recursive).
     */
    public static long gcdRecursive(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        return (b == 0) ? a : gcdRecursive(b, a % b);
    }

    /**
     * Computes the GCD for an array or variable arguments of numbers.
     */
    public static long gcd(long... numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty.");
        }
        long result = Math.abs(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            result = gcd(result, numbers[i]);
            if (result == 1) break; // Early exit optimization
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Greatest Common Divisor (GCD) Calculations ===");

        long a = 81, b = 153;
        System.out.printf("GCD of %d and %d (Iterative): %d%n", a, b, gcd(a, b));
        System.out.printf("GCD of %d and %d (Recursive): %d%n", a, b, gcdRecursive(a, b));

        System.out.println("\n=== Handling Edge Cases (Zero & Negatives) ===");
        System.out.println("GCD of 0 and 25: " + gcd(0, 25));
        System.out.println("GCD of -48 and 18: " + gcd(-48, 18));
        System.out.println("GCD of 0 and 0: " + gcd(0, 0));

        System.out.println("\n=== GCD of Multiple Numbers ===");
        long[] numbers = {48, 72, 108, 144};
        System.out.println("Numbers: " + Arrays.toString(numbers));
        System.out.println("GCD: " + gcd(numbers));
    }
}
