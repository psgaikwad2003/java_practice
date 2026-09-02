import java.util.concurrent.locks.StampedLock;


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
        
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y;
        
        
        if (!sl.validate(stamp)) {
            
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
