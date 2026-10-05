import java.util.*;

public class ChainOfResponsibilityDemo {

    public static class HttpRequest {
        private final String path;
        private final String token;
        private final String role;
        private final String body;
        private final int requestCount;

        public HttpRequest(String path, String token, String role, String body, int requestCount) {
            this.path = path;
            this.token = token;
            this.role = role;
            this.body = body;
            this.requestCount = requestCount;
        }

        public String getPath() { return path; }
        public String getToken() { return token; }
        public String getRole() { return role; }
        public String getBody() { return body; }
        public int getRequestCount() { return requestCount; }
    }

    public abstract static class MiddlewareHandler {
        private MiddlewareHandler next;

        public static MiddlewareHandler link(MiddlewareHandler first, MiddlewareHandler... chain) {
            MiddlewareHandler head = first;
            for (MiddlewareHandler nextInChain : chain) {
                head.next = nextInChain;
                head = nextInChain;
            }
            return first;
        }

        public abstract boolean process(HttpRequest request);

        protected boolean processNext(HttpRequest request) {
            if (next == null) {
                return true;
            }
            return next.process(request);
        }
    }

    public static class AuthenticationMiddleware extends MiddlewareHandler {
        @Override
        public boolean process(HttpRequest request) {
            System.out.println("  [AuthCheck] Checking token for path: " + request.getPath());
            if (request.getToken() == null || !request.getToken().startsWith("Bearer valid_")) {
                System.out.println("  [AuthCheck] FAILED: Missing or invalid authorization token.");
                return false;
            }
            System.out.println("  [AuthCheck] PASSED: User authenticated.");
            return processNext(request);
        }
    }

    public static class RateLimitingMiddleware extends MiddlewareHandler {
        private final int maxAllowedPerMinute;

        public RateLimitingMiddleware(int maxAllowedPerMinute) {
            this.maxAllowedPerMinute = maxAllowedPerMinute;
        }

        @Override
        public boolean process(HttpRequest request) {
            System.out.println("  [RateLimit] Checking client request count (" + request.getRequestCount() + " req/min)");
            if (request.getRequestCount() > maxAllowedPerMinute) {
                System.out.println("  [RateLimit] FAILED: 429 Too Many Requests. Limit is " + maxAllowedPerMinute);
                return false;
            }
            System.out.println("  [RateLimit] PASSED: Within rate limit.");
            return processNext(request);
        }
    }

    public static class RoleAuthorizationMiddleware extends MiddlewareHandler {
        private final String requiredRole;

        public RoleAuthorizationMiddleware(String requiredRole) {
            this.requiredRole = requiredRole;
        }

        @Override
        public boolean process(HttpRequest request) {
            System.out.println("  [RoleCheck] Checking if user has required role: " + requiredRole);
            if (!requiredRole.equalsIgnoreCase(request.getRole())) {
                System.out.println("  [RoleCheck] FAILED: 403 Forbidden. User has role '" + request.getRole() + "', needed '" + requiredRole + "'.");
                return false;
            }
            System.out.println("  [RoleCheck] PASSED: User has sufficient privileges.");
            return processNext(request);
        }
    }

    public static class PayloadValidationMiddleware extends MiddlewareHandler {
        @Override
        public boolean process(HttpRequest request) {
            System.out.println("  [Validation] Validating request body...");
            if (request.getBody() == null || request.getBody().trim().isEmpty()) {
                System.out.println("  [Validation] FAILED: 400 Bad Request. Body cannot be empty.");
                return false;
            }
            System.out.println("  [Validation] PASSED: Request payload is valid.");
            return processNext(request);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CHAIN OF RESPONSIBILITY PATTERN DEMO     ");
        System.out.println("==========================================");

        MiddlewareHandler pipeline = MiddlewareHandler.link(
            new AuthenticationMiddleware(),
            new RateLimitingMiddleware(100),
            new RoleAuthorizationMiddleware("ADMIN"),
            new PayloadValidationMiddleware()
        );

        System.out.println("\n--- Case 1: Perfectly Valid Admin Request ---");
        HttpRequest req1 = new HttpRequest("/api/v1/config", "Bearer valid_secret_xyz", "ADMIN", "{\"setting\":\"enable_cache\"}", 45);
        boolean res1 = pipeline.process(req1);
        System.out.println(">>> Pipeline Result: " + (res1 ? "200 OK (Processed)" : "Rejected"));

        System.out.println("\n--- Case 2: Invalid Auth Token ---");
        HttpRequest req2 = new HttpRequest("/api/v1/config", "invalid_token", "ADMIN", "{\"setting\":\"enable_cache\"}", 10);
        boolean res2 = pipeline.process(req2);
        System.out.println(">>> Pipeline Result: " + (res2 ? "200 OK (Processed)" : "Rejected"));

        System.out.println("\n--- Case 3: Exceeded Rate Limit ---");
        HttpRequest req3 = new HttpRequest("/api/v1/config", "Bearer valid_secret_xyz", "ADMIN", "{\"setting\":\"enable_cache\"}", 120);
        boolean res3 = pipeline.process(req3);
        System.out.println(">>> Pipeline Result: " + (res3 ? "200 OK (Processed)" : "Rejected"));

        System.out.println("\n--- Case 4: Non-Admin Accessing Admin Route ---");
        HttpRequest req4 = new HttpRequest("/api/v1/config", "Bearer valid_secret_xyz", "GUEST", "{\"setting\":\"enable_cache\"}", 25);
        boolean res4 = pipeline.process(req4);
        System.out.println(">>> Pipeline Result: " + (res4 ? "200 OK (Processed)" : "Rejected"));

        System.out.println("\n--- Case 5: Empty Body Payload ---");
        HttpRequest req5 = new HttpRequest("/api/v1/config", "Bearer valid_secret_xyz", "ADMIN", "   ", 5);
        boolean res5 = pipeline.process(req5);
        System.out.println(">>> Pipeline Result: " + (res5 ? "200 OK (Processed)" : "Rejected"));

        System.out.println("\nAll Chain of Responsibility demonstrations completed successfully.");
    }
}
