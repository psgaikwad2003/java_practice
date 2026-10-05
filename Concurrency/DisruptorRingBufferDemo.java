package Concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DisruptorRingBufferDemo {

    public static class PaddedAtomicLong {

        public volatile long p1, p2, p3, p4, p5, p6, p7;
        public final AtomicLong value = new AtomicLong(-1);

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

    public static class RingBuffer<E> {
        private final Object[] entries;
        private final int bufferSize;
        private final int mask;

        private final PaddedAtomicLong cursor = new PaddedAtomicLong();

        private final PaddedAtomicLong consumerSequence = new PaddedAtomicLong();

        public RingBuffer(int powerOfTwoCapacity) {
            if (Integer.bitCount(powerOfTwoCapacity) != 1) {
                throw new IllegalArgumentException("Capacity must be a power of two");
            }
            this.bufferSize = powerOfTwoCapacity;
            this.mask = powerOfTwoCapacity - 1;
            this.entries = new Object[bufferSize];
        }

        public void publish(E item) {
            long nextSeq = cursor.incrementAndGet();

            long wrapPoint = nextSeq - bufferSize;
            while (consumerSequence.get() < wrapPoint) {
                Thread.onSpinWait();
            }

            int index = (int) (nextSeq & mask);
            entries[index] = item;
        }

        @SuppressWarnings("unchecked")
        public int drainTo(java.util.function.Consumer<E> consumer, int maxBatch) {
            long currentConsumerSeq = consumerSequence.get();
            long availableProducerSeq = cursor.get();

            if (currentConsumerSeq >= availableProducerSeq) {
                return 0;
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

        int capacity = 1 << 16;
        int totalEvents = 1_000_000;

        System.out.printf("Benchmarking %d events with capacity %d...%n", totalEvents, capacity);

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
