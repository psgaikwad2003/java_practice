import java.util.Arrays;

class Calculator {

    public int add(int a, int b) {
        System.out.println("Invoked add(int, int)");
        return a + b;
    }

    public int add(int a, int b, int c) {
        System.out.println("Invoked add(int, int, int)");
        return a + b + c;
    }

    public double add(double a, double b) {
        System.out.println("Invoked add(double, double)");
        return a + b;
    }

    public double add(int a, double b) {
        System.out.println("Invoked add(int, double) [Type Promotion]");
        return a + b;
    }

    public int add(int... numbers) {
        System.out.println("Invoked add(int... numbers) [Varargs: count=" + numbers.length + "]");
        return Arrays.stream(numbers).sum();
    }

    public String add(String s1, String s2) {
        System.out.println("Invoked add(String, String)");
        return s1 + s2;
    }
}

public class methodOverloading {
    public static void main(String[] args) {
        System.out.println("=== Method Overloading & Type Promotion Demo ===");

        Calculator calc = new Calculator();

        System.out.println("Sum (10, 20)           = " + calc.add(10, 20));
        System.out.println("Sum (10, 20, 30)       = " + calc.add(10, 20, 30));
        System.out.println("Sum (10.5, 20.5)       = " + calc.add(10.5, 20.5));
        System.out.println("Sum (15, 2.5)          = " + calc.add(15, 2.5));
        System.out.println("Sum (1, 2, 3, 4, 5, 6) = " + calc.add(1, 2, 3, 4, 5, 6));
        System.out.println("Sum (\"Hello \", \"World\") = " + calc.add("Hello ", "World"));

        byte b1 = 5, b2 = 10;
        System.out.println("Sum of bytes (promoted to int) = " + calc.add(b1, b2));
    }
}
