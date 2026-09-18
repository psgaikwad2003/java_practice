package DesignPatterns;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates the Proxy Design Pattern (Structural Pattern).
 * 
 * Intent:
 * Provide a surrogate or placeholder for another object to control access to it.
 * 
 * Variants demonstrated:
 * 1. Virtual Proxy (Lazy Initialization): Defers creation and loading of resource-heavy
 *    objects until explicitly needed.
 * 2. Protection Proxy (Access Control): Enforces authentication/authorization rules
 *    before delegating to the sensitive target object.
 * 3. Caching Proxy (Performance): Stores query results locally in memory to eliminate
 *    redundant expensive database or network calls.
 * 4. Dynamic Proxy (java.lang.reflect.Proxy): Runtime bytecode proxy using an InvocationHandler
 *    for cross-cutting concerns (logging, method timing, and telemetry).
 */
public class ProxyPatternDemo {

    // =========================================================================
    // 1. Virtual Proxy: Heavy Video Streaming Service
    // =========================================================================
    public interface VideoMedia {
        void play();
        String getTitle();
    }

    public static class RealHighDefVideo implements VideoMedia {
        private final String title;
        private final String fileName;

        public RealHighDefVideo(String title, String fileName) {
            this.title = title;
            this.fileName = fileName;
            loadFromDisk(); // Expensive simulated loading operation
        }

        private void loadFromDisk() {
            System.out.printf("    [Disk I/O] Loading 4K raw video buffers for '%s' (%s)...%n", title, fileName);
            try {
                Thread.sleep(80); // Simulate disk/network latency
            } catch (InterruptedException ignored) {}
            System.out.printf("    [Disk I/O] Finished buffering '%s'.%n", title);
        }

