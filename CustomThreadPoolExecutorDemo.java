import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Demonstrates custom configuration of a ThreadPoolExecutor 
 * including custom rejection policies (CallerRunsPolicy).
 */
public class CustomThreadPoolExecutorDemo {
    public static void main(String[] args) {
        System.out.println("Creating custom ThreadPoolExecutor...");
        
        // 2 core threads, max 4 threads, 10 seconds keep-alive time, 
        // queue capacity of 2, and CallerRunsPolicy for rejected tasks.
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 10L, TimeUnit.SECONDS, 
            new LinkedBlockingQueue<>(2),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        for (int i = 1; i <= 8; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Executing Task " + taskId + " in " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500); // Simulate workload
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        System.out.println("All tasks submitted.");
    }
}
