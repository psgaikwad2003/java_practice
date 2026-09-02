import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AsyncDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Java Asynchronous Pipeline with CompletableFuture");
        System.out.println("==================================================");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            
            CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
                simulateDelay(500);
                System.out.println(Thread.currentThread().getName() + " -> Fetched User Details");
                return "User: Alex";
            }, executor);

            
            CompletableFuture<Integer> orderFuture = CompletableFuture.supplyAsync(() -> {
                simulateDelay(700);
                System.out.println(Thread.currentThread().getName() + " -> Fetched Order Count");
                return 42;
            }, executor);

            
            CompletableFuture<String> reportFuture = userFuture.thenCombine(orderFuture, (user, count) -> 
                user + " | Total Orders Placed: " + count
            );

            
            reportFuture.thenAccept(report -> 
                System.out.println("\nConsolidated Report: " + report)
            ).join(); 

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
// Updated for demonstration
