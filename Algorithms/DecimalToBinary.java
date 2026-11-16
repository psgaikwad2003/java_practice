import java.util.Arrays;

public class DecimalToBinary {

    public static String toBinary(long n) {
        if (n == 0) return "0";
        boolean isNegative = n < 0;
        long temp = Math.abs(n);
        StringBuilder sb = new StringBuilder();

        while (temp > 0) {
            sb.append(temp % 2);
            temp /= 2;
        }

        if (isNegative) sb.append("-");
        return sb.reverse().toString();
    }

    public static String fractionalDecimalToBinary(double num, int precision) {
        if (Double.isNaN(num) || Double.isInfinite(num)) return String.valueOf(num);
        long integerPart = (long) num;
        double fractionalPart = Math.abs(num - integerPart);

        StringBuilder sb = new StringBuilder();
        sb.append(toBinary(integerPart));
        sb.append(".");

        int count = 0;
        while (fractionalPart > 0 && count < precision) {
            fractionalPart *= 2;
            int bit = (int) fractionalPart;
            sb.append(bit);
            fractionalPart -= bit;
            count++;
        }
        return sb.toString();
    }

    public static String to32BitBinary(int n) {
        char[] bits = new char[32];
        for (int i = 31; i >= 0; i--) {
            bits[31 - i] = ((n >>> i) & 1) == 1 ? '1' : '0';
        }
        return new String(bits);
    }

    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1); // Clears the lowest set bit
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Decimal to Binary & Radix Representations ===");

        long[] testInts = {0, 5, 42, 255, 1024, -15};
        for (long n : testInts) {
            System.out.printf("Decimal: %-5d -> Binary: %-12s | Hex: 0x%X%n", n, toBinary(n), n);
        }

        System.out.println("\n=== Fractional Floating-Point to Binary ===");
        double[] testFloats = {0.625, 13.625, 3.14159};
        for (double d : testFloats) {
            System.out.printf("Decimal: %-8.5f -> Binary: %s%n", d, fractionalDecimalToBinary(d, 8));
        }

        System.out.println("\n=== Hamming Weight (Set Bits Count) ===");
        int val = 42; // 101010 in binary -> 3 set bits
        System.out.printf("%d (%s) set bits: %d%n", val, toBinary(val), hammingWeight(val));
    }
}
