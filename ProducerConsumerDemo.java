import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Concurrency Design Pattern: Producer-Consumer Pattern
 * 
 * Frequently asked in Java multithreading technical interviews.
 * Uses BlockingQueue to handle thread synchronization without explicit wait/notify.
 */
public class ProducerConsumerDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println(" Multi-Threaded Producer-Consumer Pattern");
        System.out.println("==================================================");

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Producer producing message #" + i);
                    queue.put(i); // blocks if queue is full
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ProducerThread");

        Thread consumerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    int val = queue.take(); // blocks if queue is empty
                    System.out.println("   Consumer consumed message #" + val);
                    Thread.sleep(600);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConsumerThread");

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        System.out.println("\nProducer-Consumer processing completed successfully.");
    }
}
