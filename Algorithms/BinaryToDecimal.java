public class BinaryToDecimal {

    /**
     * Validates that a string contains only '0' and '1' characters.
     *
     * @param binaryStr the string to validate
     * @return true if the string is a valid binary representation
     */
    public static boolean isValidBinary(String binaryStr) {
        if (binaryStr == null || binaryStr.isEmpty()) return false;
        for (char c : binaryStr.toCharArray()) {
            if (c != '0' && c != '1') return false;
        }
        return true;
    }

    /**
     * Converts a binary string to a decimal integer using bit shifting (Horner's method).
     * Throws NumberFormatException if invalid binary characters are detected.
     *
     * @param binaryStr binary representation as a String (e.g., "1010")
     * @return the decimal equivalent as a long
     * @throws IllegalArgumentException if binaryStr is null or empty
     * @throws NumberFormatException    if binaryStr contains non-binary characters
     */
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

    /**
     * Converts a binary number represented as long digits to decimal.
     *
     * @param binaryNum the binary number stored as a long integer (e.g., 1010L)
     * @return the decimal equivalent
     * @throws NumberFormatException if any digit is not 0 or 1
     */
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

    /**
     * Converts a decimal number to its binary representation as a String.
     *
     * @param decimalNum the decimal number to convert
     * @return binary string representation (e.g., "1010" for 10)
     */
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

    /**
     * Converts a binary string directly to a hexadecimal string.
     *
     * @param binaryStr the binary string to convert
     * @return uppercase hexadecimal representation (e.g., "FF")
     */
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
