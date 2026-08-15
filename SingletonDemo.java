import java.io.*;

/**
 * Technical Interview Question: Design Patterns - Thread-Safe Singleton Implementations
 * 
 * Frequently asked in technical interviews:
 * 1. Double-Checked Locking (DCL) with volatile keyword
 * 2. Bill Pugh Singleton (Lazy Initialization Holder Class)
 * 3. Prevention against Reflection and Serialization breakages
 */
public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. Double-Checked Locking (DCL) Singleton Test");
        System.out.println("==================================================");

        Runnable dclTask = () -> {
            DoubleCheckedSingleton instance = DoubleCheckedSingleton.getInstance();
            System.out.println(Thread.currentThread().getName() + " -> HashCode: " + instance.hashCode());
        };

        Thread t1 = new Thread(dclTask, "Thread-1");
        Thread t2 = new Thread(dclTask, "Thread-2");
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Bill Pugh (Lazy Initialization Holder) Test");
        System.out.println("==================================================");

        Runnable billPughTask = () -> {
            BillPughSingleton instance = BillPughSingleton.getInstance();
            System.out.println(Thread.currentThread().getName() + " -> HashCode: " + instance.hashCode());
        };

        Thread t3 = new Thread(billPughTask, "Thread-3");
        Thread t4 = new Thread(billPughTask, "Thread-4");
        t3.start();
        t4.start();

        try {
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println(" 3. Enum Singleton (Most Robust & Recommended)");
        System.out.println("==================================================");
        EnumSingleton enumInstance1 = EnumSingleton.INSTANCE;
        EnumSingleton enumInstance2 = EnumSingleton.INSTANCE;

        enumInstance1.doSomething();
        System.out.println("Enum instance 1 hash: " + enumInstance1.hashCode());
        System.out.println("Enum instance 2 hash: " + enumInstance2.hashCode());
        System.out.println("Are both Enum instances equal? " + (enumInstance1 == enumInstance2));
    }
}

/**
 * 1. Double-Checked Locking Singleton Implementation
 * Uses `volatile` to prevent instruction reordering.
 */
class DoubleCheckedSingleton implements Serializable {
    private static final long serialVersionUID = 1L;

    // volatile keyword ensures changes made by one thread are visible to all other threads instantly
    private static volatile DoubleCheckedSingleton instance;

    // Private constructor prevents instantiation from outside
    private DoubleCheckedSingleton() {
        // Guard against instantiation via Reflection API
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) { // First check (no locking overhead)
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }

    // Preserve singleton property during deserialization
    protected Object readResolve() {
        return getInstance();
    }
}

/**
 * 2. Bill Pugh Singleton (Static Inner Helper Class)
 * Best practice when eager loading is not desired.
 * Thread-safe without requiring synchronized keywords.
 */
class BillPughSingleton {

    private BillPughSingleton() {}

    // Static inner class is loaded into memory only when getInstance() is called
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

/**
 * 3. Enum Singleton
 * Recommended by Joshua Bloch (Effective Java).
 * Prevents reflection and serialization issues out of the box.
 */
enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Enum Singleton performing task safely!");
    }
}
