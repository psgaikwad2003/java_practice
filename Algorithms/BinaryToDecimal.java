public class BinaryToDecimal {

    public static boolean isValidBinary(String binaryStr) {
        if (binaryStr == null || binaryStr.isEmpty()) return false;
        for (char c : binaryStr.toCharArray()) {
            if (c != '0' && c != '1') return false;
        }
        return true;
    }

    public static long binaryStringToDecimal(String binaryStr) {
        if (binaryStr == null || binaryStr.isEmpty()) {
            throw new IllegalArgumentException("Binary string must not be empty.");
        }

        long decimal = 0;
        for (int i = 0; i < binaryStr.length(); i++) {
            char ch = binaryStr.charAt(i);
            if (ch != '0' && ch != '1') {
                throw new NumberFormatException("Invalid binary character at position " + i + ": " + ch);
            }
            decimal = (decimal << 1) | (ch - '0');
        }
        return decimal;
    }

    public static long binaryLongToDecimal(long binaryNum) {
        long decimal = 0;
        long base = 1;
        long temp = Math.abs(binaryNum);

        while (temp > 0) {
            long lastDigit = temp % 10;
            if (lastDigit != 0 && lastDigit != 1) {
                throw new NumberFormatException("Number contains non-binary digit: " + lastDigit);
            }
            decimal += lastDigit * base;
            base *= 2;
            temp /= 10;
        }

        return (binaryNum < 0) ? -decimal : decimal;
    }

    public static String decimalToBinary(long decimalNum) {
        if (decimalNum == 0) return "0";
        StringBuilder sb = new StringBuilder();
        long temp = Math.abs(decimalNum);

        while (temp > 0) {
            sb.append(temp % 2);
            temp /= 2;
        }

        if (decimalNum < 0) {
            sb.append("-");
        }
        return sb.reverse().toString();
    }

    public static String binaryStringToHex(String binaryStr) {
        long decimal = binaryStringToDecimal(binaryStr);
        return Long.toHexString(decimal).toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println("=== Binary to Decimal Conversion (Bidirectional) ===");

        String[] binaryStrings = {"0", "1", "1010", "10011011", "11111111"};
        for (String bin : binaryStrings) {
            long dec = binaryStringToDecimal(bin);
            System.out.printf("Binary: %10s -> Decimal: %4d | Hex: %4s (Java built-in: %4d)%n",
                bin, dec, binaryStringToHex(bin), Long.parseLong(bin, 2));
        }

        System.out.println("\n=== Decimal to Binary Conversion ===");
        long[] decimals = {0, 5, 10, 42, 155, 255};
        for (long dec : decimals) {
            String bin = decimalToBinary(dec);
            System.out.printf("Decimal: %4d -> Binary: %10s (Java built-in: %10s)%n",
                dec, bin, Long.toBinaryString(dec));
        }

        System.out.println("\n=== Numeric Long Binary Conversion ===");
        long num = 10011011L;
        System.out.println("Long digits " + num + " -> Decimal: " + binaryLongToDecimal(num));

        System.out.println("\n=== Binary Validation ===");
        String[] toValidate = {"1010", "10201", "", "1111", "abc"};
        for (String s : toValidate) {
            System.out.printf("'%s' is valid binary? %s%n", s, isValidBinary(s) ? "YES" : "NO");
        }
    }
}
