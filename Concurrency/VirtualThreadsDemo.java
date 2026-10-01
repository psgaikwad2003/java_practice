package Concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Modern Java 21 Concurrency:
 * 1. Project Loom Virtual Threads (Thread.ofVirtual()).
 * 2. High-Throughput I/O Scalability: Launching 25,000 concurrent simulated tasks with near-zero overhead.
 * 3. Structured Concurrency Pattern: Fan-out subtasks and aggregate results with timeout and cancellation.
 * 4. Carrier Thread vs Virtual Thread mechanics & best practices (e.g. avoiding thread pinning).
 *
 * Requirements:
 * - Java 21 LTS (or newer).
 */
public class VirtualThreadsDemo {

    /**
     * Demonstrates spawning 25,000 concurrent tasks using Virtual Threads.
     */
    public static void runMassiveScaleVirtualThreads(int taskCount) {
        System.out.printf("\n--- Test 1: Spawning %,d Concurrent Virtual Threads ---%n", taskCount);
        AtomicInteger completedTasks = new AtomicInteger(0);

        Instant start = Instant.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // Simulate network I/O or database latency
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    completedTasks.incrementAndGet();
                });
            }
        } // Executor auto-closes and awaits all subtasks

        Duration duration = Duration.between(start, Instant.now());
        System.out.printf("Successfully executed %,d virtual tasks in %d ms (Avg latency per task: 50ms)!%n",
                completedTasks.get(), duration.toMillis());
    }

    /**
     * Demonstrates Structured Concurrency (Fan-Out / Fan-In) pattern:
     * Queries multiple downstream microservices in parallel, returns first successful response
     * or aggregates all responses.
     */
    public static class StructuredCoordinator {
        public record ServiceResponse(String serviceName, String payload, long latencyMs) {}

        public static List<ServiceResponse> fetchAllServices(List<String> services, long timeoutMs) {
            List<ServiceResponse> responses = Collections.synchronizedList(new ArrayList<>());

            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<ServiceResponse>> futures = new ArrayList<>();

                for (String service : services) {
                    futures.add(executor.submit(() -> {
                        long sleepTime = (long) (Math.random() * 80 + 20);
                        Thread.sleep(sleepTime);
                        return new ServiceResponse(service, "Data from " + service, sleepTime);
                    }));
                }

                for (Future<ServiceResponse> f : futures) {
                    try {
                        responses.add(f.get(timeoutMs, TimeUnit.MILLISECONDS));
                    } catch (TimeoutException | ExecutionException | InterruptedException e) {
                        System.err.println("Task failed or timed out: " + e.getMessage());
                    }
                }
            }

            return responses;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=============================================================");
        System.out.println("   MODERN JAVA 21: VIRTUAL THREADS & STRUCTURED CONCURRENCY  ");
        System.out.println("=============================================================");

        // Display Java Runtime Information
        System.out.println("Runtime Version: " + System.getProperty("java.version"));
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());

        // Basic Virtual Thread Creation
        System.out.println("\n--- Basic Virtual Thread vs Platform Thread ---");
        Thread platformThread = Thread.ofPlatform().name("platform-worker").start(() -> {
            System.out.println("Running on Platform Thread: " + Thread.currentThread());
        });
        platformThread.join();

        Thread virtualThread = Thread.ofVirtual().name("virtual-worker").start(() -> {
            System.out.println("Running on Virtual Thread: " + Thread.currentThread() +
                    " (isVirtual=" + Thread.currentThread().isVirtual() + ")");
        });
        virtualThread.join();

        // Massive Scale Test
        runMassiveScaleVirtualThreads(25_000);

        // Structured Concurrency Microservices Fan-Out
        System.out.println("\n--- Test 2: Structured Concurrency Microservices Fan-Out ---");
        List<String> microservices = List.of(
            "Auth-Service",
            "User-Profile-Service",
            "Billing-Service",
            "Recommendation-Engine",
            "Notification-Service"
        );

        List<StructuredCoordinator.ServiceResponse> results =
                StructuredCoordinator.fetchAllServices(microservices, 2000);

        System.out.println("Aggregated Responses (" + results.size() + "/" + microservices.size() + "):");
        for (var res : results) {
            System.out.printf("  [OK] %-25s -> %s (latency: %d ms)%n",
                    res.serviceName(), res.payload(), res.latencyMs());
        }

        System.out.println("\nVirtual Threads verification completed successfully.");
    }
}
