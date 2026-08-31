import java.util.concurrent.locks.StampedLock;

/**
 * Demonstrates the use of StampedLock, providing optimistic read locking 
 * for improved performance over standard ReadWriteLocks.
 */
public class StampedLockDemo {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    public void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        // Optimistic read
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y;
        
        // Validate if there was any write lock acquired during the read
        if (!sl.validate(stamp)) {
            // Fallback to pessimistic read lock
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    public static void main(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        System.out.println("Initial Distance: " + demo.distanceFromOrigin());
        
        demo.move(3.0, 4.0);
        System.out.println("Distance after move (3.0, 4.0): " + demo.distanceFromOrigin());
    }
}
