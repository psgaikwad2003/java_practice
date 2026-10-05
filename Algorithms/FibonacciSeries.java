import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FibonacciSeries {

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

    public static boolean isFibonacci(long num) {
        if (num < 0) return false;
        long val1 = 5L * num * num + 4;
        long val2 = 5L * num * num - 4;
        long sqrt1 = (long) Math.sqrt(val1);
        long sqrt2 = (long) Math.sqrt(val2);
        return sqrt1 * sqrt1 == val1 || sqrt2 * sqrt2 == val2;
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

        System.out.println("\n=== Fibonacci Membership Check ===");
        long[] candidates = {0, 1, 2, 4, 5, 8, 10, 13, 20, 21, 34};
        for (long c : candidates) {
            System.out.printf("%3d is Fibonacci? %s%n", c, isFibonacci(c) ? "YES" : "NO");
        }
    }
}
