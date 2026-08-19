import java.util.EnumSet;
import java.util.EnumMap;
import java.util.Map;

public class EnumSetMapDemo {

    enum Permission { READ, WRITE, EXECUTE, DELETE, ADMIN }

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

    enum HttpStatus {
        OK(200, "Success"), CREATED(201, "Created"),
        BAD_REQUEST(400, "Bad Request"), NOT_FOUND(404, "Not Found"),
        INTERNAL_ERROR(500, "Server Error");

        private final int code;
        private final String msg;
        HttpStatus(int code, String msg) { this.code = code; this.msg = msg; }
        public int getCode() { return code; }
        public String getMsg() { return msg; }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. EnumSet — Creating Sets of Enums");
        System.out.println("==================================================");

        EnumSet<Permission> basic = EnumSet.of(Permission.READ, Permission.WRITE);
        System.out.println("Basic perms:  " + basic);

        EnumSet<Permission> all = EnumSet.allOf(Permission.class);
        System.out.println("All perms:    " + all);

        EnumSet<Permission> none = EnumSet.noneOf(Permission.class);
        System.out.println("No perms:     " + none);

        EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        System.out.println("Weekdays:     " + weekdays);

        EnumSet<Day> weekends = EnumSet.complementOf(weekdays);
        System.out.println("Weekends:     " + weekends);

        System.out.println("\n==================================================");
        System.out.println(" 2. EnumSet — Permission System");
        System.out.println("==================================================");

        EnumSet<Permission> guest = EnumSet.of(Permission.READ);
        EnumSet<Permission> user = EnumSet.of(Permission.READ, Permission.WRITE);
        EnumSet<Permission> admin = EnumSet.allOf(Permission.class);

        System.out.println("Guest: " + guest + " | Can WRITE? " + guest.contains(Permission.WRITE));
        System.out.println("User:  " + user + " | Can WRITE? " + user.contains(Permission.WRITE));
        System.out.println("Admin has all user perms? " + admin.containsAll(user));

        System.out.println("\n==================================================");
        System.out.println(" 3. EnumMap — Weekly Schedule");
        System.out.println("==================================================");

        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        schedule.put(Day.MONDAY, "Standup + coding");
        schedule.put(Day.TUESDAY, "Feature dev");
        schedule.put(Day.WEDNESDAY, "Code review");
        schedule.put(Day.THURSDAY, "Sprint planning");
        schedule.put(Day.FRIDAY, "Deploy + retro");
        schedule.put(Day.SATURDAY, "Side project");
        schedule.put(Day.SUNDAY, "Rest");

        for (Map.Entry<Day, String> e : schedule.entrySet()) {
            System.out.printf("  %-10s -> %s%n", e.getKey(), e.getValue());
        }

        System.out.println("\n==================================================");
        System.out.println(" 4. EnumMap — HTTP Status Counters");
        System.out.println("==================================================");

        EnumMap<HttpStatus, Integer> counters = new EnumMap<>(HttpStatus.class);
        for (HttpStatus s : HttpStatus.values()) counters.put(s, 0);

        HttpStatus[] responses = { HttpStatus.OK, HttpStatus.OK, HttpStatus.NOT_FOUND,
            HttpStatus.OK, HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_ERROR, HttpStatus.OK };

        for (HttpStatus s : responses) counters.put(s, counters.get(s) + 1);

        for (Map.Entry<HttpStatus, Integer> e : counters.entrySet()) {
            if (e.getValue() > 0)
                System.out.printf("  %d %-15s : %d hits%n", e.getKey().getCode(), e.getKey().getMsg(), e.getValue());
        }

        System.out.println("\n==================================================");
        System.out.println(" 5. EnumSet Operations (Union, Intersection, Diff)");
        System.out.println("==================================================");

        EnumSet<Permission> setA = EnumSet.of(Permission.READ, Permission.WRITE, Permission.EXECUTE);
        EnumSet<Permission> setB = EnumSet.of(Permission.WRITE, Permission.DELETE, Permission.ADMIN);
        System.out.println("Set A: " + setA);
        System.out.println("Set B: " + setB);

        EnumSet<Permission> union = EnumSet.copyOf(setA); union.addAll(setB);
        EnumSet<Permission> inter = EnumSet.copyOf(setA); inter.retainAll(setB);
        EnumSet<Permission> diff = EnumSet.copyOf(setA); diff.removeAll(setB);

        System.out.println("Union (A+B):  " + union);
        System.out.println("Intersect:    " + inter);
        System.out.println("Diff (A-B):   " + diff);

        System.out.println("\n==================================================");
        System.out.println(" KEY TAKEAWAYS:");
        System.out.println("==================================================");
        System.out.println("1. EnumSet uses bit vectors — very fast.");
        System.out.println("2. EnumMap uses ordinal as array index — faster than HashMap.");
        System.out.println("3. Always prefer EnumSet/EnumMap for enum types.");
        System.out.println("4. EnumSet supports union, intersection, difference.");
    }
}
