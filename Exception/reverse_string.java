public class reverse_string{

    public static void main(String[] args) {
        
        String str = "Hello, World!";
        String reversedStr = reverseString(str);
        System.out.println("Original String: " + str);
        System.out.println("Reversed String: " + reversedStr);
    }

    public static String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }
}