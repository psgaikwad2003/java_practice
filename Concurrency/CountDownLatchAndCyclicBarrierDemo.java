import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchAndCyclicBarrierDemo {

    public static class ServiceComponent {
        private final String name;
        private final int startupDurationMs;
        private final boolean shouldSucceed;

        public ServiceComponent(String name, int startupDurationMs, boolean shouldSucceed) {
            this.name = name;
            this.startupDurationMs = startupDurationMs;
            this.shouldSucceed = shouldSucceed;
        }

        public boolean initialize() {
            try {
                System.out.printf("  [INIT START] %-22s initializing (expected ~%d ms)...%n", name, startupDurationMs);
                Thread.sleep(startupDurationMs);
                if (shouldSucceed) {
                    System.out.printf("  [INIT DONE]  %-22s is READY.%n", name);
                    return true;
                } else {
                    System.out.printf("  [INIT FAIL]  %-22s FAILED during startup!%n", name);
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.printf("  [INTERRUPTED] %s interrupted!%n", name);
                return false;
            }
        }

        public String getName() {
            return name;
        }
    }

    public static void runCountDownLatchDemo() {
        System.out.println("===============================================================");
        System.out.println("   PART 1: CountDownLatch - Microservice Readiness Probe      ");
        System.out.println("===============================================================");

        List<ServiceComponent> services = List.of(
            new ServiceComponent("PostgreSQL Database", 120, true),
            new ServiceComponent("Redis Cache Cluster", 80, true),
            new ServiceComponent("Kafka Message Broker", 150, true),
            new ServiceComponent("OAuth2 Identity Provider", 100, true)
        );

        int totalServices = services.size();
        CountDownLatch startupLatch = new CountDownLatch(totalServices);
        ConcurrentMap<String, Boolean> serviceStatus = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(totalServices);

        long startTime = System.currentTimeMillis();

        try {
            System.out.println("Main Orchestrator: Bootstrapping " + totalServices + " core microservices concurrently...\n");

            for (ServiceComponent service : services) {
                executor.submit(() -> {
                    boolean ok = false;
                    try {
                        ok = service.initialize();
                    } finally {
                        serviceStatus.put(service.getName(), ok);
                        startupLatch.countDown();
                        System.out.printf("  [LATCH]      Count down! Remaining pending services: %d%n", startupLatch.getCount());
                    }
                });
            }

            System.out.println("\nMain Orchestrator: Waiting for all services to report ready (timeout: 3s)...");
            boolean allCompleted = startupLatch.await(3, TimeUnit.SECONDS);
            long totalDuration = System.currentTimeMillis() - startTime;

            if (allCompleted) {
                boolean allHealthy = serviceStatus.values().stream().allMatch(Boolean::booleanValue);
                if (allHealthy) {
                    System.out.printf("%n>>> SUCCESS: All services ready in %d ms. Opening API Gateway traffic! <<<%n%n", totalDuration);
                } else {
                    System.out.printf("%n>>> FAILURE: Some services reported unhealthy! Gateway startup aborted. <<<%n%n");
                }
            } else {
                System.out.printf("%n>>> TIMEOUT: Startup exceeded deadline! Gateway startup aborted. <<<%n%n");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main orchestrator thread interrupted!");
        } finally {
            executor.shutdown();
        }
    }

    public static class SimulationWorker implements Runnable {
        private final int workerId;
        private final int totalPhases;
        private final CyclicBarrier barrier;
        private final int[][] sharedData;
        private final AtomicInteger globalSum;

        public SimulationWorker(int workerId, int totalPhases, CyclicBarrier barrier,
                                int[][] sharedData, AtomicInteger globalSum) {
            this.workerId = workerId;
            this.totalPhases = totalPhases;
            this.barrier = barrier;
            this.sharedData = sharedData;
            this.globalSum = globalSum;
        }

        @Override
        public void run() {
            try {
                for (int phase = 1; phase <= totalPhases; phase++) {

                    int workTime = 50 + (workerId * 25);
                    Thread.sleep(workTime);

                    int partialResult = (phase * 10) + (workerId * 5);
                    sharedData[phase - 1][workerId] = partialResult;
                    globalSum.addAndGet(partialResult);

                    System.out.printf("  [Worker-%d] Finished Phase %d computation (val=%d). Awaiting barrier...%n",
                            workerId, phase, partialResult);

                    barrier.await();

                    System.out.printf("  [Worker-%d] Released past Barrier for Phase %d!%n", workerId, phase);
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.printf("  [Worker-%d] Barrier interrupted/broken!%n", workerId);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void runCyclicBarrierDemo() {
        System.out.println("===============================================================");
        System.out.println("   PART 2: CyclicBarrier - Multi-Phase Iterative Simulation   ");
        System.out.println("===============================================================");

        int numWorkers = 3;
        int totalPhases = 3;
        int[][] phaseResults = new int[totalPhases][numWorkers];
        AtomicInteger globalSum = new AtomicInteger(0);
        AtomicInteger barrierCycleCounter = new AtomicInteger(1);

        Runnable barrierAction = () -> {
            int currentPhase = barrierCycleCounter.getAndIncrement();
            System.out.println("\n  -------------------------------------------------------------");
            System.out.printf("  *** BARRIER ACTION: Phase %d Complete! Aggregating Row Data: %s%n",
                    currentPhase, Arrays.toString(phaseResults[currentPhase - 1]));
            System.out.printf("  *** Global Running Sum: %d%n", globalSum.get());
            System.out.println("  *** Barrier automatically resets. Advancing all threads to next phase!");
            System.out.println("  -------------------------------------------------------------\n");
        };

        CyclicBarrier barrier = new CyclicBarrier(numWorkers, barrierAction);
        ExecutorService executor = Executors.newFixedThreadPool(numWorkers);

        System.out.printf("Launching %d workers across %d synchronized execution phases...%n%n", numWorkers, totalPhases);

        for (int i = 0; i < numWorkers; i++) {
            executor.submit(new SimulationWorker(i, totalPhases, barrier, phaseResults, globalSum));
        }

        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);
            if (finished) {
                System.out.println("\n>>> SUCCESS: All computation phases completed across all workers! <<<\n");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        runCountDownLatchDemo();
        runCyclicBarrierDemo();
    }
}
