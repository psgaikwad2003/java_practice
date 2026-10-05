package DesignPatterns;

import java.util.*;

public class SagaPatternDemo {

    public enum SagaStatus {
        SUCCESS,
        COMPENSATED_FAILURE,
        FAILED_COMPENSATION
    }

    public static class OrderContext {
        public final String orderId;
        public final String customerId;
        public final double amount;
        public final String itemSku;

        public boolean orderCreated = false;
        public boolean paymentAuthorized = false;
        public boolean inventoryReserved = false;
        public boolean shipmentDispatched = false;

        public final List<String> executionAuditLog = new ArrayList<>();

        public OrderContext(String orderId, String customerId, double amount, String itemSku) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.amount = amount;
            this.itemSku = itemSku;
        }

        public void log(String message) {
            executionAuditLog.add(message);
            System.out.println("    [LOG] " + message);
        }
    }

    public interface SagaStep<T> {
        String getName();
        boolean execute(T context);
        void compensate(T context);
    }

    public static class SagaOrchestrator<T> {
        private final String sagaName;
        private final List<SagaStep<T>> steps = new ArrayList<>();

        public SagaOrchestrator(String sagaName) {
            this.sagaName = sagaName;
        }

        public SagaOrchestrator<T> addStep(SagaStep<T> step) {
            steps.add(step);
            return this;
        }

        public SagaStatus execute(T context) {
            System.out.printf("=== Starting Saga: %s ===%n", sagaName);
            Deque<SagaStep<T>> executedSteps = new ArrayDeque<>();

            for (SagaStep<T> step : steps) {
                System.out.printf("  -> Executing Step: %s%n", step.getName());
                boolean success = false;
                try {
                    success = step.execute(context);
                } catch (Exception ex) {
                    System.out.printf("  [!] Exception during step %s: %s%n", step.getName(), ex.getMessage());
                    success = false;
                }

                if (success) {
                    executedSteps.push(step);
                } else {
                    System.out.printf("  [X] Step failed: %s! Initiating compensating rollback cascade...%n", step.getName());
                    rollback(executedSteps, context);
                    return SagaStatus.COMPENSATED_FAILURE;
                }
            }

            System.out.printf("=== Saga %s COMPLETED SUCCESSFULLY ===%n", sagaName);
            return SagaStatus.SUCCESS;
        }

        private void rollback(Deque<SagaStep<T>> executedSteps, T context) {
            while (!executedSteps.isEmpty()) {
                SagaStep<T> stepToCompensate = executedSteps.pop();
                System.out.printf("  <- Compensating Step: %s%n", stepToCompensate.getName());
                try {
                    stepToCompensate.compensate(context);
                } catch (Exception ex) {
                    System.err.printf("FATAL: Failed to compensate step %s: %s%n",
                            stepToCompensate.getName(), ex.getMessage());
                }
            }
            System.out.println("  [OK] Compensation rollback completed. System state is clean.");
        }
    }

    public static class OrderCreationStep implements SagaStep<OrderContext> {
        @Override
        public String getName() { return "OrderCreationStep"; }

        @Override
        public boolean execute(OrderContext ctx) {
            ctx.orderCreated = true;
            ctx.log("Order #" + ctx.orderId + " created in PENDING state.");
            return true;
        }

        @Override
        public void compensate(OrderContext ctx) {
            ctx.orderCreated = false;
            ctx.log("Compensate: Order #" + ctx.orderId + " marked as CANCELLED.");
        }
    }

    public static class PaymentStep implements SagaStep<OrderContext> {
        @Override
        public String getName() { return "PaymentStep"; }

        @Override
        public boolean execute(OrderContext ctx) {
            ctx.paymentAuthorized = true;
            ctx.log("Authorized payment of $" + ctx.amount + " for customer " + ctx.customerId);
            return true;
        }

        @Override
        public void compensate(OrderContext ctx) {
            ctx.paymentAuthorized = false;
            ctx.log("Compensate: Refunded $" + ctx.amount + " back to customer " + ctx.customerId);
        }
    }

    public static class InventoryStep implements SagaStep<OrderContext> {
        private final boolean simulateOutOfStock;

        public InventoryStep(boolean simulateOutOfStock) {
            this.simulateOutOfStock = simulateOutOfStock;
        }

        @Override
        public String getName() { return "InventoryStep"; }

        @Override
        public boolean execute(OrderContext ctx) {
            if (simulateOutOfStock) {
                ctx.log("Error: Item " + ctx.itemSku + " is OUT OF STOCK!");
                return false;
            }
            ctx.inventoryReserved = true;
            ctx.log("Reserved 1 unit of " + ctx.itemSku + " in warehouse.");
            return true;
        }

        @Override
        public void compensate(OrderContext ctx) {
            ctx.inventoryReserved = false;
            ctx.log("Compensate: Released reservation for " + ctx.itemSku);
        }
    }

    public static class ShippingStep implements SagaStep<OrderContext> {
        @Override
        public String getName() { return "ShippingStep"; }

        @Override
        public boolean execute(OrderContext ctx) {
            ctx.shipmentDispatched = true;
            ctx.log("Generated tracking label and dispatched parcel.");
            return true;
        }

        @Override
        public void compensate(OrderContext ctx) {
            ctx.shipmentDispatched = false;
            ctx.log("Compensate: Cancelled shipping label with courier.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("        DISTRIBUTED SAGA ORCHESTRATOR PATTERN DEMO           ");
        System.out.println("=============================================================");

        System.out.println("\n--- Scenario 1: Happy Path (All Steps Pass) ---");
        OrderContext ctx1 = new OrderContext("ORD-9001", "CUST-101", 149.99, "LAPTOP-STAND");
        SagaOrchestrator<OrderContext> successfulSaga = new SagaOrchestrator<>("OrderCheckoutSaga");
        successfulSaga
            .addStep(new OrderCreationStep())
            .addStep(new PaymentStep())
            .addStep(new InventoryStep(false))
            .addStep(new ShippingStep());

        SagaStatus status1 = successfulSaga.execute(ctx1);
        System.out.println("Saga 1 Final Status: " + status1);
        System.out.printf("Summary: Order=%b, Payment=%b, Inventory=%b, Shipping=%b%n",
                ctx1.orderCreated, ctx1.paymentAuthorized, ctx1.inventoryReserved, ctx1.shipmentDispatched);

        System.out.println("\n--- Scenario 2: Downstream Failure (Automatic Rollback) ---");
        OrderContext ctx2 = new OrderContext("ORD-9002", "CUST-202", 899.00, "OUT-OF-STOCK-PHONE");
        SagaOrchestrator<OrderContext> failingSaga = new SagaOrchestrator<>("OrderCheckoutSagaWithFailure");
        failingSaga
            .addStep(new OrderCreationStep())
            .addStep(new PaymentStep())
            .addStep(new InventoryStep(true))
            .addStep(new ShippingStep());

        SagaStatus status2 = failingSaga.execute(ctx2);
        System.out.println("Saga 2 Final Status: " + status2);
        System.out.printf("Summary after compensation: Order=%b, Payment=%b, Inventory=%b, Shipping=%b%n",
                ctx2.orderCreated, ctx2.paymentAuthorized, ctx2.inventoryReserved, ctx2.shipmentDispatched);

        System.out.println("\nSaga Orchestrator verification completed successfully.");
    }
}
