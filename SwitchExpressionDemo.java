public class SwitchExpressionDemo {
    public static void main(String[] args) {
        String day = "MONDAY";
        
        String result = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> "Let's meet!";
            case "TUESDAY", "THURSDAY", "SATURDAY" -> "No meeting.";
            case "WEDNESDAY" -> "Midweek review.";
            default -> "Invalid day.";
        };
        
        System.out.println("Action for " + day + ": " + result);
    }
}
