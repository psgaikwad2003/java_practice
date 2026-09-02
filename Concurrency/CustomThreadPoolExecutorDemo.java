import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;


public class CustomThreadPoolExecutorDemo {
    public static void main(String[] args) {
        System.out.println("Creating custom ThreadPoolExecutor...");
        
        
        
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
                    Thread.sleep(500); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        System.out.println("All tasks submitted.");
    }
}
