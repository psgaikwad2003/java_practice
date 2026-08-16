import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Concurrency & Asynchronous Programming in Java: CompletableFuture & ExecutorService
 * 
 * Demonstrates:
 * 1. Asynchronous execution using supplyAsync.
 * 2. Pipeline transformations using thenApply and thenCombine.
 * 3. Custom thread pool management and graceful shutdown.
 */
public class AsyncDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Java Asynchronous Pipeline with CompletableFuture");
        System.out.println("==================================================");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            // Task 1: Fetch user details asynchronously
            CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
                simulateDelay(500);
                System.out.println(Thread.currentThread().getName() + " -> Fetched User Details");
                return "User: Alex";
            }, executor);

            // Task 2: Fetch order statistics asynchronously
            CompletableFuture<Integer> orderFuture = CompletableFuture.supplyAsync(() -> {
                simulateDelay(700);
                System.out.println(Thread.currentThread().getName() + " -> Fetched Order Count");
                return 42;
            }, executor);

            // Combine both futures when ready
            CompletableFuture<String> reportFuture = userFuture.thenCombine(orderFuture, (user, count) -> 
                user + " | Total Orders Placed: " + count
            );

            // Print final consolidated result
            reportFuture.thenAccept(report -> 
                System.out.println("\nConsolidated Report: " + report)
            ).join(); // wait for completion in main

        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
