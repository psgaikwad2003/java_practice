
public class stringMethods {
    public static void main(String[] args) {

        String str = "Hello, Java Programming!";

        System.out.println("========== String Methods Demo ==========");
        System.out.println("Original String      : " + str);
        System.out.println("Length               : " + str.length());
        System.out.println("Uppercase            : " + str.toUpperCase());
        System.out.println("Lowercase            : " + str.toLowerCase());
        System.out.println("Trim (with spaces)   : " + "  Hello  ".trim());
        System.out.println("Replace              : " + str.replace("Java", "Python"));
        System.out.println("Contains 'Java'      : " + str.contains("Java"));
        System.out.println("Starts With 'Hello'  : " + str.startsWith("Hello"));
        System.out.println("Ends With '!'        : " + str.endsWith("!"));
        System.out.println("Index Of 'Java'      : " + str.indexOf("Java"));
        System.out.println("Substring (7 to 11)  : " + str.substring(7, 11));
        System.out.println("Char At index 1      : " + str.charAt(1));
        System.out.println("Is Empty             : " + str.isEmpty());

        
        String reversed = new StringBuilder(str).reverse().toString();
        System.out.println("Reversed             : " + reversed);

        
        String word = "madam";
        String rev = new StringBuilder(word).reverse().toString();
        System.out.println("\n\"" + word + "\" is palindrome: " + word.equals(rev));

        System.out.println("=========================================");
    }
}
