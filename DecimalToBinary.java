public class DecimalToBinary {
    public static void main(String[] args) {
        int num = 19;
        System.out.println("Decimal " + num + " to Binary is: " + Integer.toBinaryString(num));
        
        // Manual way
        int[] binaryNum = new int[1000];
        int n = 19;
        int i = 0;
        while (n > 0) {
            binaryNum[i] = n % 2;
            n = n / 2;
            i++;
        }
        
        System.out.print("Binary representation manually: ");
        for (int j = i - 1; j >= 0; j--)
            System.out.print(binaryNum[j]);
    }
}
