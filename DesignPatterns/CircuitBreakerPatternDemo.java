package DesignPatterns;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

/**
 * Demonstrates the Circuit Breaker Pattern for fault-tolerant microservice architectures.
 *
 * States:
 * 1. CLOSED: Normal operation. Requests pass to downstream service.
 *    Failures are recorded in a sliding window. If failure rate exceeds threshold, trips to OPEN.
 * 2. OPEN: Downstream is presumed broken. Requests fail fast without calling remote service,
 *    immediately executing a fallback. After a cooldown window, transitions to HALF_OPEN.
 * 3. HALF_OPEN: Probe state allowing a limited number of trial requests. If trial requests succeed,
 *    circuit resets to CLOSED. If any fail, circuit re-opens.
 *
 * Real-world correspondence:
 * - Netflix Hystrix / Resilience4j CircuitBreaker.
 */
public class CircuitBreakerPatternDemo {

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    public static class CircuitBreakerConfig {
        public final int slidingWindowSize;
        public final double failureRateThreshold; // e.g., 0.50 (50%)
        public final long cooldownPeriodMs;
        public final int halfOpenTrialCalls;

        public CircuitBreakerConfig(int slidingWindowSize, double failureRateThreshold,
                                    long cooldownPeriodMs, int halfOpenTrialCalls) {
            this.slidingWindowSize = slidingWindowSize;
            this.failureRateThreshold = failureRateThreshold;
            this.cooldownPeriodMs = cooldownPeriodMs;
            this.halfOpenTrialCalls = halfOpenTrialCalls;
        }
    }

    public static class CircuitBreaker {
        private final String name;
        private final CircuitBreakerConfig config;

        private State state = State.CLOSED;
        private long lastStateChangedTimestamp = System.currentTimeMillis();
        private final Deque<Boolean> callHistory = new ArrayDeque<>(); // true = success, false = failure
        private int halfOpenSuccesses = 0;

        public CircuitBreaker(String name, CircuitBreakerConfig config) {
            this.name = name;
            this.config = config;
        }

        public synchronized State getState() {
            // Check if OPEN cooldown has elapsed
            if (state == State.OPEN) {
                long elapsed = System.currentTimeMillis() - lastStateChangedTimestamp;
                if (elapsed >= config.cooldownPeriodMs) {
                    transitionTo(State.HALF_OPEN);
                }
            }
            return state;
        }

        private synchronized void transitionTo(State newState) {
            System.out.printf("[%s] State transition: %s -> %s%n", name, this.state, newState);
            this.state = newState;
            this.lastStateChangedTimestamp = System.currentTimeMillis();
            if (newState == State.HALF_OPEN) {
                halfOpenSuccesses = 0;
            } else if (newState == State.CLOSED) {
                callHistory.clear();
                halfOpenSuccesses = 0;
            }
        }

        public synchronized <T> T execute(Supplier<T> action, Supplier<T> fallback) {
            State currentState = getState();

            if (currentState == State.OPEN) {
                // Short-circuit execution!
                System.out.println("  [!] Circuit OPEN: fast-failing to fallback.");
                return fallback.get();
            }

            try {
                T result = action.get();
                recordSuccess();
                return result;
            } catch (Exception ex) {
                System.out.println("  [X] Call failed: " + ex.getMessage());
                recordFailure();
                return fallback.get();
            }
        }

        private synchronized void recordSuccess() {
            if (state == State.HALF_OPEN) {
                halfOpenSuccesses++;
                if (halfOpenSuccesses >= config.halfOpenTrialCalls) {
                    transitionTo(State.CLOSED);
                }
            } else if (state == State.CLOSED) {
                pushHistory(true);
            }
        }

        private synchronized void recordFailure() {
            if (state == State.HALF_OPEN) {
                transitionTo(State.OPEN);
            } else if (state == State.CLOSED) {
                pushHistory(false);
                evaluateFailureRate();
            }
        }

        private void pushHistory(boolean success) {
            if (callHistory.size() >= config.slidingWindowSize) {
                callHistory.pollFirst();
            }
            callHistory.addLast(success);
        }

        private void evaluateFailureRate() {
            if (callHistory.size() < config.slidingWindowSize) {
                return; // Not enough calls to evaluate threshold yet
            }

            long failures = callHistory.stream().filter(s -> !s).count();
            double failureRate = (double) failures / callHistory.size();

            if (failureRate >= config.failureRateThreshold) {
                System.out.printf("[%s] Failure rate %.1f%% exceeded threshold %.1f%%! Tripping circuit!%n",
                        name, failureRate * 100, config.failureRateThreshold * 100);
                transitionTo(State.OPEN);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=============================================================");
        System.out.println("       FAULT-TOLERANT CIRCUIT BREAKER PATTERN DEMO           ");
        System.out.println("=============================================================");

        // Config: window=6 calls, 50% threshold, 300ms cooldown, 2 trials in HALF_OPEN
        CircuitBreakerConfig config = new CircuitBreakerConfig(6, 0.50, 300, 2);
        CircuitBreaker cb = new CircuitBreaker("PaymentGatewayService", config);

        // Flaky external payment gateway simulator
        boolean[] shouldFail = {false, false, true, true, true, true, false, false, false};
        int[] callIndex = new int[]{0};

        Supplier<String> remotePaymentCall = () -> {
            int idx = callIndex[0]++;
            if (idx < shouldFail.length && shouldFail[idx]) {
                throw new RuntimeException("HTTP 503 Service Unavailable");
            }
            return "Payment Processed: Txn#" + (1000 + idx);
        };

        Supplier<String> fallbackPayment = () -> "FALLBACK: Queued payment offline for later retry";

        System.out.println("\n--- Phase 1: Calling Remote Service (Normal then Failing) ---");
        for (int i = 0; i < 6; i++) {
            String res = cb.execute(remotePaymentCall, fallbackPayment);
            System.out.printf("Call #%d Result: %s (CB State: %s)%n", i + 1, res, cb.getState());
        }

        System.out.println("\n--- Phase 2: Calling while Circuit is OPEN (Fast-Fail) ---");
        String fastFailResult = cb.execute(remotePaymentCall, fallbackPayment);
        System.out.println("Fast-Fail Result: " + fastFailResult);

        System.out.println("\n--- Phase 3: Waiting for Cooldown (350 ms)... ---");
        Thread.sleep(350);

        System.out.println("\n--- Phase 4: Probing in HALF_OPEN State ---");
        // Next 2 calls will be evaluated as trials
        for (int i = 0; i < 2; i++) {
            String res = cb.execute(remotePaymentCall, fallbackPayment);
            System.out.printf("Trial #%d Result: %s (CB State: %s)%n", i + 1, res, cb.getState());
        }

        System.out.println("\n--- Phase 5: Normal Operation Restored ---");
        String finalRes = cb.execute(remotePaymentCall, fallbackPayment);
        System.out.printf("Final Call Result: %s (CB State: %s)%n", finalRes, cb.getState());

        System.out.println("\nCircuit Breaker verification completed successfully.");
    }
}
