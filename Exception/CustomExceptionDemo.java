package Exception;


class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ResourceNotFoundException extends AppException {
    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("Resource '%s' not found with ID: %s", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId() { return resourceId; }
}

class DatabaseConnection implements AutoCloseable {
    public void executeQuery(String query) {
        System.out.println("Executing SQL Query: " + query);
    }

    @Override
    public void close() {
        System.out.println("DatabaseConnection closed automatically via AutoCloseable.");
    }
}

public class CustomExceptionDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. AutoCloseable & Try-With-Resources Test");
        System.out.println("==================================================");

        try (DatabaseConnection db = new DatabaseConnection()) {
            db.executeQuery("SELECT * FROM users WHERE id = 42");
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Custom Exception Chaining");
        System.out.println("==================================================");

        try {
            fetchUserFromDatabase(99);
        } catch (AppException e) {
            System.err.println("Caught Domain Exception: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Root Cause: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
            }
        }
    }

    private static void fetchUserFromDatabase(int userId) {
        try {
            
            throw new NullPointerException("Database cursor returned null row");
        } catch (Exception cause) {
            throw new ResourceNotFoundException("User", userId);
        }
    }
}
