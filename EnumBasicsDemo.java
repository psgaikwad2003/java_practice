/**
 * Enum Basics Demo (Easy - Level 1)
 * 
 * Topics Covered:
 * - Defining a simple enum
 * - Using values(), valueOf(), name(), ordinal()
 * - Iterating over all enum constants
 * - Comparing enums with == and .equals()
 * - Using enum in if-else and switch
 */
public class EnumBasicsDemo {

    // Simple enum representing traffic light colors
    enum TrafficLight {
        RED, YELLOW, GREEN
    }

    // Enum representing pizza sizes
    enum PizzaSize {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. Creating and Printing Enum Constants");
        System.out.println("==================================================");
        TrafficLight light = TrafficLight.RED;
        System.out.println("Current light: " + light);           // RED
        System.out.println("Name: " + light.name());             // RED
        System.out.println("Ordinal (index): " + light.ordinal()); // 0

        System.out.println("\n==================================================");
        System.out.println(" 2. Iterating Over All Enum Constants (values())");
        System.out.println("==================================================");
        // values() returns an array of all enum constants
        System.out.println("All Traffic Lights:");
        for (TrafficLight t : TrafficLight.values()) {
            System.out.printf("  %s -> ordinal: %d%n", t.name(), t.ordinal());
        }

        System.out.println("\nAll Pizza Sizes:");
        for (PizzaSize size : PizzaSize.values()) {
            System.out.printf("  %s -> ordinal: %d%n", size.name(), size.ordinal());
        }

        System.out.println("\n==================================================");
        System.out.println(" 3. valueOf() - String to Enum Conversion");
        System.out.println("==================================================");
        // valueOf() converts a string to an enum constant (case-sensitive)
        TrafficLight fromString = TrafficLight.valueOf("GREEN");
        System.out.println("valueOf(\"GREEN\"): " + fromString);

        try {
            TrafficLight invalid = TrafficLight.valueOf("BLUE");
        } catch (IllegalArgumentException e) {
            System.out.println("valueOf(\"BLUE\"): ERROR - " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println(" 4. Comparing Enums (== vs .equals())");
        System.out.println("==================================================");
        // Both == and .equals() work for enums, but == is preferred (null-safe)
        TrafficLight a = TrafficLight.RED;
        TrafficLight b = TrafficLight.RED;
        TrafficLight c = TrafficLight.GREEN;

        System.out.println("RED == RED   : " + (a == b));        // true
        System.out.println("RED == GREEN : " + (a == c));        // false
        System.out.println("RED.equals(RED): " + a.equals(b));   // true
        System.out.println("null == RED  : " + (null == a));     // false (no NPE!)
        // null.equals(RED) would throw NullPointerException — that's why == is safer

        System.out.println("\n==================================================");
        System.out.println(" 5. Enum in Switch Statement");
        System.out.println("==================================================");
        PizzaSize myPizza = PizzaSize.LARGE;

        switch (myPizza) {
            case SMALL:
                System.out.println("Small pizza: $8.99");
                break;
            case MEDIUM:
                System.out.println("Medium pizza: $11.99");
                break;
            case LARGE:
                System.out.println("Large pizza: $14.99");
                break;
            case EXTRA_LARGE:
                System.out.println("Extra Large pizza: $17.99");
                break;
        }

        System.out.println("\n==================================================");
        System.out.println(" 6. Enum in If-Else");
        System.out.println("==================================================");
        TrafficLight current = TrafficLight.YELLOW;

        if (current == TrafficLight.RED) {
            System.out.println("STOP! Red light.");
        } else if (current == TrafficLight.YELLOW) {
            System.out.println("CAUTION! Yellow light - prepare to stop.");
        } else if (current == TrafficLight.GREEN) {
            System.out.println("GO! Green light.");
        }

        System.out.println("\n==================================================");
        System.out.println(" KEY TAKEAWAYS:");
        System.out.println("==================================================");
        System.out.println("1. Enums define a FIXED set of named constants.");
        System.out.println("2. values()  -> returns array of all constants.");
        System.out.println("3. valueOf() -> converts String to enum (case-sensitive).");
        System.out.println("4. name()    -> returns the constant name as String.");
        System.out.println("5. ordinal() -> returns the position index (0-based).");
        System.out.println("6. Use == for comparison (null-safe, preferred).");
    }
}
