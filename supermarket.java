import java.util.Scanner;

// Supermarket Billing System - extends atm.java and electricity.java style
public class supermarket {
    public static void main(String[] args) {

        // Product list: {name, price, quantity}
        String[] products = {"Rice (1kg)", "Milk (1L)", "Bread", "Eggs (12pcs)", "Sugar (1kg)"};
        double[] prices   = {60.0, 55.0, 35.0, 80.0, 45.0};
        int[]    quantity = {2,    3,     1,    2,     1};

        System.out.println("============================================");
        System.out.println("       SUPERMARKET BILLING SYSTEM           ");
        System.out.println("============================================");
        System.out.printf("%-20s %8s %5s %10s%n", "Item", "Price", "Qty", "Total");
        System.out.println("--------------------------------------------");

        double grandTotal = 0;

        for (int i = 0; i < products.length; i++) {
            double total = prices[i] * quantity[i];
            grandTotal += total;
            System.out.printf("%-20s %8.2f %5d %10.2f%n",
                    products[i], prices[i], quantity[i], total);
        }

        System.out.println("--------------------------------------------");
        System.out.printf("%-20s %24.2f%n", "Grand Total:", grandTotal);

        // Discount logic
        double discount = 0;
        if (grandTotal > 500) {
            discount = grandTotal * 0.10;
            System.out.printf("%-20s %24.2f%n", "Discount (10%):", discount);
        }

        double finalAmount = grandTotal - discount;
        System.out.printf("%-20s %24.2f%n", "Final Amount:", finalAmount);
        System.out.println("============================================");
        System.out.println("     Thank you for shopping with us!        ");
        System.out.println("============================================");
    }
}
