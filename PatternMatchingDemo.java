public class PatternMatchingDemo {
    public static void main(String[] args) {
        Object obj = "This is a string";
        
        if (obj instanceof String s) {
            System.out.println("The length of the string is: " + s.length());
        } else {
            System.out.println("Not a string");
        }
    }
}
