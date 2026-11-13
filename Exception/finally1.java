class AutoCleanableResource implements AutoCloseable {
    private final String name;

    public AutoCleanableResource(String name) {
        this.name = name;
        System.out.println("[Resource Open] " + name + " opened.");
    }

    public void doWork() {
        System.out.println("[Resource Working] " + name + " performing I/O.");
    }

    @Override
    public void close() {
        System.out.println("[Resource Closed] " + name + " cleanly closed.");
    }
}

public class finally1 {

    public static int finallyReturnPrecedence() {
        try {
            System.out.println("Inside try: returning 100");
            return 100;
        } finally {
            System.out.println("Inside finally: executes before method returns to caller");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Classic Try-Catch-Finally Execution Flow ===");
        try {
            int result = 20 / 2;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed regardless of exception.");
        }

        System.out.println("\n=== Finally Execution During Return ===");
        int val = finallyReturnPrecedence();
        System.out.println("Caller received returned value: " + val);

        System.out.println("\n=== Modern Try-With-Resources Comparison ===");
        try (AutoCleanableResource res = new AutoCleanableResource("DatabaseConnectionPool")) {
            res.doWork();
        }
    }
}
