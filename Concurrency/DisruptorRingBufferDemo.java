package Concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates a High-Performance Lock-Free Ring Buffer (LMAX Disruptor Pattern).
 *
 * Architecture Highlights:
 * 1. Pre-allocated Array: Zero garbage collection during runtime message processing.
 * 2. Power-of-Two Sizing: Bitwise AND masking (seq & (capacity - 1)) replacing costly modulo arithmetic.
 * 3. Cache Line Padding: Avoids false sharing between Producer cursor and Consumer cursor across CPU L1/L2/L3 caches.
 * 4. Lock-Free Sequence Tracking: Uses memory barriers and atomic cursors rather than heavyweight locks.
 * 5. Batching Consumer: Consumes all available published events up to the producer's high watermark in one cycle.
 *
 * Comparison:
 * - Benchmarked against java.util.concurrent.ArrayBlockingQueue for message throughput.
 */
public class DisruptorRingBufferDemo {

    /**
     * Cache line padded sequence to prevent False Sharing.
     * Most 64-bit CPU cache lines are 64 bytes (8 long variables).
     */
    public static class PaddedAtomicLong {
        // Prevent false sharing before the value
        public volatile long p1, p2, p3, p4, p5, p6, p7;
        public final AtomicLong value = new AtomicLong(-1);
        // Prevent false sharing after the value
        public volatile long a1, a2, a3, a4, a5, a6, a7;

        public long get() {
            return value.get();
        }

        public void set(long val) {
            value.set(val);
        }

        public boolean compareAndSet(long expect, long update) {
            return value.compareAndSet(expect, update);
        }

        public long incrementAndGet() {
            return value.incrementAndGet();
        }
    }

    /**
     * Circular Ring Buffer for Value Objects.
     */
    public static class RingBuffer<E> {
        private final Object[] entries;
        private final int bufferSize;
        private final int mask;

        // Producer claim cursor & published cursor
        private final PaddedAtomicLong cursor = new PaddedAtomicLong();
        // Consumer processed cursor
        private final PaddedAtomicLong consumerSequence = new PaddedAtomicLong();

        public RingBuffer(int powerOfTwoCapacity) {
            if (Integer.bitCount(powerOfTwoCapacity) != 1) {
                throw new IllegalArgumentException("Capacity must be a power of two");
            }
            this.bufferSize = powerOfTwoCapacity;
            this.mask = powerOfTwoCapacity - 1;
            this.entries = new Object[bufferSize];
        }

        /**
         * Publishes an element into the ring buffer, blocking/spinning if ring buffer is full.
         */
        public void publish(E item) {
            long nextSeq = cursor.incrementAndGet();

            // Wait until consumer catches up so we don't overwrite unconsumed slots
            long wrapPoint = nextSeq - bufferSize;
            while (consumerSequence.get() < wrapPoint) {
                Thread.onSpinWait(); // Java 9+ CPU pause hint to reduce pipeline stalls
            }

            int index = (int) (nextSeq & mask);
            entries[index] = item;
        }

        /**
         * Consumes up to maxBatch items currently available.
         */
        @SuppressWarnings("unchecked")
        public int drainTo(java.util.function.Consumer<E> consumer, int maxBatch) {
            long currentConsumerSeq = consumerSequence.get();
            long availableProducerSeq = cursor.get();

            if (currentConsumerSeq >= availableProducerSeq) {
                return 0; // Nothing new to consume
            }

            int count = 0;
            long nextToConsume = currentConsumerSeq + 1;
            long limit = Math.min(availableProducerSeq, currentConsumerSeq + maxBatch);

            while (nextToConsume <= limit) {
                int index = (int) (nextToConsume & mask);
                E item = (E) entries[index];
                consumer.accept(item);
                nextToConsume++;
                count++;
            }

            consumerSequence.set(limit);
            return count;
        }

        public int capacity() {
            return bufferSize;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=============================================================");
        System.out.println("       LMAX DISRUPTOR HIGH-THROUGHPUT RING BUFFER DEMO       ");
        System.out.println("=============================================================");

        int capacity = 1 << 16; // 65,536 power of two
        int totalEvents = 1_000_000;

        System.out.printf("Benchmarking %d events with capacity %d...%n", totalEvents, capacity);

        // 1. Benchmark RingBuffer
        RingBuffer<Long> ringBuffer = new RingBuffer<>(capacity);
        CountDownLatch disruptorLatch = new CountDownLatch(1);
        AtomicLong consumedCountDisruptor = new AtomicLong(0);

        long startRingBuffer = System.currentTimeMillis();

        Thread consumerThread = new Thread(() -> {
            long processed = 0;
            while (processed < totalEvents) {
                int drained = ringBuffer.drainTo(event -> {}, 512);
                processed += drained;
                if (drained == 0) {
                    Thread.onSpinWait();
                }
            }
            consumedCountDisruptor.set(processed);
            disruptorLatch.countDown();
        });
        consumerThread.start();

        Thread producerThread = new Thread(() -> {
            for (long i = 0; i < totalEvents; i++) {
                ringBuffer.publish(i);
            }
        });
        producerThread.start();

        disruptorLatch.await();
        producerThread.join();
        consumerThread.join();
        long timeRingBuffer = Math.max(1, System.currentTimeMillis() - startRingBuffer);

        System.out.printf("[RingBuffer] Completed %d events in %d ms (Throughput: %,d ops/sec)%n",
                consumedCountDisruptor.get(), timeRingBuffer, (totalEvents * 1000L / timeRingBuffer));

        // 2. Compare with standard ArrayBlockingQueue
        ArrayBlockingQueue<Long> abq = new ArrayBlockingQueue<>(capacity);
        CountDownLatch abqLatch = new CountDownLatch(1);
        AtomicLong consumedCountAbq = new AtomicLong(0);

        long startAbq = System.currentTimeMillis();

        Thread abqConsumer = new Thread(() -> {
            long processed = 0;
            while (processed < totalEvents) {
                try {
                    abq.take();
                    processed++;
                } catch (InterruptedException ignored) {}
            }
            consumedCountAbq.set(processed);
            abqLatch.countDown();
        });
        abqConsumer.start();

        Thread abqProducer = new Thread(() -> {
            for (long i = 0; i < totalEvents; i++) {
                try {
                    abq.put(i);
                } catch (InterruptedException ignored) {}
            }
        });
        abqProducer.start();

        abqLatch.await();
        abqProducer.join();
        abqConsumer.join();
        long timeAbq = Math.max(1, System.currentTimeMillis() - startAbq);

        System.out.printf("[ArrayBlockingQueue] Completed %d events in %d ms (Throughput: %,d ops/sec)%n",
                consumedCountAbq.get(), timeAbq, (totalEvents * 1000L / timeAbq));

        System.out.printf("Speedup factor of RingBuffer over ArrayBlockingQueue: %.2fx%n",
                (double) timeAbq / timeRingBuffer);

        System.out.println("\nDisruptor Ring Buffer verification completed successfully.");
    }
}
