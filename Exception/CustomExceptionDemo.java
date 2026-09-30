package Exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public ResourceNotFoundException(String resourceName, Object resourceId, Throwable cause) {
        super(String.format("Resource '%s' not found with ID: %s", resourceName, resourceId), cause);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId() { return resourceId; }
}

class ValidationException extends AppException {
    private final Map<String, String> fieldErrors;

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " | Errors: " + fieldErrors;
    }
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

class FaultyResource implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new IllegalStateException("Failed to close socket during cleanup");
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
        System.out.println(" 2. Custom Exception Chaining (Preserving Root Cause)");
        System.out.println("==================================================");

        try {
            fetchUserFromDatabase(99);
        } catch (AppException e) {
            System.out.println("Caught Domain Exception: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Root Cause preserved: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
            }
        }

        System.out.println("\n==================================================");
        System.out.println(" 3. Multi-Field Validation Exception");
        System.out.println("==================================================");

        try {
            validateRegistration("", "invalid-email", -5);
        } catch (ValidationException ve) {
            System.out.println("Caught Validation Exception: " + ve.getMessage());
            System.out.println("Individual field errors:");
            ve.getFieldErrors().forEach((field, err) ->
                    System.out.printf("  - [%s]: %s%n", field, err));
        }

        System.out.println("\n==================================================");
        System.out.println(" 4. Suppressed Exceptions Demonstration");
        System.out.println("==================================================");

        try (FaultyResource res = new FaultyResource()) {
            throw new RuntimeException("Primary business logic failure");
        } catch (Exception ex) {
            System.out.println("Primary Exception: " + ex.getMessage());
            for (Throwable suppressed : ex.getSuppressed()) {
                System.out.println("  Suppressed Exception: " + suppressed.getMessage());
            }
        }
    }

    private static void fetchUserFromDatabase(int userId) {
        try {
            throw new NullPointerException("Database cursor returned null row");
        } catch (Exception cause) {
            // Pass cause so exception chaining is properly preserved
            throw new ResourceNotFoundException("User", userId, cause);
        }
    }

    private static void validateRegistration(String username, String email, int age) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (username == null || username.isBlank()) {
            errors.put("username", "Username cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            errors.put("email", "Must be a valid email format");
        }
        if (age < 0 || age > 150) {
            errors.put("age", "Age must be between 0 and 150");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("User registration input validation failed", errors);
        }
    }
}
