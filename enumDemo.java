import java.util.Optional;

/**
 * Comprehensive Demonstration of Enums in Java.
 * 
 * Enums (Enumerations) define a fixed set of named constants.
 * In Java, enums are full-fledged classes that can contain fields,
 * constructors, methods, and implement interfaces.
 */
public class enumDemo {

    // Simple Enum definition
    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    // Enum with Fields, Constructor, and Custom Methods
    public enum OrderStatus {
        PENDING(1, "Order placed, awaiting payment"),
        PROCESSING(2, "Payment confirmed, preparing item"),
        SHIPPED(3, "Item handed to courier"),
        DELIVERED(4, "Package delivered to customer"),
        CANCELLED(5, "Order cancelled");

        private final int statusCode;
        private final String description;

        // Enum constructors are implicitly private
        OrderStatus(int statusCode, String description) {
            this.statusCode = statusCode;
            this.description = description;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getDescription() {
            return description;
        }

        // Custom instance method inside Enum
        public boolean isTerminalState() {
            return this == DELIVERED || this == CANCELLED;
        }

        // Static lookup method by status code
        public static Optional<OrderStatus> fromCode(int code) {
            for (OrderStatus status : values()) {
                if (status.statusCode == code) {
                    return Optional.of(status);
                }
            }
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. Basic Enum & Switch Usage");
        System.out.println("==================================================");
        Day today = Day.FRIDAY;

        switch (today) {
            case SATURDAY:
            case SUNDAY:
                System.out.println("It's the weekend! Time to relax.");
                break;
            case FRIDAY:
                System.out.println("Friday! Almost the weekend!");
                break;
            default:
                System.out.println("Regular weekday.");
                break;
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Enum with Fields and Methods");
        System.out.println("==================================================");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.printf("Status: %-10s | Code: %d | Description: %s (Terminal: %b)%n",
                status.name(),
                status.getStatusCode(),
                status.getDescription(),
                status.isTerminalState()
            );
        }

        System.out.println("\n==================================================");
        System.out.println(" 3. Enum Built-in Methods");
        System.out.println("==================================================");
        OrderStatus currentStatus = OrderStatus.SHIPPED;

        System.out.println("Name: " + currentStatus.name());        // SHIPPED
        System.out.println("Ordinal (Index): " + currentStatus.ordinal()); // 2
        System.out.println("ValueOf: " + OrderStatus.valueOf("DELIVERED"));

        System.out.println("\n==================================================");
        System.out.println(" 4. Optional-Based Code Lookup");
        System.out.println("==================================================");
        OrderStatus.fromCode(3).ifPresent(s -> System.out.println("Code 3 found: " + s.getDescription()));
        System.out.println("Code 99 found: " + OrderStatus.fromCode(99).orElse(null));
    }
}
