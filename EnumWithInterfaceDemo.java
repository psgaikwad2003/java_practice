/**
 * Enum Implementing Interfaces Demo (Medium - Level 3)
 * 
 * Topics Covered:
 * - Enum implementing one or more interfaces
 * - Polymorphism with enums (using interface reference)
 * - Enum constants as strategy objects
 * - Comparable behavior with enums (enums implement Comparable by default)
 */
public class EnumWithInterfaceDemo {

    // Interface for any entity that can be logged
    interface Loggable {
        String getLogPrefix();
        default void log(String message) {
            System.out.println("[" + getLogPrefix() + "] " + message);
        }
    }

    // Interface for anything that can provide a display label
    interface Displayable {
        String getDisplayLabel();
    }

    // Enum implementing MULTIPLE interfaces
    enum LogLevel implements Loggable, Displayable {
        DEBUG("DBG", "Debug", "\u001B[36m"),       // Cyan
        INFO("INF", "Information", "\u001B[32m"),   // Green
        WARNING("WRN", "Warning", "\u001B[33m"),    // Yellow
        ERROR("ERR", "Error", "\u001B[31m"),        // Red
        FATAL("FTL", "Fatal Error", "\u001B[35m");  // Magenta

        private final String prefix;
        private final String displayLabel;
        private final String colorCode;

        LogLevel(String prefix, String displayLabel, String colorCode) {
            this.prefix = prefix;
            this.displayLabel = displayLabel;
            this.colorCode = colorCode;
        }

        // From Loggable interface
        @Override
        public String getLogPrefix() {
            return prefix;
        }

        // From Displayable interface
        @Override
        public String getDisplayLabel() {
            return displayLabel;
        }

        // Check if this log level is severe enough
        public boolean isSevere() {
            return this.ordinal() >= WARNING.ordinal();
        }

        // Get color code for terminal output
        public String getColorCode() {
            return colorCode;
        }
    }

    // Interface for payment processing
    interface PaymentProcessor {
        boolean processPayment(double amount);
        double getFeePercentage();
        default double calculateFee(double amount) {
            return amount * getFeePercentage() / 100.0;
        }
    }

    // Enum as a Strategy — each constant IS a strategy implementation
    enum PaymentMethod implements PaymentProcessor {
        CREDIT_CARD {
            @Override
            public boolean processPayment(double amount) {
                System.out.printf("  Processing $%.2f via Credit Card...%n", amount);
                return amount <= 10000;
            }
            @Override
            public double getFeePercentage() { return 2.5; }
        },
        DEBIT_CARD {
            @Override
            public boolean processPayment(double amount) {
                System.out.printf("  Processing $%.2f via Debit Card...%n", amount);
                return amount <= 5000;
            }
            @Override
            public double getFeePercentage() { return 1.0; }
        },
        UPI {
            @Override
            public boolean processPayment(double amount) {
                System.out.printf("  Processing $%.2f via UPI...%n", amount);
                return amount <= 100000;
            }
            @Override
            public double getFeePercentage() { return 0.0; }
        },
        NET_BANKING {
            @Override
            public boolean processPayment(double amount) {
                System.out.printf("  Processing $%.2f via Net Banking...%n", amount);
                return amount <= 50000;
            }
            @Override
            public double getFeePercentage() { return 1.5; }
        };
    }

    // Simulated logger that accepts any Loggable
    static void performLogging(Loggable logger, String message) {
        logger.log(message);
    }

    // Simulated payment checkout that accepts any PaymentProcessor
    static void checkout(PaymentProcessor processor, double amount) {
        double fee = processor.calculateFee(amount);
        double total = amount + fee;
        System.out.printf("  Amount: $%.2f | Fee: $%.2f | Total: $%.2f%n", amount, fee, total);
        boolean success = processor.processPayment(total);
        System.out.println("  Result: " + (success ? "SUCCESS" : "FAILED - Limit Exceeded"));
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. Enum Implementing Loggable & Displayable");
        System.out.println("==================================================");
        for (LogLevel level : LogLevel.values()) {
            System.out.printf("  %-8s | Display: %-15s | Prefix: %s | Severe: %b%n",
                level.name(), level.getDisplayLabel(), level.getLogPrefix(), level.isSevere());
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Polymorphism — Enum as Interface Reference");
        System.out.println("==================================================");
        // Passing enum constants as Loggable interface references
        Loggable logger = LogLevel.INFO;
        logger.log("Application started successfully");

        performLogging(LogLevel.WARNING, "Memory usage is high");
        performLogging(LogLevel.ERROR, "Database connection failed");

        System.out.println("\n==================================================");
        System.out.println(" 3. Enum as Strategy Pattern (PaymentProcessor)");
        System.out.println("==================================================");
        double orderAmount = 499.99;
        System.out.println("Order amount: $" + orderAmount + "\n");

        for (PaymentMethod method : PaymentMethod.values()) {
            System.out.println("--- " + method.name() + " ---");
            checkout(method, orderAmount);
            System.out.println();
        }

        System.out.println("==================================================");
        System.out.println(" 4. Comparable — Enums are Naturally Ordered");
        System.out.println("==================================================");
        LogLevel a = LogLevel.DEBUG;
        LogLevel b = LogLevel.ERROR;

        int comparison = a.compareTo(b);
        System.out.printf("  %s.compareTo(%s) = %d%n", a, b, comparison);
        System.out.println("  " + a + " comes before " + b + ": " + (comparison < 0));

        System.out.println("\n==================================================");
        System.out.println(" KEY TAKEAWAYS:");
        System.out.println("==================================================");
        System.out.println("1. Enums can implement one or more interfaces.");
        System.out.println("2. Each constant can be passed as an interface ref.");
        System.out.println("3. Enables Strategy Pattern — each constant is a strategy.");
        System.out.println("4. Enums already implement Comparable (by ordinal).");
        System.out.println("5. default methods in interfaces work with enums too.");
    }
}
