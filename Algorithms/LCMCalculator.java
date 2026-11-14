import java.math.BigInteger;
import java.util.Arrays;

public class LCMCalculator {

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

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        a = Math.abs(a);
        b = Math.abs(b);
        return Math.multiplyExact(a / gcd(a, b), b);
    }

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

    public static BigInteger lcmBigInteger(BigInteger a, BigInteger b) {
        if (a.signum() == 0 || b.signum() == 0) return BigInteger.ZERO;
        BigInteger gcdVal = a.gcd(b);
        return a.abs().divide(gcdVal).multiply(b.abs());
    }

    public record Fraction(long num, long den) {
        public Fraction {
            if (den == 0) throw new IllegalArgumentException("Denominator cannot be 0");
        }
        @Override public String toString() { return num + "/" + den; }
    }

    public static Fraction lcmFractions(Fraction f1, Fraction f2) {
        long lcmNum = lcm(f1.num(), f2.num());
        long gcdDen = gcd(f1.den(), f2.den());
        return new Fraction(lcmNum, gcdDen);
    }

    public static void main(String[] args) {
        System.out.println("=== LCM Calculations & Overflow Detection ===");

        long n1 = 72, n2 = 120;
        System.out.printf("LCM of %d and %d: %d%n", n1, n2, lcm(n1, n2));

        System.out.println("\n=== LCM of Multiple Numbers ===");
        long[] list = {12, 15, 20, 30};
        System.out.println("LCM of " + Arrays.toString(list) + " = " + lcm(list));

        System.out.println("\n=== BigInteger LCM (Arbitrary Precision) ===");
        BigInteger big1 = new BigInteger("12345678901234567890");
        BigInteger big2 = new BigInteger("98765432109876543210");
        System.out.println("LCM of big numbers: " + lcmBigInteger(big1, big2));

        System.out.println("\n=== Rational Fraction LCM ===");
        Fraction frac1 = new Fraction(2, 3);
        Fraction frac2 = new Fraction(4, 9);
        System.out.printf("LCM of %s and %s = %s%n", frac1, frac2, lcmFractions(frac1, frac2));
    }
}
