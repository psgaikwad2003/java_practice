import java.util.concurrent.Phaser;


public class PhaserComplexDemo {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); 
        System.out.println("Phaser complex demo starting...");
        
        for (int i = 0; i < 3; i++) {
            phaser.register();
            final int threadId = i;
            new Thread(() -> {
                System.out.println("Thread " + threadId + " working on phase 0");
                simulateWork(500);
                System.out.println("Thread " + threadId + " completing phase 0");
                phaser.arriveAndAwaitAdvance();
                
                System.out.println("Thread " + threadId + " working on phase 1");
                simulateWork(500);
                System.out.println("Thread " + threadId + " completing phase 1");
                phaser.arriveAndDeregister();
            }).start();
        }
        
        System.out.println("Main thread advancing from phase 0 to phase 1");
        phaser.arriveAndAwaitAdvance();
        
        System.out.println("Main thread deregistering");
        phaser.arriveAndDeregister();
        System.out.println("Phaser complex demo finished.");
    }
    
    private static void simulateWork(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
