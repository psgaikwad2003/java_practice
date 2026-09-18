package DesignPatterns;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates the State Design Pattern (Behavioral Pattern).
 * 
 * Intent:
 * Allows an object to alter its behavior when its internal state changes.
 * The object will appear to change its class, replacing bulky if-else or switch
 * state management with encapsulated polymorphic state objects.
 * 
 * Scenario:
 * A Smart Vending Machine with dynamic state transitions:
 * 1. IdleState: Awaiting payment.
 * 2. HasMoneyState: Payment received; waiting for item selection or refund request.
 * 3. DispensingState: Validating price, deducting credit, and dispensing product.
 * 4. SoldOutState: Inventory exhausted; blocks purchases until restocked.
 */
public class StatePatternDemo {

    // =========================================================================
    // Domain Model: Item
    // =========================================================================
    public static class Item {
        private final String code;
        private final String name;
        private final int priceCents;
        private int stock;

        public Item(String code, String name, int priceCents, int stock) {
            this.code = code;
            this.name = name;
            this.priceCents = priceCents;
            this.stock = stock;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public int getPriceCents() { return priceCents; }
        public int getStock() { return stock; }

        public void decrementStock() {
            if (stock > 0) stock--;
        }

        public void addStock(int count) {
            this.stock += count;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s ($%.2f) - Stock: %d", code, name, priceCents / 100.0, stock);
        }
    }

    // =========================================================================
    // State Interface
    // =========================================================================
    public interface VendingMachineState {
        void insertMoney(VendingMachine context, int amountCents);
        void ejectMoney(VendingMachine context);
        void selectItem(VendingMachine context, String itemCode);
        void dispense(VendingMachine context);
        String getStateName();
    }

    // =========================================================================
    // Context: VendingMachine
    // =========================================================================
    public static class VendingMachine {
        private final Map<String, Item> inventory = new HashMap<>();
        private int balanceCents = 0;
        private String selectedItemCode = null;

        // State instances
        private final VendingMachineState idleState;
        private final VendingMachineState hasMoneyState;
        private final VendingMachineState dispensingState;
        private final VendingMachineState soldOutState;

        private VendingMachineState currentState;

        public VendingMachine() {
            this.idleState = new IdleState();
            this.hasMoneyState = new HasMoneyState();
            this.dispensingState = new DispensingState();
            this.soldOutState = new SoldOutState();

            // Initial state
            this.currentState = idleState;
        }

        public void addItem(Item item) {
            inventory.put(item.getCode(), item);
            if (hasStock() && currentState == soldOutState) {
                setState(idleState);
            }
        }

        public void setState(VendingMachineState newState) {
            System.out.printf("  [Transition] %s -> %s%n", 
                    currentState.getStateName(), newState.getStateName());
            this.currentState = newState;
        }

        // Delegate actions to current state
        public void insertMoney(int amountCents) {
            currentState.insertMoney(this, amountCents);
        }

        public void ejectMoney() {
            currentState.ejectMoney(this);
        }

        public void selectItem(String itemCode) {
            currentState.selectItem(this, itemCode);
        }

        public void dispense() {
            currentState.dispense(this);
        }

        // Helper methods for state manipulation
        public void addBalance(int cents) {
            this.balanceCents += cents;
        }

        public int getBalanceCents() {
            return balanceCents;
        }

        public void resetBalance() {
            this.balanceCents = 0;
        }

        public String getSelectedItemCode() {
            return selectedItemCode;
        }

        public void setSelectedItemCode(String selectedItemCode) {
            this.selectedItemCode = selectedItemCode;
        }

        public Item getItem(String code) {
            return inventory.get(code);
        }

        public boolean hasStock() {
            for (Item item : inventory.values()) {
                if (item.getStock() > 0) return true;
            }
            return false;
        }

        // State Getters
        public VendingMachineState getIdleState() { return idleState; }
        public VendingMachineState getHasMoneyState() { return hasMoneyState; }
        public VendingMachineState getDispensingState() { return dispensingState; }
        public VendingMachineState getSoldOutState() { return soldOutState; }
        public VendingMachineState getCurrentState() { return currentState; }

