import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

public class ThreadSafetyDemo {

    // 1. Race condition without synchronization
    static int unsafeCounter = 0;

    static void incrementUnsafe() {
        unsafeCounter++;
    }

    // 2. Synchronized method
    static int safeCounter = 0;

    static synchronized void incrementSafe() {
        safeCounter++;
    }

    // 3. AtomicInteger – lock-free thread safety
    static AtomicInteger atomicCounter = new AtomicInteger(0);

    // 4. Thread-safe bank account using synchronized block
    static class BankAccount {
        private double balance;
        private final String owner;
        private final List<String> log = Collections.synchronizedList(new ArrayList<>());

        BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
        }

        synchronized void deposit(double amount) {
            balance += amount;
            log.add(String.format("DEPOSIT  +%.2f -> balance: %.2f [%s]",
                    amount, balance, Thread.currentThread().getName()));
        }

        synchronized boolean withdraw(double amount) {
            if (balance < amount) {
                log.add(String.format("FAILED   -%.2f (insufficient) [%s]",
                        amount, Thread.currentThread().getName()));
                return false;
            }
            balance -= amount;
            log.add(String.format("WITHDRAW -%.2f -> balance: %.2f [%s]",
                    amount, balance, Thread.currentThread().getName()));
            return true;
        }

        synchronized double getBalance() { return balance; }
        String getOwner() { return owner; }
        List<String> getLog() { return log; }
    }

    // 5. Deadlock demonstration (safe – uses tryLock pattern to avoid it)
    static class DeadlockSafeTransfer {
        private final ReentrantLock lockA = new ReentrantLock();
        private final ReentrantLock lockB = new ReentrantLock();
        private double accountA = 1000.0;
        private double accountB = 1000.0;

        boolean transfer(boolean fromAToB, double amount) throws InterruptedException {
            ReentrantLock first = fromAToB ? lockA : lockB;
            ReentrantLock second = fromAToB ? lockB : lockA;

            if (first.tryLock(50, TimeUnit.MILLISECONDS)) {
                try {
                    if (second.tryLock(50, TimeUnit.MILLISECONDS)) {
                        try {
                            if (fromAToB) {
                                accountA -= amount;
                                accountB += amount;
                            } else {
                                accountB -= amount;
                                accountA += amount;
                            }
                            return true;
                        } finally { second.unlock(); }
                    }
                } finally { first.unlock(); }
            }
            return false; // could not acquire locks, retry logic would go here
        }

        double getAccountA() { return accountA; }
        double getAccountB() { return accountB; }
    }

    // 6. Producer-Consumer with BlockingQueue
    static class MessageProducer implements Runnable {
        private final BlockingQueue<String> queue;
        private final String name;

        MessageProducer(BlockingQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 3; i++) {
                    String msg = name + "-msg-" + i;
                    queue.put(msg);
                    System.out.println("  [PRODUCER-" + name + "] Sent: " + msg);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class MessageConsumer implements Runnable {
        private final BlockingQueue<String> queue;
        private final int messagesToConsume;

        MessageConsumer(BlockingQueue<String> queue, int messagesToConsume) {
            this.queue = queue;
            this.messagesToConsume = messagesToConsume;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < messagesToConsume; i++) {
                    String msg = queue.poll(2, TimeUnit.SECONDS);
                    if (msg != null) System.out.println("  [CONSUMER] Received: " + msg);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Safety Demo ===\n");

        // 1. Race condition
        System.out.println("--- Race Condition Demo ---");
        unsafeCounter = 0;
        safeCounter = 0;
        atomicCounter.set(0);

        int threadCount = 10, incrementsPerThread = 1000;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    incrementUnsafe();
                    incrementSafe();
                    atomicCounter.incrementAndGet();
                }
            });
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int expected = threadCount * incrementsPerThread;
        System.out.println("  Expected value:      " + expected);
        System.out.println("  Unsafe counter:      " + unsafeCounter + (unsafeCounter == expected ? " (OK)" : " (RACE CONDITION!)"));
        System.out.println("  Synchronized:        " + safeCounter);
        System.out.println("  AtomicInteger:       " + atomicCounter.get());

        // 2. Thread-safe bank account
        System.out.println("\n--- Synchronized Bank Account ---");
        BankAccount account = new BankAccount("Alice", 500.0);
        Thread[] bankThreads = new Thread[6];
        bankThreads[0] = new Thread(() -> account.deposit(200), "T-Deposit1");
        bankThreads[1] = new Thread(() -> account.withdraw(100), "T-Withdraw1");
        bankThreads[2] = new Thread(() -> account.deposit(300), "T-Deposit2");
        bankThreads[3] = new Thread(() -> account.withdraw(800), "T-Withdraw2");
        bankThreads[4] = new Thread(() -> account.deposit(50), "T-Deposit3");
        bankThreads[5] = new Thread(() -> account.withdraw(150), "T-Withdraw3");
        for (Thread t : bankThreads) t.start();
        for (Thread t : bankThreads) t.join();
        account.getLog().forEach(entry -> System.out.println("  " + entry));
        System.out.printf("  Final balance: %.2f%n", account.getBalance());

        // 3. Deadlock-safe transfer
        System.out.println("\n--- Deadlock-Safe Transfer (tryLock) ---");
        DeadlockSafeTransfer transfer = new DeadlockSafeTransfer();
        Thread t1 = new Thread(() -> {
            try { transfer.transfer(true, 100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "TransferAtoB");
        Thread t2 = new Thread(() -> {
            try { transfer.transfer(false, 50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "TransferBtoA");
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.printf("  Account A: %.2f%n", transfer.getAccountA());
        System.out.printf("  Account B: %.2f%n", transfer.getAccountB());

        // 4. Producer-Consumer with BlockingQueue
        System.out.println("\n--- Producer-Consumer (BlockingQueue) ---");
        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(5);
        Thread producerA = new Thread(new MessageProducer(messageQueue, "A"), "ProducerA");
        Thread producerB = new Thread(new MessageProducer(messageQueue, "B"), "ProducerB");
        Thread consumer = new Thread(new MessageConsumer(messageQueue, 6), "Consumer");
        consumer.start();
        producerA.start();
        producerB.start();
        producerA.join();
        producerB.join();
        consumer.join();

        // 5. ConcurrentHashMap vs HashMap
        System.out.println("\n--- ConcurrentHashMap Thread Safety ---");
        ConcurrentHashMap<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();
        String[] wordsToCount = {"java", "thread", "java", "safe", "thread", "java", "concurrent"};
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (String word : wordsToCount) {
            executor.submit(() -> wordCount.computeIfAbsent(word, k -> new AtomicInteger(0)).incrementAndGet());
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("  Word counts: " + wordCount);

        System.out.println("\n=== Demo Complete ===");
    }
}