        @Override
        public void play() {
            System.out.printf("    [Playback] Playing 4K HDR stream: '%s'%n", title);
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

    public static class LazyVideoProxy implements VideoMedia {
        private final String title;
        private final String fileName;
        private RealHighDefVideo realVideo; // Instantiated only when needed

        public LazyVideoProxy(String title, String fileName) {
            this.title = title;
            this.fileName = fileName;
        }

        @Override
        public void play() {
            if (realVideo == null) {
                System.out.println("  [Lazy Proxy] Real video not loaded yet. Initializing on first playback request...");
                realVideo = new RealHighDefVideo(title, fileName);
            } else {
                System.out.println("  [Lazy Proxy] Reusing already loaded video instance.");
            }
            realVideo.play();
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

    // =========================================================================
    // 2. Protection Proxy: Document Management Service
    // =========================================================================
    public enum UserRole {
        ADMIN,
        EDITOR,
        VIEWER
    }

    public static class UserContext {
        private final String username;
        private final UserRole role;

        public UserContext(String username, UserRole role) {
            this.username = username;
            this.role = role;
        }

        public String getUsername() { return username; }
        public UserRole getRole() { return role; }
    }

    public interface DocumentService {
        String readDocument(String id);
        void updateDocument(String id, String content);
        void deleteDocument(String id);
    }

    public static class RealDocumentService implements DocumentService {
        private final Map<String, String> documents = new HashMap<>();

        public RealDocumentService() {
            documents.put("doc-101", "Confidential Financial Roadmap 2027");
            documents.put("doc-102", "Public Press Release");
        }

        @Override
        public String readDocument(String id) {
            return documents.getOrDefault(id, "[Not Found]");
        }

        @Override
        public void updateDocument(String id, String content) {
            documents.put(id, content);
            System.out.printf("    [Real Service] Document '%s' updated.%n", id);
        }

        @Override
        public void deleteDocument(String id) {
            documents.remove(id);
            System.out.printf("    [Real Service] Document '%s' deleted from storage.%n", id);
        }
    }

    public static class SecuredDocumentProxy implements DocumentService {
        private final DocumentService targetService;
        private final UserContext currentUser;

        public SecuredDocumentProxy(DocumentService targetService, UserContext user) {
            this.targetService = targetService;
            this.currentUser = user;
        }

        @Override
        public String readDocument(String id) {
            System.out.printf("  [Security Check] User '%s' (%s) requested read on '%s'. Access granted.%n",
                    currentUser.getUsername(), currentUser.getRole(), id);
            return targetService.readDocument(id);
        }

        @Override
        public void updateDocument(String id, String content) {
            if (currentUser.getRole() == UserRole.VIEWER) {
                System.out.printf("  [ACCESS DENIED] User '%s' with role %s cannot modify documents!%n",
                        currentUser.getUsername(), currentUser.getRole());
                return;
            }
            System.out.printf("  [Security Check] User '%s' (%s) permitted to edit.%n",
                    currentUser.getUsername(), currentUser.getRole());
            targetService.updateDocument(id, content);
        }

        @Override
        public void deleteDocument(String id) {
            if (currentUser.getRole() != UserRole.ADMIN) {
                System.out.printf("  [ACCESS DENIED] User '%s' with role %s cannot delete documents! Requires ADMIN.%n",
                        currentUser.getUsername(), currentUser.getRole());
                return;
            }
            System.out.printf("  [Security Check] Admin access verified for user '%s'.%n", currentUser.getUsername());
            targetService.deleteDocument(id);
        }
    }

    // =========================================================================
    // 3. Caching Proxy: Heavy Weather / Telemetry API
    // =========================================================================
    public interface WeatherService {
        String getWeatherForecast(String city);
    }

    public static class RemoteWeatherService implements WeatherService {
        @Override
        public String getWeatherForecast(String city) {
            // Simulate slow remote API query
            try {
                Thread.sleep(60);
            } catch (InterruptedException ignored) {}
            return "Forecast for " + city + ": 22°C, Sunny with mild breeze";
        }
    }

    public static class CachingWeatherProxy implements WeatherService {
        private final WeatherService remoteService;
        private final Map<String, String> cache = new HashMap<>();
        private int hits = 0;
        private int misses = 0;

        public CachingWeatherProxy(WeatherService remoteService) {
            this.remoteService = remoteService;
        }

        @Override
        public String getWeatherForecast(String city) {
            if (cache.containsKey(city)) {
                hits++;
                System.out.printf("  [Cache HIT] Retrieved forecast for '%s' from in-memory cache.%n", city);
                return cache.get(city);
            }

            misses++;
            System.out.printf("  [Cache MISS] Querying remote weather service for '%s'...%n", city);
            String result = remoteService.getWeatherForecast(city);
            cache.put(city, result);
            return result;
        }

        public void printCacheStats() {
            System.out.printf("  [Cache Stats] Hits: %d, Misses: %d, Total Entries: %d%n", 
                    hits, misses, cache.size());
        }
    }

    // =========================================================================
    // 4. Dynamic Proxy: Telemetry & Logging via java.lang.reflect.Proxy
    // =========================================================================
    public static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;

        public LoggingInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTime = System.nanoTime();
            System.out.printf("  [Dynamic Proxy Interceptor] >> Invoking: %s()%n", method.getName());

            try {
                Object result = method.invoke(target, args);
                long elapsedMicro = (System.nanoTime() - startTime) / 1000;
                System.out.printf("  [Dynamic Proxy Interceptor] << Completed %s() in %d μs%n", 
                        method.getName(), elapsedMicro);
                return result;
            } catch (Exception e) {
                System.out.printf("  [Dynamic Proxy Interceptor] !! Exception during %s(): %s%n", 
                        method.getName(), e.getMessage());
                throw e;
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T createProxy(T target, Class<T> interfaceClass) {
            return (T) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[] { interfaceClass },
                    new LoggingInvocationHandler(target)
            );
        }
    }

    // =========================================================================
    // Main Demonstration
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("            PROXY DESIGN PATTERN DEMONSTRATION           ");
        System.out.println("=========================================================\n");

        // ---------------------------------------------------------------------
        // 1. Virtual Proxy Test
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Virtual Proxy (Lazy Loading) ---");
        System.out.println("Creating lazy video proxy (object creation is instant, no heavy I/O yet)...");
        VideoMedia video = new LazyVideoProxy("Interstellar Trailer", "interstellar_4k.mkv");
        System.out.println("Video title: " + video.getTitle());

        System.out.println("\nFirst call to play():");
        video.play(); // Triggers initialization

        System.out.println("\nSecond call to play():");
        video.play(); // Uses already initialized instance

        // ---------------------------------------------------------------------
        // 2. Protection Proxy Test
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Protection Proxy (Access Control) ---");
        DocumentService realDocService = new RealDocumentService();

        UserContext aliceViewer = new UserContext("Alice", UserRole.VIEWER);
        DocumentService aliceProxy = new SecuredDocumentProxy(realDocService, aliceViewer);

        UserContext bobAdmin = new UserContext("Bob", UserRole.ADMIN);
        DocumentService bobProxy = new SecuredDocumentProxy(realDocService, bobAdmin);

        System.out.println("\nAlice reads doc-101:");
        System.out.println("  Result: " + aliceProxy.readDocument("doc-101"));

        System.out.println("\nAlice tries to update doc-101:");
        aliceProxy.updateDocument("doc-101", "Hacked content!");

        System.out.println("\nBob updates doc-101:");
        bobProxy.updateDocument("doc-101", "Updated Q1 Roadmap");

        System.out.println("\nAlice tries to delete doc-101:");
        aliceProxy.deleteDocument("doc-101");

        System.out.println("\nBob deletes doc-101:");
        bobProxy.deleteDocument("doc-101");

        // ---------------------------------------------------------------------
        // 3. Caching Proxy Test
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Caching Proxy (Performance Optimization) ---");
        CachingWeatherProxy weatherProxy = new CachingWeatherProxy(new RemoteWeatherService());

        System.out.println(weatherProxy.getWeatherForecast("London"));
        System.out.println(weatherProxy.getWeatherForecast("Tokyo"));
        System.out.println(weatherProxy.getWeatherForecast("London")); // Cached!
        System.out.println(weatherProxy.getWeatherForecast("Tokyo"));  // Cached!
        weatherProxy.printCacheStats();

        // ---------------------------------------------------------------------
        // 4. Dynamic Proxy Test
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Dynamic Proxy (Runtime Method Telemetry) ---");
        WeatherService telemetryProxy = LoggingInvocationHandler.createProxy(
                new RemoteWeatherService(),
                WeatherService.class
        );

        String forecast = telemetryProxy.getWeatherForecast("San Francisco");
        System.out.println("  Returned: " + forecast);

        System.out.println("\n=========================================================");
        System.out.println("   PROXY PATTERN DEMONSTRATION COMPLETED SUCCESSFULLY    ");
        System.out.println("=========================================================");
    }
}
