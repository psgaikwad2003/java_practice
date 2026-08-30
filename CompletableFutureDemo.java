import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Run a task asynchronously
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Running task asynchronously in: " + Thread.currentThread().getName());
        });

        // Block and wait for the future to complete
        future.get();
        System.out.println("Task completed.");

        // Supply a result asynchronously
        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
            return "Hello from CompletableFuture!";
        });

        // Then apply a function to the result
        CompletableFuture<Integer> lengthFuture = supplyFuture.thenApply(String::length);

        System.out.println("Length of string: " + lengthFuture.get());
    }
}
