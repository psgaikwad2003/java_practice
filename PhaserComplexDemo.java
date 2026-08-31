import java.util.concurrent.Phaser;

/**
 * Demonstrates the usage of Phaser, a more flexible synchronization barrier 
 * compared to CyclicBarrier and CountDownLatch.
 */
public class PhaserComplexDemo {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); // Register main thread
        System.out.println("Phaser complex demo starting...");
        
        for (int i = 0; i < 3; i++) {
            phaser.register();
            final int threadId = i;
            new Thread(() -> {
                System.out.println("Thread " + threadId + " completing phase 0");
                phaser.arriveAndAwaitAdvance();
                
                System.out.println("Thread " + threadId + " completing phase 1");
                phaser.arriveAndDeregister();
            }).start();
        }
        
        System.out.println("Main thread advancing from phase 0 to phase 1");
        phaser.arriveAndAwaitAdvance();
        
        System.out.println("Main thread deregistering");
        phaser.arriveAndDeregister();
        
        System.out.println("Phaser terminated: " + phaser.isTerminated());
    }
}
