import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates the Builder Design Pattern (Effective Java Item 2).
 * 
 * Benefits:
 * - Solves the Telescoping Constructor anti-pattern.
 * - Guarantees immutability by omitting setters on the target class.
 * - Provides fluent, readable object construction.
 * - Enables validation prior to object instantiation.
 */
public class BuilderPatternDemo {

    // =========================================================================
    // 1. User Profile Example (Fluent Domain Object)
    // =========================================================================
    public static class User {
        // Required fields
        private final String firstName;
        private final String lastName;
        private final String email;

        // Optional fields
        private final int age;
        private final String phone;
        private final String address;
        private final boolean newsletterSubscribed;

        // Private constructor ensures construction only via Builder
        private User(Builder builder) {
            this.firstName = builder.firstName;
            this.lastName = builder.lastName;
            this.email = builder.email;
            this.age = builder.age;
            this.phone = builder.phone;
            this.address = builder.address;
            this.newsletterSubscribed = builder.newsletterSubscribed;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public boolean isNewsletterSubscribed() { return newsletterSubscribed; }

        @Override
        public String toString() {
            return String.format("User{name='%s %s', email='%s', age=%d, phone='%s', address='%s', newsletter=%b}",
                    firstName, lastName, email, age,
                    phone != null ? phone : "N/A",
                    address != null ? address : "N/A",
                    newsletterSubscribed);
        }

        // Static nested Builder class
        public static class Builder {
            // Required parameters
            private final String firstName;
            private final String lastName;
            private final String email;

            // Optional parameters with default values
            private int age = 0;
            private String phone = null;
            private String address = null;
            private boolean newsletterSubscribed = false;

            public Builder(String firstName, String lastName, String email) {
                if (firstName == null || firstName.isBlank()) {
                    throw new IllegalArgumentException("First name is required.");
                }
                if (lastName == null || lastName.isBlank()) {
                    throw new IllegalArgumentException("Last name is required.");
                }
                if (email == null || !email.contains("@")) {
                    throw new IllegalArgumentException("Valid email is required.");
                }
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
            }

            public Builder age(int age) {
                if (age < 0 || age > 150) {
                    throw new IllegalArgumentException("Invalid age: " + age);
                }
                this.age = age;
                return this;
            }

            public Builder phone(String phone) {
                this.phone = phone;
                return this;
            }

            public Builder address(String address) {
                this.address = address;
                return this;
            }

            public Builder newsletterSubscribed(boolean newsletterSubscribed) {
                this.newsletterSubscribed = newsletterSubscribed;
                return this;
            }

            public User build() {
                return new User(this);
            }
        }
    }

    // =========================================================================
    // 2. HTTP Request Example (Complex Configuration Object)
    // =========================================================================
    public static class HttpRequest {
        private final String url;
        private final String method;
        private final Map<String, String> headers;
        private final String body;
        private final int timeoutSeconds;

        private HttpRequest(HttpRequestBuilder builder) {
            this.url = builder.url;
            this.method = builder.method;
            this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
            this.body = builder.body;
            this.timeoutSeconds = builder.timeoutSeconds;
        }

        @Override
        public String toString() {
            return String.format("HttpRequest[%s %s, timeout=%ds, headers=%s, body=%s]",
                    method, url, timeoutSeconds, headers, body != null ? body : "<empty>");
        }

        public static class HttpRequestBuilder {
            private final String url;
            private String method = "GET";
            private final Map<String, String> headers = new HashMap<>();
            private String body = null;
            private int timeoutSeconds = 30;

            public HttpRequestBuilder(String url) {
                if (url == null || url.isBlank()) {
                    throw new IllegalArgumentException("URL cannot be empty.");
                }
                this.url = url;
            }

            public HttpRequestBuilder method(String method) {
                this.method = method.toUpperCase();
                return this;
            }

            public HttpRequestBuilder addHeader(String key, String value) {
                this.headers.put(key, value);
                return this;
            }

            public HttpRequestBuilder body(String body) {
                this.body = body;
                return this;
            }

            public HttpRequestBuilder timeoutSeconds(int timeoutSeconds) {
                this.timeoutSeconds = timeoutSeconds;
                return this;
            }

            public HttpRequest build() {
                return new HttpRequest(this);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("          Builder Design Pattern Demonstration    ");
        System.out.println("==================================================");

        // Constructing user with only required fields
        User basicUser = new User.Builder("Prathamesh", "Gaikwad", "psgaikwad2003@gmail.com")
                .build();
        System.out.println("Basic User:\n  " + basicUser);

        // Constructing user with all optional fields using fluent chaining
        User fullUser = new User.Builder("Alice", "Smith", "alice.smith@example.com")
                .age(28)
                .phone("+1-555-0199")
                .address("456 Innovation Way, San Jose, CA")
                .newsletterSubscribed(true)
                .build();
        System.out.println("\nFull User:\n  " + fullUser);

        // Constructing immutable HTTP Request
        HttpRequest request = new HttpRequest.HttpRequestBuilder("https://api.github.com/users")
                .method("POST")
                .addHeader("Authorization", "Bearer ghp_exampleToken123")
                .addHeader("Content-Type", "application/json")
                .body("{\"username\": \"psgaikwad2003\"}")
                .timeoutSeconds(10)
                .build();
        System.out.println("\nHTTP Request Built via Builder:\n  " + request);

        // Validation test
        System.out.println("\n=== Validation Check ===");
        try {
            new User.Builder("Bob", "Ross", "invalid-email-address").build();
        } catch (IllegalArgumentException e) {
            System.out.println("Expected Validation Caught: " + e.getMessage());
        }
    }
}
