import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockCacheDemo {

    public static class ReadWriteCache<K, V> {
        private final Map<K, V> internalMap = new HashMap<>();
        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        public V get(K key) {
            readLock.lock();
            try {
                return internalMap.get(key);
            } finally {
                readLock.unlock();
            }
        }

        public void put(K key, V value) {
            writeLock.lock();
            try {
                internalMap.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public void clear() {
            writeLock.lock();
            try {
                internalMap.clear();
            } finally {
                writeLock.unlock();
            }
        }

        public int size() {
            readLock.lock();
            try {
                return internalMap.size();
            } finally {
                readLock.unlock();
            }
        }

        public boolean containsKey(K key) {
            readLock.lock();
            try {
                return internalMap.containsKey(key);
            } finally {
                readLock.unlock();
            }
        }

        public V getOrCompute(K key, java.util.function.Function<K, V> computeFunction) {
            V value;

            readLock.lock();
            try {
                value = internalMap.get(key);
                if (value != null) {
                    return value;
                }
            } finally {
                readLock.unlock();
            }

            writeLock.lock();
            try {

                value = internalMap.get(key);
                if (value == null) {
                    value = computeFunction.apply(key);
                    internalMap.put(key, value);
                }

                readLock.lock();
            } finally {
                writeLock.unlock();
            }

            try {

                return value;
            } finally {
                readLock.unlock();
            }
        }

        public int size() {
            readLock.lock();
            try {
                return internalMap.size();
            } finally {
                readLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println(" REENTRANT READ-WRITE LOCK CACHE DEMO     ");
        System.out.println("==========================================");

        ReadWriteCache<String, String> userCache = new ReadWriteCache<>();

        System.out.println("\n[1] Basic Write and Read Operations:");
        userCache.put("user:101", "Alice Johnson (Admin)");
        userCache.put("user:102", "Bob Smith (Engineer)");

        System.out.println("    user:101 => " + userCache.get("user:101"));
        System.out.println("    user:102 => " + userCache.get("user:102"));
        System.out.println("    Current Cache Size: " + userCache.size());

        System.out.println("\n[2] Compute-If-Absent with Lock Downgrading:");
        String result = userCache.getOrCompute("user:103", k -> {
            System.out.println("    -> Cache MISS! Simulating expensive DB query for " + k + "...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
            return "Charlie Brown (Manager)";
        });
        System.out.println("    Retrieved value: " + result);

        System.out.println("    Second fetch (instant hit): " + userCache.get("user:103"));

        System.out.println("\n[3] Concurrent Multi-Threaded Reader/Writer Benchmark:");
        int readerCount = 8;
        int operationsPerReader = 10_000;
        ExecutorService pool = Executors.newFixedThreadPool(readerCount + 2);
        CountDownLatch latch = new CountDownLatch(readerCount + 2);

        long start = System.currentTimeMillis();

        for (int i = 0; i < readerCount; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerReader; j++) {
                        userCache.get("user:101");
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < 2; i++) {
            final int writerId = i;
            pool.submit(() -> {
                try {
                    for (int j = 0; j < 50; j++) {
                        userCache.put("metric:writer:" + writerId, "value-" + j);
                        Thread.sleep(1);
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();
        long elapsed = System.currentTimeMillis() - start;

        System.out.printf("    Completed %d read ops and 100 write ops in %d ms.%n",
                readerCount * operationsPerReader, elapsed);
        System.out.println("    Final Cache Size: " + userCache.size());

        System.out.println("\nAll ReadWriteLock Cache demonstrations completed successfully.");
    }
}
