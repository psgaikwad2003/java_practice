import java.util.Arrays;

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

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a / gcd(a, b) * b);
    }

    public static long modInverse(long a, long m) {
        if (m <= 1) {
            throw new IllegalArgumentException("Modulus m must be greater than 1.");
        }
        ExtendedGCDResult result = extendedGCD(a, m);
        if (result.gcd() != 1) {
            throw new ArithmeticException(String.format("Modular inverse does not exist: %d and %d are not coprime (gcd = %d).", a, m, result.gcd()));
        }
        return (result.x() % m + m) % m;
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
        System.out.println("Numbers: " + java.util.Arrays.toString(numbers));
        System.out.println("GCD: " + gcd(numbers));

        System.out.println("\n=== Least Common Multiple (LCM) ===");
        System.out.printf("LCM(%d, %d) = %d%n", a, b, lcm(a, b));
        System.out.println("LCM(0, 12) = " + lcm(0, 12));
        System.out.println("LCM(4, 6) = " + lcm(4, 6));
        System.out.println("LCM(21, 14) = " + lcm(21, 14));

        System.out.println("\n=== Extended Euclidean Algorithm (Bézout Identity) ===");
        long exA = 35, exB = 15;
        ExtendedGCDResult ext = extendedGCD(exA, exB);
        System.out.printf("%d*(%d) + %d*(%d) = %d (GCD)%n",
            exA, ext.x(), exB, ext.y(), ext.gcd());

        long exA2 = 240, exB2 = 46;
        ExtendedGCDResult ext2 = extendedGCD(exA2, exB2);
        System.out.printf("%d*(%d) + %d*(%d) = %d (GCD)%n",
            exA2, ext2.x(), exB2, ext2.y(), ext2.gcd());

        System.out.println("\n=== Modular Multiplicative Inverse ===");
        long val = 3, mod = 11;
        long inv = modInverse(val, mod);
        System.out.printf("Modular inverse of %d mod %d = %d (Check: (%d * %d) %% %d = %d)%n",
            val, mod, inv, val, inv, mod, (val * inv) % mod);

        long val2 = 10, mod2 = 17;
        long inv2 = modInverse(val2, mod2);
        System.out.printf("Modular inverse of %d mod %d = %d (Check: (%d * %d) %% %d = %d)%n",
            val2, mod2, inv2, val2, inv2, mod2, (val2 * inv2) % mod2);
    }
}