        public void displayInventory() {
            System.out.println("  Current Inventory:");
            inventory.values().forEach(item -> System.out.println("    " + item));
        }
    }

    // =========================================================================
    // Concrete State 1: IdleState
    // =========================================================================
    public static class IdleState implements VendingMachineState {
        @Override
        public void insertMoney(VendingMachine context, int amountCents) {
            if (amountCents <= 0) {
                System.out.println("  [Error] Insert a valid positive amount.");
                return;
            }
            context.addBalance(amountCents);
            System.out.printf("  Inserted $%.2f. Current balance: $%.2f%n", 
                    amountCents / 100.0, context.getBalanceCents() / 100.0);
            context.setState(context.getHasMoneyState());
        }

        @Override
        public void ejectMoney(VendingMachine context) {
            System.out.println("  [Warning] No money inserted to return.");
        }

        @Override
        public void selectItem(VendingMachine context, String itemCode) {
            System.out.println("  [Warning] Please insert money before selecting an item.");
        }

        @Override
        public void dispense(VendingMachine context) {
            System.out.println("  [Warning] Insert money and choose an item first.");
        }

        @Override
        public String getStateName() { return "IDLE"; }
    }

    // =========================================================================
    // Concrete State 2: HasMoneyState
    // =========================================================================
    public static class HasMoneyState implements VendingMachineState {
        @Override
        public void insertMoney(VendingMachine context, int amountCents) {
            if (amountCents <= 0) {
                System.out.println("  [Error] Insert a valid positive amount.");
                return;
            }
            context.addBalance(amountCents);
            System.out.printf("  Added $%.2f. Current balance: $%.2f%n", 
                    amountCents / 100.0, context.getBalanceCents() / 100.0);
        }

        @Override
        public void ejectMoney(VendingMachine context) {
            System.out.printf("  Refunding $%.2f. Come back soon!%n", context.getBalanceCents() / 100.0);
            context.resetBalance();
            context.setSelectedItemCode(null);
            context.setState(context.getIdleState());
        }

        @Override
        public void selectItem(VendingMachine context, String itemCode) {
            Item item = context.getItem(itemCode);
            if (item == null) {
                System.out.printf("  [Error] Unknown item code: '%s'.%n", itemCode);
                return;
            }

            if (item.getStock() <= 0) {
                System.out.printf("  [Out of Stock] '%s' is out of stock! Choose another item or refund.%n", item.getName());
                return;
            }

            if (context.getBalanceCents() < item.getPriceCents()) {
                int needed = item.getPriceCents() - context.getBalanceCents();
                System.out.printf("  [Insufficient Funds] '%s' costs $%.2f. Insert $%.2f more.%n", 
                        item.getName(), item.getPriceCents() / 100.0, needed / 100.0);
                return;
            }

            // Funds sufficient and item in stock
            System.out.printf("  Item '%s' selected.%n", item.getName());
            context.setSelectedItemCode(itemCode);
            context.setState(context.getDispensingState());
            // Automatically trigger dispensing
            context.dispense();
        }

        @Override
        public void dispense(VendingMachine context) {
            System.out.println("  [Warning] Please select an item to dispense.");
        }

        @Override
        public String getStateName() { return "HAS_MONEY"; }
    }

    // =========================================================================
    // Concrete State 3: DispensingState
    // =========================================================================
    public static class DispensingState implements VendingMachineState {
        @Override
        public void insertMoney(VendingMachine context, int amountCents) {
            System.out.println("  [Busy] Currently dispensing. Please wait.");
        }

        @Override
        public void ejectMoney(VendingMachine context) {
            System.out.println("  [Busy] Already dispensing your selection. Cannot refund now.");
        }

        @Override
        public void selectItem(VendingMachine context, String itemCode) {
            System.out.println("  [Busy] Already dispensing. Please wait.");
        }

