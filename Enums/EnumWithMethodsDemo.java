public class EnumWithMethodsDemo {


    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS(4.869e+24, 6.0518e6),
        EARTH(5.976e+24, 6.37814e6),
        MARS(6.421e+23, 3.3972e6),
        JUPITER(1.9e+27, 7.1492e7),
        SATURN(5.688e+26, 6.0268e7),
        URANUS(8.686e+25, 2.5559e7),
        NEPTUNE(1.024e+26, 2.4746e7);

        private final double mass;
        private final double radius;


        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }


        private static final double G = 6.67300E-11;


        public double surfaceGravity() {
            return G * mass / (radius * radius);
        }


        public double surfaceWeight(double earthWeight) {
            return earthWeight * surfaceGravity() / EARTH.surfaceGravity();
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    enum Operation {
        ADD("+") {
            @Override
            public double apply(double a, double b) { return a + b; }
        },
        SUBTRACT("-") {
            @Override
            public double apply(double a, double b) { return a - b; }
        },
        MULTIPLY("*") {
            @Override
            public double apply(double a, double b) { return a * b; }
        },
        DIVIDE("/") {
            @Override
            public double apply(double a, double b) {
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                return a / b;
            }
        };

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public abstract double apply(double a, double b);

        @Override
        public String toString() {
            return name() + " (" + symbol + ")";
        }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. Enum with Fields — Planet Surface Weight");
        System.out.println("==================================================");
        double earthWeight = 75.0;
        System.out.printf("Your weight on Earth: %.2f kg%n%n", earthWeight);

        for (Planet planet : Planet.values()) {
            System.out.printf("  %-8s -> Gravity: %.2f m/s² | Weight: %.2f kg%n",
                planet, planet.surfaceGravity(), planet.surfaceWeight(earthWeight));
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Enum with Abstract Methods — Calculator");
        System.out.println("==================================================");
        double x = 20.0, y = 6.0;

        for (Operation op : Operation.values()) {
            double result = op.apply(x, y);
            System.out.printf("  %s : %.1f %s %.1f = %.2f%n",
                op, x, op.getSymbol(), y, result);
        }

        System.out.println("\n==================================================");
        System.out.println(" 3. Using Specific Operations");
        System.out.println("==================================================");
        Operation myOp = Operation.MULTIPLY;
        System.out.println("Selected operation: " + myOp);
        System.out.println("Result: " + myOp.apply(7, 8));


        try {
            System.out.println("10 / 0 = " + Operation.DIVIDE.apply(10, 0));
        } catch (ArithmeticException e) {
            System.out.println("10 / 0 = ERROR: " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println(" KEY TAKEAWAYS:");
        System.out.println("==================================================");
        System.out.println("1. Enums can have fields, constructors, and methods.");
        System.out.println("2. Enum constructors are ALWAYS private (implicit).");
        System.out.println("3. Abstract methods force EACH constant to implement.");
        System.out.println("4. toString() can be overridden for custom display.");
        System.out.println("5. Each enum constant is an INSTANCE of the enum class.");
    }
}
