import java.util.OptionalDouble;

public class Arithmetic {

    public static OptionalDouble safeDivide(int dividend, int divisor) {
        if (divisor == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of((double) dividend / divisor);
    }

    public static int safeModulo(int dividend, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Modulo by zero undefined: " + dividend + " % 0");
        }
        return dividend % divisor;
    }

    public static void main(String[] args) {
        System.out.println("=== Arithmetic Exception & Division Nuances ===");

        int a = 10;
        int b = 0;

        // 1. Traditional try-catch
        try {
            int result = a / b;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("[Handled] Cannot divide integer by zero: " + e.getMessage());
        }

        // 2. Safe division via OptionalDouble
        OptionalDouble safe = safeDivide(10, 0);
        System.out.println("Safe divide (10, 0): " + (safe.isPresent() ? safe.getAsDouble() : "Undefined / DivByZero"));

        OptionalDouble safeSuccess = safeDivide(10, 4);
        System.out.println("Safe divide (10, 4): " + (safeSuccess.isPresent() ? safeSuccess.getAsDouble() : "Undefined"));

        // 3. Floating point IEEE-754 behavior (does NOT throw ArithmeticException)
        double floatDiv = 10.0 / 0.0;
        System.out.println("Double 10.0 / 0.0   : " + floatDiv + " (IsInfinite: " + Double.isInfinite(floatDiv) + ")");

        double nanDiv = 0.0 / 0.0;
        System.out.println("Double 0.0 / 0.0    : " + nanDiv + " (IsNaN: " + Double.isNaN(nanDiv) + ")");

        // 4. Overflow detection
        try {
            int overflow = Math.multiplyExact(Integer.MAX_VALUE, 2);
            System.out.println("Overflow: " + overflow);
        } catch (ArithmeticException e) {
            System.out.println("[Handled] Math.multiplyExact overflow detected: " + e.getMessage());
        }
    }
}
