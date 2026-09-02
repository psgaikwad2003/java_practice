public class BinaryToDecimal {
    public static void main(String[] args) {
        long num = 10011011;
        long binaryNumber = num;
        int decimalNumber = 0, i = 0;
        long remainder;

        while (binaryNumber != 0) {
            remainder = binaryNumber % 10;
            binaryNumber /= 10;
            decimalNumber += remainder * Math.pow(2, i);
            ++i;
        }

        System.out.println("Binary " + num + " in Decimal is: " + decimalNumber);
    }
}
// Updated for demonstration
