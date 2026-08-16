import java.util.ArrayList;
import java.util.List;

/**
 * Supermarket Billing System - Refactored with Object-Oriented Principles.
 * Demonstrates encapsulation, list management, tax calculation, and discount strategy.
 */
public class supermarket {

    static class CartItem {
        private final String name;
        private final double unitPrice;
        private final int quantity;

        public CartItem(String name, double unitPrice, int quantity) {
            if (unitPrice < 0 || quantity <= 0) {
                throw new IllegalArgumentException("Invalid price or quantity for item: " + name);
            }
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public double getUnitPrice() { return unitPrice; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return unitPrice * quantity; }
    }

    public static void main(String[] args) {
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem("Rice (1kg)", 60.0, 2));
        cart.add(new CartItem("Milk (1L)", 55.0, 3));
        cart.add(new CartItem("Bread", 35.0, 1));
        cart.add(new CartItem("Eggs (12pcs)", 80.0, 2));
        cart.add(new CartItem("Sugar (1kg)", 45.0, 1));

        System.out.println("============================================");
        System.out.println("       SUPERMARKET BILLING SYSTEM (v2)      ");
        System.out.println("============================================");
        System.out.printf("%-20s %8s %5s %10s%n", "Item", "Price", "Qty", "Total");
        System.out.println("--------------------------------------------");

        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.getTotalPrice();
            System.out.printf("%-20s %8.2f %5d %10.2f%n",
                item.getName(), item.getUnitPrice(), item.getQuantity(), item.getTotalPrice());
        }

        System.out.println("--------------------------------------------");
        System.out.printf("%-20s %24.2f%n", "Subtotal:", subtotal);

        // Tax calculation (5% GST)
        double taxRate = 0.05;
        double taxAmount = subtotal * taxRate;
        System.out.printf("%-20s %24.2f%n", "Tax (5% GST):", taxAmount);

        double totalWithTax = subtotal + taxAmount;

        // Tiered Discount logic
        double discount = 0;
        if (subtotal > 500) {
            discount = subtotal * 0.10; // 10% discount for orders over 500
            System.out.printf("%-20s %24.2f%n", "Discount (10%):", discount);
        }

        double finalAmount = totalWithTax - discount;
        System.out.printf("%-20s %24.2f%n", "Final Amount Payable:", finalAmount);
        System.out.println("============================================");
        System.out.println("     Thank you for shopping with us!        ");
        System.out.println("============================================");
    }
}
