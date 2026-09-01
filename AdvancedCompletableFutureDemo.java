import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AdvancedCompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Starting Async Operations...");
        
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1000);
            System.out.println("Task 1 completed.");
            return "Advanced";
        });
        
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(500);
            System.out.println("Task 2 completed.");
            return "Concurrency";
        });
        
        
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2 + " Demo");
        
        System.out.println("Result: " + combinedFuture.get());
    }
    
    private static void simulateDelay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
