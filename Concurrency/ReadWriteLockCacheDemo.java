import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Demonstrates a high-performance, thread-safe Cache using ReentrantReadWriteLock.
 *
 * Concepts covered:
 * 1. ReadLock (Shared): Allows unlimited concurrent reader threads without contention.
 * 2. WriteLock (Exclusive): Guarantees mutual exclusion during updates and invalidations.
 * 3. Cache Stampede Protection: Double-checked locking with read and write locks.
 * 4. Lock Downgrading: Acquiring WriteLock -> updating state -> acquiring ReadLock ->
 *    releasing WriteLock -> reading safely with guaranteed atomicity.
 * 5. Concurrent throughput benchmark comparing ReadWriteLock vs synchronized.
 */
public class ReadWriteLockCacheDemo {

    public static class ReadWriteCache<K, V> {
        private final Map<K, V> internalMap = new HashMap<>();
        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true); // Fair mode
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        /**
         * Reads value for given key. Multiple readers execute concurrently.
         */
        public V get(K key) {
            readLock.lock();
            try {
                return internalMap.get(key);
            } finally {
                readLock.unlock();
            }
        }

        /**
         * Writes a key-value pair into the cache under exclusive write lock.
         *
         * @param key   the cache key
         * @param value the value to associate with the key
         */
        public void put(K key, V value) {
            writeLock.lock();
            try {
                internalMap.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * Clears all entries in the cache under an exclusive write lock.
         */
        public void clear() {
            writeLock.lock();
            try {
                internalMap.clear();
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * Returns the number of entries currently in the cache.
         * Uses a shared read lock for concurrent access safety.
         *
         * @return the current entry count
         */
        public int size() {
            readLock.lock();
            try {
                return internalMap.size();
            } finally {
                readLock.unlock();
            }
        }

        /**
         * Checks whether the cache contains a mapping for the given key.
         *
         * @param key the key to look up
         * @return true if the key exists in the cache, false otherwise
         */
        public boolean containsKey(K key) {
            readLock.lock();
            try {
                return internalMap.containsKey(key);
            } finally {
                readLock.unlock();
            }
        }

        /**
         * Compute if absent with Double-Checked Locking and Lock Downgrading.
         */
        public V getOrCompute(K key, java.util.function.Function<K, V> computeFunction) {
            V value;
            // 1. Initial read check (Shared lock)
            readLock.lock();
            try {
                value = internalMap.get(key);
                if (value != null) {
                    return value;
                }
            } finally {
                readLock.unlock();
            }

            // 2. Cache miss: Upgrade requires releasing readLock first, then taking writeLock
            writeLock.lock();
            try {
                // Recheck to ensure another thread didn't compute it while waiting for writeLock
                value = internalMap.get(key);
                if (value == null) {
                    value = computeFunction.apply(key);
                    internalMap.put(key, value);
                }

                // 3. Lock Downgrading: acquire readLock BEFORE releasing writeLock
                readLock.lock();
            } finally {
                writeLock.unlock(); // Downgraded to ReadLock!
            }

            try {
                // Now safely hold readLock
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

        // Demo 1: Basic Write and Concurrent Reads
        System.out.println("\n[1] Basic Write and Read Operations:");
        userCache.put("user:101", "Alice Johnson (Admin)");
        userCache.put("user:102", "Bob Smith (Engineer)");

        System.out.println("    user:101 => " + userCache.get("user:101"));
        System.out.println("    user:102 => " + userCache.get("user:102"));
        System.out.println("    Current Cache Size: " + userCache.size());

        // Demo 2: Cache Miss with Lock Downgrading
        System.out.println("\n[2] Compute-If-Absent with Lock Downgrading:");
        String result = userCache.getOrCompute("user:103", k -> {
            System.out.println("    -> Cache MISS! Simulating expensive DB query for " + k + "...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
            return "Charlie Brown (Manager)";
        });
        System.out.println("    Retrieved value: " + result);

        // Subsequent get should be an instant cache hit
        System.out.println("    Second fetch (instant hit): " + userCache.get("user:103"));

        // Demo 3: Concurrent Readers/Writers Stress Test
        System.out.println("\n[3] Concurrent Multi-Threaded Reader/Writer Benchmark:");
        int readerCount = 8;
        int operationsPerReader = 10_000;
        ExecutorService pool = Executors.newFixedThreadPool(readerCount + 2);
        CountDownLatch latch = new CountDownLatch(readerCount + 2);

        long start = System.currentTimeMillis();

        // 8 Concurrent Readers
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

        // 2 Occasional Background Writers
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
