import java.util.*;

/**
 * Enum State Machine Demo (Hard - Level 5)
 * 
 * Topics Covered:
 * - Enum-based State Machine pattern
 * - Defining valid transitions between states
 * - Preventing invalid state transitions
 * - Abstract methods for state-specific behavior
 * - Practical: Order processing lifecycle
 */
public class EnumStateMachineDemo {

    // State Machine Enum — each state knows its valid next states
    enum OrderState {
        PLACED("Order has been placed") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(PAID, CANCELLED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] New order created. Awaiting payment.");
            }
        },
        PAID("Payment received") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(PROCESSING, REFUNDED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Payment confirmed! Preparing order.");
            }
        },
        PROCESSING("Order being prepared") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(SHIPPED, CANCELLED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Order is being prepared in warehouse.");
            }
        },
        SHIPPED("Order shipped") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(DELIVERED, RETURNED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Package handed to courier!");
            }
        },
        DELIVERED("Order delivered") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(RETURNED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Delivered! Thank you for your order.");
            }
        },
        CANCELLED("Order cancelled") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.noneOf(OrderState.class); // terminal state
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Order has been cancelled.");
            }
        },
        REFUNDED("Payment refunded") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.noneOf(OrderState.class); // terminal state
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Refund processed to original payment.");
            }
        },
        RETURNED("Order returned") {
            @Override
            public Set<OrderState> allowedTransitions() {
                return EnumSet.of(REFUNDED);
            }
            @Override
            public void onEnter(String orderId) {
                System.out.println("  [" + orderId + "] Return received. Processing refund.");
            }
        };

        private final String description;

        OrderState(String description) {
            this.description = description;
        }

        public String getDescription() { return description; }

        // Each state defines its valid transitions
        public abstract Set<OrderState> allowedTransitions();

        // Each state defines entry behavior
        public abstract void onEnter(String orderId);

        // Check if transition is valid
        public boolean canTransitionTo(OrderState next) {
            return allowedTransitions().contains(next);
        }

        public boolean isTerminal() {
            return allowedTransitions().isEmpty();
        }
    }

    // Order class that uses the state machine
    static class Order {
        private final String orderId;
        private OrderState currentState;
        private final List<String> history = new ArrayList<>();

        public Order(String orderId) {
            this.orderId = orderId;
            this.currentState = OrderState.PLACED;
            currentState.onEnter(orderId);
            history.add(currentState.name());
        }

        public void transitionTo(OrderState newState) {
            if (currentState.canTransitionTo(newState)) {
                System.out.printf("  Transition: %s -> %s%n", currentState, newState);
                currentState = newState;
                currentState.onEnter(orderId);
                history.add(currentState.name());
            } else {
                System.out.printf("  INVALID: Cannot go from %s to %s!%n", currentState, newState);
                System.out.printf("  Allowed transitions from %s: %s%n",
                    currentState, currentState.allowedTransitions());
            }
        }

        public void printHistory() {
            System.out.println("  Order " + orderId + " history: " + String.join(" -> ", history));
        }

        public OrderState getState() { return currentState; }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. State Transition Map");
        System.out.println("==================================================");
        for (OrderState state : OrderState.values()) {
            System.out.printf("  %-12s -> %s %s%n",
                state, state.allowedTransitions(),
                state.isTerminal() ? "(TERMINAL)" : "");
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Happy Path — Successful Order");
        System.out.println("==================================================");
        Order order1 = new Order("ORD-001");
        order1.transitionTo(OrderState.PAID);
        order1.transitionTo(OrderState.PROCESSING);
        order1.transitionTo(OrderState.SHIPPED);
        order1.transitionTo(OrderState.DELIVERED);
        order1.printHistory();

        System.out.println("\n==================================================");
        System.out.println(" 3. Cancellation Path");
        System.out.println("==================================================");
        Order order2 = new Order("ORD-002");
        order2.transitionTo(OrderState.CANCELLED);
        order2.printHistory();
        System.out.println("  Is terminal? " + order2.getState().isTerminal());

        System.out.println("\n==================================================");
        System.out.println(" 4. Invalid Transitions (Rejected)");
        System.out.println("==================================================");
        Order order3 = new Order("ORD-003");
        order3.transitionTo(OrderState.SHIPPED);    // Can't skip payment!
        order3.transitionTo(OrderState.DELIVERED);   // Can't skip everything!

        System.out.println("\n==================================================");
        System.out.println(" 5. Return & Refund Path");
        System.out.println("==================================================");
        Order order4 = new Order("ORD-004");
        order4.transitionTo(OrderState.PAID);
        order4.transitionTo(OrderState.PROCESSING);
        order4.transitionTo(OrderState.SHIPPED);
        order4.transitionTo(OrderState.DELIVERED);
        order4.transitionTo(OrderState.RETURNED);
        order4.transitionTo(OrderState.REFUNDED);
        order4.printHistory();

        System.out.println("\n==================================================");
        System.out.println(" KEY TAKEAWAYS:");
        System.out.println("==================================================");
        System.out.println("1. Enum State Machines embed transition rules in enum.");
        System.out.println("2. Each state defines allowed transitions via abstract method.");
        System.out.println("3. Invalid transitions are caught at runtime.");
        System.out.println("4. Abstract onEnter() enables state-specific behavior.");
        System.out.println("5. Terminal states have empty allowedTransitions().");
    }
}
