import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiterTokenBucketDemo {

    public static class TokenBucketRateLimiter {
        private final long capacity;
        private final double refillTokensPerSecond;

        private double availableTokens;
        private long lastRefillNanos;
        private final ReentrantLock lock = new ReentrantLock();

        private long totalRequests = 0;
        private long acceptedRequests = 0;
        private long throttledRequests = 0;

        public TokenBucketRateLimiter(long capacity, double refillTokensPerSecond) {
            if (capacity <= 0 || refillTokensPerSecond <= 0) {
                throw new IllegalArgumentException("Capacity and refill rate must be positive.");
            }
            this.capacity = capacity;
            this.refillTokensPerSecond = refillTokensPerSecond;
            this.availableTokens = capacity;
            this.lastRefillNanos = System.nanoTime();
        }

        public boolean tryAcquire() {
            return tryAcquire(1);
        }

        public boolean tryAcquire(int tokens) {
            if (tokens <= 0) return true;
            if (tokens > capacity) return false;

            lock.lock();
            try {
                totalRequests++;
                refill();
                if (availableTokens >= tokens) {
                    availableTokens -= tokens;
                    acceptedRequests++;
                    return true;
                }
                throttledRequests++;
                return false;
            } finally {
                lock.unlock();
            }
        }

        public boolean tryAcquire(int tokens, long timeout, TimeUnit unit) throws InterruptedException {
            if (tokens <= 0) return true;
            if (tokens > capacity) return false;

            long timeoutNanos = unit.toNanos(timeout);
            long deadline = System.nanoTime() + timeoutNanos;

            while (true) {
                long waitNanos = 0;
                lock.lock();
                try {
                    refill();
                    if (availableTokens >= tokens) {
                        availableTokens -= tokens;
                        totalRequests++;
                        acceptedRequests++;
                        return true;
                    }

                    double missingTokens = tokens - availableTokens;
                    waitNanos = (long) ((missingTokens / refillTokensPerSecond) * 1_000_000_000L);
                } finally {
                    lock.unlock();
                }

                long remainingNanos = deadline - System.nanoTime();
                if (remainingNanos <= 0 || waitNanos > remainingNanos) {
                    lock.lock();
                    try {
                        totalRequests++;
                        throttledRequests++;
                    } finally {
                        lock.unlock();
                    }
                    return false;
                }

                TimeUnit.NANOSECONDS.sleep(Math.min(waitNanos, remainingNanos));
            }
        }

        public void acquire(int tokens) throws InterruptedException {
            if (tokens <= 0) return;

            while (true) {
                long waitNanos = 0;
                lock.lock();
                try {
                    refill();
                    if (availableTokens >= tokens) {
                        availableTokens -= tokens;
                        totalRequests++;
                        acceptedRequests++;
                        return;
                    }

                    double missingTokens = tokens - availableTokens;
                    waitNanos = (long) ((missingTokens / refillTokensPerSecond) * 1_000_000_000L);
                } finally {
                    lock.unlock();
                }

                if (waitNanos > 0) {
                    TimeUnit.NANOSECONDS.sleep(waitNanos);
                }
            }
        }

        private void refill() {
            long now = System.nanoTime();
            long elapsedNanos = now - lastRefillNanos;

            if (elapsedNanos > 0) {
                double tokensToAdd = (elapsedNanos / 1_000_000_000.0) * refillTokensPerSecond;
                if (tokensToAdd > 0) {
                    availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
                    lastRefillNanos = now;
                }
            }
        }

        public double getAvailableTokens() {
            lock.lock();
            try {
                refill();
                return availableTokens;
            } finally {
                lock.unlock();
            }
        }

        public long getTotalRequests() {
            lock.lock();
            try { return totalRequests; } finally { lock.unlock(); }
        }

        public long getAcceptedRequests() {
            lock.lock();
            try { return acceptedRequests; } finally { lock.unlock(); }
        }

        public long getThrottledRequests() {
            lock.lock();
            try { return throttledRequests; } finally { lock.unlock(); }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println(" TOKEN BUCKET RATE LIMITER CONCURRENCY    ");
        System.out.println("==========================================");

        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(5, 2.0);

        System.out.println("\n[1] Burst Traffic Simulation:");
        System.out.printf("    Initial tokens: %.2f%n", rateLimiter.getAvailableTokens());
        for (int i = 1; i <= 7; i++) {
            boolean allowed = rateLimiter.tryAcquire();
            System.out.printf("    Request #%d: %s (Remaining ~ %.2f tokens)%n",
                    i, (allowed ? "ACCEPTED (200 OK)" : "DROPPED (429 Too Many Requests)"),
                    rateLimiter.getAvailableTokens());
        }

        System.out.println("\n[2] Waiting 1.5 seconds for token refill...");
        Thread.sleep(1500);
        System.out.printf("    Available after refill: %.2f tokens%n", rateLimiter.getAvailableTokens());

        boolean retry1 = rateLimiter.tryAcquire();
        boolean retry2 = rateLimiter.tryAcquire();
        System.out.println("    Retry 1 allowed: " + retry1);
        System.out.println("    Retry 2 allowed: " + retry2);

        System.out.println("\n[3] Multi-Threaded Stress Test (10 concurrent threads):");
        TokenBucketRateLimiter sharedLimiter = new TokenBucketRateLimiter(3, 5.0);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger acceptedCount = new AtomicInteger(0);
        AtomicInteger rejectedCount = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    if (sharedLimiter.tryAcquire()) {
                        acceptedCount.incrementAndGet();
                        System.out.println("    [Thread-" + Thread.currentThread().threadId() + "] Req #" + requestId + ": SUCCESS");
                    } else {
                        rejectedCount.incrementAndGet();
                        System.out.println("    [Thread-" + Thread.currentThread().threadId() + "] Req #" + requestId + ": THROTTLED (429)");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        startLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("\n--- Concurrent Results ---");
        System.out.println("    Total Requests: 10");
        System.out.println("    Accepted: " + acceptedCount.get() + " (Permitted by initial burst capacity)");
        System.out.println("    Throttled: " + rejectedCount.get());

        System.out.println("\n[4] Timed tryAcquire Demo:");
        TokenBucketRateLimiter timedLimiter = new TokenBucketRateLimiter(1, 2.0);
        timedLimiter.tryAcquire();
        System.out.println("    Tokens drained. Attempting timed acquire with 600ms timeout...");
        boolean acquiredWithinTimeout = timedLimiter.tryAcquire(1, 600, TimeUnit.MILLISECONDS);
        System.out.println("    Acquired within 600ms? " + acquiredWithinTimeout);
        System.out.printf("    Telemetry: Total=%d, Accepted=%d, Throttled=%d%n",
                timedLimiter.getTotalRequests(), timedLimiter.getAcceptedRequests(), timedLimiter.getThrottledRequests());

        System.out.println("\nAll Token Bucket Rate Limiter tests completed successfully.");
    }
}
