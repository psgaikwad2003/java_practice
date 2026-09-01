import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Running task asynchronously in: " + Thread.currentThread().getName());
        });

        
        future.get();
        System.out.println("Task completed.");

        
        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
            return "Hello from CompletableFuture!";
        });

        
        CompletableFuture<Integer> lengthFuture = supplyFuture.thenApply(String::length);

        System.out.println("Length of string: " + lengthFuture.get());
    }
}
