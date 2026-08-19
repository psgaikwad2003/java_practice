public class EnumBasicsDemo {

    enum TrafficLight {
        RED, YELLOW, GREEN
    }

    enum PizzaSize {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. Creating and Printing Enum Constants");
        System.out.println("==================================================");
        TrafficLight light = TrafficLight.RED;
        System.out.println("Current light: " + light);
        System.out.println("Name: " + light.name());
        System.out.println("Ordinal (index): " + light.ordinal());

        System.out.println("\n==================================================");
        System.out.println(" 2. Iterating Over All Enum Constants (values())");
        System.out.println("==================================================");

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

        TrafficLight a = TrafficLight.RED;
        TrafficLight b = TrafficLight.RED;
        TrafficLight c = TrafficLight.GREEN;

        System.out.println("RED == RED   : " + (a == b));
        System.out.println("RED == GREEN : " + (a == c));
        System.out.println("RED.equals(RED): " + a.equals(b));
        System.out.println("null == RED  : " + (null == a));

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
