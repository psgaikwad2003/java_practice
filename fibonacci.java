// Fibonacci Series - similar pattern to array programs
public class fibonacci {
    public static void main(String[] args) {

        int n = 10; // Number of terms

        System.out.println("Fibonacci Series (first " + n + " terms):");

        int a = 0, b = 1;

        for (int i = 1; i <= n; i++) {
            System.out.print(a + " ");
            int next = a + b;
            a = b;
            b = next;
        }

        System.out.println();

        // Check if a number is Fibonacci
        int num = 21;
        if (isPerfectSquare(5 * num * num + 4) || isPerfectSquare(5 * num * num - 4)) {
            System.out.println("\n" + num + " is a Fibonacci number.");
        } else {
            System.out.println("\n" + num + " is NOT a Fibonacci number.");
        }
    }

    static boolean isPerfectSquare(int x) {
        int s = (int) Math.sqrt(x);
        return (s * s == x);
    }
}
