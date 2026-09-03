package Enums;

public class EnumPolymorphismDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum Polymorphism (Constant-Specific Class Bodies) Demo ---");
        
        double x = 10.0;
        double y = 5.0;
        
        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }
}

enum Operation {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    // Abstract method must be implemented by all constants
    public abstract double apply(double x, double y);
}
