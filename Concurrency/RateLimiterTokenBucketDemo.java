import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates a Thread-Safe Token Bucket Rate Limiter.
 *
 * Algorithm Concept:
 * - A bucket has a maximum capacity of tokens (burst capacity).
 * - Tokens refill into the bucket at a constant rate (refillTokensPerSecond).
 * - Instead of a background refill thread, tokens are calculated lazily on each request
 *   based on the elapsed time since the last refill (highly efficient, zero background CPU).
 * - Requests consume 1 or more tokens. If not enough tokens are available, the request
 *   is either rejected immediately (tryAcquire) or paused until tokens refill (acquire).
 *
 * Industry relevance:
 * - Used in API Gateways (Spring Cloud Gateway, NGINX, Stripe, AWS API Gateway).
 * - Protects downstream databases and microservices from traffic spikes.
 */
public class RateLimiterTokenBucketDemo {

    public static class TokenBucketRateLimiter {
        private final long capacity;
        private final double refillTokensPerSecond;

        private double availableTokens;
        private long lastRefillNanos;
        private final ReentrantLock lock = new ReentrantLock();

        public TokenBucketRateLimiter(long capacity, double refillTokensPerSecond) {
            if (capacity <= 0 || refillTokensPerSecond <= 0) {
                throw new IllegalArgumentException("Capacity and refill rate must be positive.");
            }
            this.capacity = capacity;
            this.refillTokensPerSecond = refillTokensPerSecond;
            this.availableTokens = capacity; // Start with a full bucket
            this.lastRefillNanos = System.nanoTime();
        }

        /**
         * Non-blocking attempt to acquire 1 token.
         * Returns true if acquired, false if rate limit exceeded.
         */
        public boolean tryAcquire() {
            return tryAcquire(1);
        }

        /**
         * Non-blocking attempt to acquire 'tokens' amount.
         */
        public boolean tryAcquire(int tokens) {
            if (tokens <= 0) return true;
            if (tokens > capacity) return false;

            lock.lock();
            try {
                refill();
                if (availableTokens >= tokens) {
                    availableTokens -= tokens;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        /**
         * Blocking acquire: Waits until enough tokens become available.
         */
        public void acquire(int tokens) throws InterruptedException {
            if (tokens <= 0) return;

            while (true) {
                long waitNanos = 0;
                lock.lock();
                try {
                    refill();
                    if (availableTokens >= tokens) {
                        availableTokens -= tokens;
                        return; // Acquired successfully
                    }

                    // Calculate required wait time
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

        /**
         * Lazily adds tokens according to the elapsed duration.
         * Must be called while holding the lock.
         */
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
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println(" TOKEN BUCKET RATE LIMITER CONCURRENCY    ");
        System.out.println("==========================================");

        // Capacity: 5 tokens (allows burst of 5 requests), Refill: 2 tokens / sec
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(5, 2.0);

        System.out.println("\n[1] Burst Traffic Simulation:");
        System.out.printf("    Initial tokens: %.2f%n", rateLimiter.getAvailableTokens());
        for (int i = 1; i <= 7; i++) {
            boolean allowed = rateLimiter.tryAcquire();
            System.out.printf("    Request #%d: %s (Remaining ~ %.2f tokens)%n",
                    i, (allowed ? "ACCEPTED (200 OK)" : "DROPPED (429 Too Many Requests)"),
                    rateLimiter.getAvailableTokens());
        }

        // Wait 1.5 seconds to accumulate ~3 tokens
        System.out.println("\n[2] Waiting 1.5 seconds for token refill...");
        Thread.sleep(1500);
        System.out.printf("    Available after refill: %.2f tokens%n", rateLimiter.getAvailableTokens());

        boolean retry1 = rateLimiter.tryAcquire();
        boolean retry2 = rateLimiter.tryAcquire();
        System.out.println("    Retry 1 allowed: " + retry1);
        System.out.println("    Retry 2 allowed: " + retry2);

        // Demo 3: Multi-threaded Concurrent Hammer Test
        System.out.println("\n[3] Multi-Threaded Stress Test (10 concurrent threads):");
        TokenBucketRateLimiter sharedLimiter = new TokenBucketRateLimiter(3, 5.0); // 3 burst, 5 req/sec
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger acceptedCount = new AtomicInteger(0);
        AtomicInteger rejectedCount = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Synchronize all threads to fire at the exact same millisecond
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

        // Fire all threads simultaneously
        startLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("\n--- Concurrent Results ---");
        System.out.println("    Total Requests: 10");
        System.out.println("    Accepted: " + acceptedCount.get() + " (Permitted by initial burst capacity)");
        System.out.println("    Throttled: " + rejectedCount.get());

        System.out.println("\nAll Token Bucket Rate Limiter tests completed successfully.");
    }
}