        @Override
        public void dispense(VendingMachine context) {
            String code = context.getSelectedItemCode();
            Item item = context.getItem(code);

            if (item != null && item.getStock() > 0) {
                item.decrementStock();
                int remainingChange = context.getBalanceCents() - item.getPriceCents();
                System.out.printf("  [Dispensed] Enjoy your %s!%n", item.getName());

                if (remainingChange > 0) {
                    System.out.printf("  [Change Returned] Returning $%.2f in change.%n", remainingChange / 100.0);
                }
                context.resetBalance();
                context.setSelectedItemCode(null);

                // Check remaining overall stock
                if (!context.hasStock()) {
                    context.setState(context.getSoldOutState());
                } else {
                    context.setState(context.getIdleState());
                }
            } else {
                System.out.println("  [Error] Failed to dispense item.");
                context.setState(context.getIdleState());
            }
        }

        @Override
        public String getStateName() { return "DISPENSING"; }
    }

    // =========================================================================
    // Concrete State 4: SoldOutState
    // =========================================================================
    public static class SoldOutState implements VendingMachineState {
        @Override
        public void insertMoney(VendingMachine context, int amountCents) {
            System.out.println("  [Sold Out] Machine is completely out of stock. Money rejected.");
        }

        @Override
        public void ejectMoney(VendingMachine context) {
            if (context.getBalanceCents() > 0) {
                System.out.printf("  Refunding $%.2f.%n", context.getBalanceCents() / 100.0);
                context.resetBalance();
            } else {
                System.out.println("  [Sold Out] No money to return.");
            }
        }

        @Override
        public void selectItem(VendingMachine context, String itemCode) {
            System.out.println("  [Sold Out] Machine is sold out.");
        }

        @Override
        public void dispense(VendingMachine context) {
            System.out.println("  [Sold Out] No products available to dispense.");
        }

        @Override
        public String getStateName() { return "SOLD_OUT"; }
    }

    // =========================================================================
    // Demonstration
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("   STATE DESIGN PATTERN DEMONSTRATION (VENDING MACHINE)   ");
        System.out.println("=========================================================\n");

        VendingMachine vm = new VendingMachine();
        vm.addItem(new Item("A1", "Espresso", 250, 2));      // $2.50, stock: 2
        vm.addItem(new Item("B2", "Sparkling Water", 150, 1)); // $1.50, stock: 1

        vm.displayInventory();
        System.out.println("\nInitial State: " + vm.getCurrentState().getStateName());

        System.out.println("\n--- Scenario 1: Attempting to select without inserting money ---");
        vm.selectItem("A1");

        System.out.println("\n--- Scenario 2: Successful purchase with change return ---");
        vm.insertMoney(100); // $1.00
        vm.insertMoney(200); // $2.00 more (total $3.00)
        vm.selectItem("A1"); // costs $2.50, change $0.50

        System.out.println("\n--- Scenario 3: Insufficient funds & cancellation refund ---");
        vm.insertMoney(100); // $1.00
        vm.selectItem("A1"); // costs $2.50 -> needs $1.50 more
        vm.ejectMoney();     // refunds $1.00

        System.out.println("\n--- Scenario 4: Purchasing until out of stock ---");
        vm.insertMoney(250);
        vm.selectItem("A1"); // stock reaches 0

        vm.insertMoney(150);
        vm.selectItem("B2"); // stock reaches 0 -> machine completely sold out!

        System.out.println("\n--- Scenario 5: Interacting with SOLD_OUT state ---");
        vm.insertMoney(500); // Should be rejected

        System.out.println("\n--- Scenario 6: Restocking and recovery ---");
        System.out.println("  Restocking Espresso with 5 units...");
        Item espresso = vm.getItem("A1");
        espresso.addStock(5);
        // Add item will trigger state transition back to IDLE
        vm.addItem(espresso);

        System.out.println("  Testing machine after restocking:");
        vm.insertMoney(300);
        vm.selectItem("A1");

        System.out.println("\n=========================================================");
        System.out.println("   STATE PATTERN DEMONSTRATION COMPLETED SUCCESSFULLY   ");
        System.out.println("=========================================================");
    }
}
