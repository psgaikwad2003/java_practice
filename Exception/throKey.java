public class throKey {

    public static void main(String[] args) {
        int age = 20;

        try {
            
            if (age < 18) {
                throw new ArithmeticException("You are not eligible to vote.");
            }
            System.out.println("You are eligible to vote.");
                
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
