package DesignPatterns;

/**
 * Demonstrates the Adapter Design Pattern (Structural Pattern).
 * 
 * Allows incompatible interfaces to collaborate by converting the interface of a class
 * into another interface that clients expect.
 * 
 * Features:
 * 1. Object Adapter (Composition-based): Adapts legacy XML payment gateway into modern JSON API.
 * 2. Class Adapter (Inheritance-based): Adapts temperature sensor metrics.
 */
public class AdapterPatternDemo {

    // =========================================================================
    // Example 1: Payment Gateway Adapter (Object Adapter Pattern)
    // =========================================================================

    /**
     * Target Interface expected by modern e-commerce clients.
     */
    public interface ModernPaymentProcessor {
        void processPayment(String customerId, double amountInUsd, String currency);
    }

    /**
     * Adaptee: Legacy 3rd-party service using XML payload format and cents.
     */
    public static class LegacyXmlPaymentGateway {
        public void executeXmlTransaction(String xmlPayload) {
            System.out.println("  [LegacyGateway] Transmitting raw XML over SOAP endpoint:");
            System.out.println("    " + xmlPayload);
            System.out.println("  [LegacyGateway] Transaction successfully approved by banking mainframe.");
        }
    }

    /**
     * Object Adapter: Wraps legacy gateway instance and converts modern call parameters into XML.
     */
    public static class PaymentGatewayAdapter implements ModernPaymentProcessor {
        private final LegacyXmlPaymentGateway legacyGateway;

        public PaymentGatewayAdapter(LegacyXmlPaymentGateway legacyGateway) {
            this.legacyGateway = legacyGateway;
        }

        @Override
        public void processPayment(String customerId, double amountInUsd, String currency) {
            long amountInCents = Math.round(amountInUsd * 100);
            String xml = String.format(
                "<Transaction><Customer>%s</Customer><AmountCents>%d</AmountCents><Currency>%s</Currency></Transaction>",
                customerId, amountInCents, currency
            );
            System.out.println("  [Adapter] Transforming JSON client request into legacy XML format...");
            legacyGateway.executeXmlTransaction(xml);
        }
    }

    // =========================================================================
    // Example 2: Sensor Adapter (Class Adapter via Inheritance)
    // =========================================================================

    /**
     * Target Interface: Metric Weather Station (°Celsius).
     */
    public interface MetricTemperatureSensor {
        double getTemperatureCelsius();
    }

    /**
     * Adaptee: Imperial hardware sensor returning °Fahrenheit.
     */
    public static class FahrenheitHardwareSensor {
        public double readFahrenheit() {
            // Simulated sensor reading (e.g., 98.6°F)
            return 98.6;
        }
    }

    /**
     * Class Adapter: Inherits from Fahrenheit sensor and implements Metric interface.
     */
    public static class TemperatureSensorAdapter extends FahrenheitHardwareSensor implements MetricTemperatureSensor {
        @Override
        public double getTemperatureCelsius() {
            double fahrenheit = readFahrenheit();
            double celsius = (fahrenheit - 32) * 5.0 / 9.0;
            return Math.round(celsius * 100.0) / 100.0;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Adapter Design Pattern Demonstration ===\n");

        // 1. Payment Gateway Adapter (Object Adapter)
        System.out.println("--- 1. Object Adapter: Modern Payment to Legacy XML ---");
        LegacyXmlPaymentGateway legacyGateway = new LegacyXmlPaymentGateway();
        ModernPaymentProcessor paymentProcessor = new PaymentGatewayAdapter(legacyGateway);

        System.out.println("Client invoking: paymentProcessor.processPayment(\"CUST_8821\", 149.95, \"USD\"):");
        paymentProcessor.processPayment("CUST_8821", 149.95, "USD");

        // 2. Sensor Adapter (Class Adapter)
        System.out.println("\n--- 2. Class Adapter: Imperial Fahrenheit to Metric Celsius ---");
        MetricTemperatureSensor weatherStation = new TemperatureSensorAdapter();
        System.out.printf("Weather Station Reading (Metric): %.2f deg C%n", weatherStation.getTemperatureCelsius());
    }
}
