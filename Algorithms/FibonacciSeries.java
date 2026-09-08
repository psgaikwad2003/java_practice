import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FibonacciSeries {

    /**
     * Generates the first n terms of the Fibonacci sequence using iterative O(1) extra space.
     */
    public static List<Long> generateSeries(int n) {
        if (n <= 0) return new ArrayList<>();
        List<Long> series = new ArrayList<>(n);

        long a = 0, b = 1;
        for (int i = 1; i <= n; i++) {
            series.add(a);
            long next = a + b;
            a = b;
            b = next;
        }
        return series;
    }

    /**
     * Finds the N-th Fibonacci number in O(n) time and O(1) space.
     */
    public static long getNthFibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative: " + n);
        }
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev2 = 0, prev1 = 1, current = 0;
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return current;
    }

    /**
     * Computes arbitrarily large Fibonacci numbers using BigInteger to avoid overflow.
     */
    public static BigInteger getNthFibonacciBigInteger(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative: " + n);
        }
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        BigInteger prev2 = BigInteger.ZERO;
        BigInteger prev1 = BigInteger.ONE;
        BigInteger current = BigInteger.ZERO;

        for (int i = 2; i <= n; i++) {
            current = prev1.add(prev2);
            prev2 = prev1;
            prev1 = current;
        }
        return current;
    }

    public static void main(String[] args) {
        System.out.println("=== Fibonacci Series & N-th Term Generation ===");

        int n = 15;
        System.out.println("First " + n + " Fibonacci terms: " + generateSeries(n));

        System.out.println("\n=== Specific Term Lookups ===");
        int[] indices = {0, 1, 5, 10, 20, 50};
        for (int idx : indices) {
            System.out.printf("Fibonacci(%2d) = %d%n", idx, getNthFibonacci(idx));
        }

        System.out.println("\n=== Large Term Computation (BigInteger) ===");
        int[] largeIndices = {100, 200};
        for (int idx : largeIndices) {
            System.out.printf("Fibonacci(%d) = %s%n", idx, getNthFibonacciBigInteger(idx));
        }
    }
}
