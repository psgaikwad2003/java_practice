package Enums;

public class EnumSwitchCaseDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum in Switch Case / Expressions Demo ---");
        
        Day day = Day.WEDNESDAY;
        
        // Traditional Switch Statement
        System.out.println("Traditional Switch:");
        switch (day) {
            case MONDAY:
            case FRIDAY:
            case SUNDAY:
                System.out.println(day + " is a busy day.");
                break;
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                System.out.println(day + " is a normal day.");
                break;
            case SATURDAY:
                System.out.println(day + " is a rest day.");
                break;
        }

        // Enhanced Switch Expression (Java 14+)
        System.out.println("\nEnhanced Switch Expression:");
        String typeOfDay = switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
        System.out.println(day + " is a " + typeOfDay);
    }
}

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
