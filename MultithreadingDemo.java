import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;


public class MultithreadingDemo {

    private static int unsafeCounter = 0;
    private static int syncCounter = 0;
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);
    private static final ReentrantLock fairLock = new ReentrantLock(true);
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) throws Exception {
        System.out.println("══════════════════════════════════════════");
        System.out.println("   Multithreading & Concurrency Demo");
        System.out.println("══════════════════════════════════════════\n");

        demoThreadCreation();
        demoRaceCondition();
        demoReentrantLock();
        demoDeadlock();
        demoCountDownLatch();
        demoSemaphore();
    }

    
    static void demoThreadCreation() throws Exception {
        System.out.println("── 1. Thread Creation ──────────────────");
        Thread t1 = new Thread(() ->
            System.out.println("  [Runnable] " + Thread.currentThread().getName()));
        t1.start();
        t1.join();

        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<String> future = pool.submit(() -> "Result from Callable");
        System.out.println("  [Callable] " + future.get());
        pool.shutdown();
        System.out.println();
    }

    
    static void demoRaceCondition() throws InterruptedException {
        System.out.println("── 2. Race Condition ───────────────────");
        int iterations = 100_000;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) unsafeCounter++;
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) incrementSync();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) atomicCounter.incrementAndGet();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("  Expected : " + (threads.length * iterations));
        System.out.println("  Unsafe   : " + unsafeCounter + " (race!)");
        System.out.println("  Synced   : " + syncCounter);
        System.out.println("  Atomic   : " + atomicCounter.get());
        System.out.println();
    }

    private static synchronized void incrementSync() { syncCounter++; }

    
    static void demoReentrantLock() throws InterruptedException {
        System.out.println("── 3. ReentrantLock ────────────────────");
        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            try {
                if (fairLock.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("  " + name + " acquired lock");
                        Thread.sleep(150);
                    } finally { fairLock.unlock(); }
                } else {
                    System.out.println("  " + name + " timed out");
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
        Thread t1 = new Thread(task, "W-A");
        Thread t2 = new Thread(task, "W-B");
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println();
    }

    
    static void demoDeadlock() throws InterruptedException {
        System.out.println("── 4. Deadlock Demo ────────────────────");
        Thread d1 = new Thread(() -> {
            synchronized (LOCK_A) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                synchronized (LOCK_B) { System.out.println("  D1 got both"); }
            }
        });
        Thread d2 = new Thread(() -> {
            synchronized (LOCK_B) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                synchronized (LOCK_A) { System.out.println("  D2 got both"); }
            }
        });
        d1.setDaemon(true); d2.setDaemon(true);
        d1.start(); d2.start();
        d1.join(2000); d2.join(2000);
        if (d1.isAlive() && d2.isAlive()) {
            System.out.println("  ⚠ Deadlock! Fix: acquire locks in same order.");
        }
        System.out.println();
    }

    
    static void demoCountDownLatch() throws InterruptedException {
        System.out.println("── 5. CountDownLatch ───────────────────");
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                try { Thread.sleep((long)(Math.random() * 300)); }
                catch (InterruptedException ignored) {}
                System.out.println("  Worker-" + id + " done");
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println("  All workers finished!\n");
    }

    
    static void demoSemaphore() throws InterruptedException {
        System.out.println("── 6. Semaphore (2 permits) ────────────");
        Semaphore sem = new Semaphore(2);
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    sem.acquire();
                    System.out.println("  Task-" + id + " running");
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                } finally {
                    sem.release();
                    System.out.println("  Task-" + id + " released");
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }
}
