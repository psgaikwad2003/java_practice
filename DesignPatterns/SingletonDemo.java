import java.io.*;


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

        System.out.println("\n==================================================");
        System.out.println(" 4. High Concurrency Stress Test (10 Threads)");
        System.out.println("==================================================");
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(10);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        java.util.Set<Integer> hashCodes = java.util.Collections.synchronizedSet(new java.util.HashSet<>());

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    DoubleCheckedSingleton instance = DoubleCheckedSingleton.getInstance();
                    hashCodes.add(instance.hashCode());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        latch.countDown();
        executor.shutdown();
        try {
            executor.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Unique instance hashCodes collected across 10 threads: " + hashCodes.size());
        System.out.println("Singleton state: " + (hashCodes.size() == 1 ? "PASSED (Single Instance)" : "FAILED"));
    }
}


class DoubleCheckedSingleton implements Serializable {
    private static final long serialVersionUID = 1L;

    
    private static volatile DoubleCheckedSingleton instance;

    
    private DoubleCheckedSingleton() {
        
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) { 
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) { 
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }

    
    protected Object readResolve() {
        return getInstance();
    }
}


class BillPughSingleton {

    private BillPughSingleton() {}

    
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}


enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Enum Singleton performing task safely!");
    }
}
