public class string {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        String str3 = str1 + " " + str2;

        System.out.println("Concatenated String: " + str3);
        System.out.println("Length of str3: " + str3.length());
        System.out.println("Character at index 4: " + str3.charAt(4));
        System.out.println("Substring from index 6 to 11: " + str3.substring(6, 11));
    }
}
