import java.util.Arrays;
import java.util.OptionalLong;

public class GCDCalculator {

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

    public static long gcdRecursive(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        return (b == 0) ? a : gcdRecursive(b, a % b);
    }

    // Stein's Binary GCD Algorithm (shift-based O(log(min(a,b))))
    public static long binaryGcdStein(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0) return b;
        if (b == 0) return a;

        int shift = Long.numberOfTrailingZeros(a | b);
        a >>= Long.numberOfTrailingZeros(a);

        do {
            b >>= Long.numberOfTrailingZeros(b);
            if (a > b) {
                long temp = a;
                a = b;
                b = temp;
            }
            b -= a;
        } while (b != 0);

        return a << shift;
    }

    public static long gcd(long... numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty.");
        }
        long result = Math.abs(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            result = gcd(result, numbers[i]);
            if (result == 1) break;
        }
        return result;
    }

    public record ExtendedGCDResult(long gcd, long x, long y) {}

    public static ExtendedGCDResult extendedGCD(long a, long b) {
        if (b == 0) {
            return new ExtendedGCDResult(Math.abs(a), a < 0 ? -1 : 1, 0);
        }
        ExtendedGCDResult next = extendedGCD(b, a % b);
        long gcd = next.gcd();
        long x = next.y();
        long y = next.x() - (a / b) * next.y();
        return new ExtendedGCDResult(gcd, x, y);
    }

    public static OptionalLong modInverse(long a, long m) {
        ExtendedGCDResult res = extendedGCD(a, m);
        if (res.gcd() != 1) {
            return OptionalLong.empty(); // Coprime required
        }
        long inv = (res.x() % m + m) % m;
        return OptionalLong.of(inv);
    }

    public static boolean areCoprime(long a, long b) {
        return gcd(a, b) == 1;
    }

    public static void main(String[] args) {
        System.out.println("=== Greatest Common Divisor (Euclidean & Stein) ===");

        long a = 48, b = 18;
        System.out.printf("Euclidean GCD(%d, %d) = %d%n", a, b, gcd(a, b));
        System.out.printf("Stein Binary GCD(%d, %d) = %d%n", a, b, binaryGcdStein(a, b));

        System.out.println("\n=== Multi-number GCD ===");
        long[] arr = {144, 216, 360, 540};
        System.out.println("GCD" + Arrays.toString(arr) + " = " + gcd(arr));

        System.out.println("\n=== Modular Multiplicative Inverse ===");
        long num = 3, mod = 11;
        OptionalLong inv = modInverse(num, mod);
        System.out.printf("Modular inverse of %d mod %d: %s%n", num, mod, inv.isPresent() ? inv.getAsLong() : "None");
    }
}
