/**
 * Technical Interview Question: Behavioral & Creational Design Patterns
 * 
 * Demonstrates:
 * 1. Factory Pattern for Notification creation.
 * 2. Strategy Pattern for Payment Processing.
 */
public class DesignPatternsDemo {

    // ==================================================
    // 1. Factory Pattern
    // ==================================================
    interface Notification {
        void send(String message);
    }

    static class EmailNotification implements Notification {
        @Override
        public void send(String message) {
            System.out.println("[EMAIL] Sending message: " + message);
        }
    }

    static class SMSNotification implements Notification {
        @Override
        public void send(String message) {
            System.out.println("[SMS] Sending message: " + message);
        }
    }

    static class NotificationFactory {
        public static Notification createNotification(String channel) {
            if (channel == null || channel.isEmpty()) return null;
            switch (channel.toUpperCase()) {
                case "EMAIL": return new EmailNotification();
                case "SMS":   return new SMSNotification();
                default: throw new IllegalArgumentException("Unknown channel: " + channel);
            }
        }
    }

    // ==================================================
    // 2. Strategy Pattern
    // ==================================================
    interface PaymentStrategy {
        void pay(double amount);
    }

    static class CreditCardPayment implements PaymentStrategy {
        private final String cardNumber;

        public CreditCardPayment(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        @Override
        public void pay(double amount) {
            System.out.printf("Paid $%.2f using Credit Card ending in %s%n",
                amount, cardNumber.substring(cardNumber.length() - 4));
        }
    }

    static class PayPalPayment implements PaymentStrategy {
        private final String email;

        public PayPalPayment(String email) {
            this.email = email;
        }

        @Override
        public void pay(double amount) {
            System.out.printf("Paid $%.2f using PayPal account (%s)%n", amount, email);
        }
    }

    static class ShoppingCart {
        private PaymentStrategy paymentStrategy;

        public void setPaymentStrategy(PaymentStrategy strategy) {
            this.paymentStrategy = strategy;
        }

        public void checkout(double amount) {
            if (paymentStrategy == null) {
                throw new IllegalStateException("Payment strategy not set!");
            }
            paymentStrategy.pay(amount);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. Factory Pattern Demo");
        System.out.println("==================================================");
        Notification emailNotif = NotificationFactory.createNotification("EMAIL");
        emailNotif.send("Your order has shipped!");

        Notification smsNotif = NotificationFactory.createNotification("SMS");
        smsNotif.send("OTP for login: 49201");

        System.out.println("\n==================================================");
        System.out.println(" 2. Strategy Pattern Demo");
        System.out.println("==================================================");
        ShoppingCart cart = new ShoppingCart();

        cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
        cart.checkout(149.99);

        cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
        cart.checkout(29.99);
    }
}
