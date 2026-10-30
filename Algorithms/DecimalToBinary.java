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

        if (isNegative) {
            sb.append("-");
        }
        return sb.reverse().toString();
    }

    public static String toBinaryBitwise(int n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        boolean foundFirstOne = false;

        for (int i = 31; i >= 0; i--) {
            int bit = (n >>> i) & 1;
            if (bit == 1) {
                foundFirstOne = true;
            }
            if (foundFirstOne) {
                sb.append(bit);
            }
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

    public static int[] toBinaryDigits(long n) {
        String binaryStr = toBinary(Math.abs(n));
        int[] digits = new int[binaryStr.length()];
        for (int i = 0; i < binaryStr.length(); i++) {
            digits[i] = binaryStr.charAt(i) - '0';
        }
        return digits;
    }

    public static String toBinaryRecursive(long n) {
        if (n < 0) return "-" + toBinaryRecursive(-n);
        if (n == 0) return "0";
        if (n == 1) return "1";
        return toBinaryRecursive(n / 2) + (n % 2);
    }

    public static void main(String[] args) {
        long[] testValues = {0, 1, 2, 7, 19, 42, 255, 1024, -19};

        System.out.println("=== Decimal to Binary Demonstrations ===\n");
        for (long num : testValues) {
            String bin = toBinary(num);
            String rec = toBinaryRecursive(num);
            int[] digits = toBinaryDigits(num);
            System.out.printf("Decimal: %6d -> Binary: %12s | Recursive: %12s | Digits: %s%n",
                    num, bin, rec, Arrays.toString(digits));
        }

        System.out.println("\n=== Bitwise and Two's Complement (32-bit) ===");
        int[] bitwiseTests = {19, -19, 0, Integer.MAX_VALUE, Integer.MIN_VALUE};
        for (int val : bitwiseTests) {
            System.out.printf("Value: %11d | Unsigned: %32s | 32-bit: %s%n",
                    val, toBinaryBitwise(val), to32BitBinary(val));
        }
    }
}
