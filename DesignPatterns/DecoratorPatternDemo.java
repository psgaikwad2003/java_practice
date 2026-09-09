import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Demonstrates the Decorator Structural Design Pattern.
 *
 * Core Concept:
 * - Attaches additional responsibilities and behavior to an object dynamically.
 * - Provides a flexible, composable alternative to subclassing for extending functionality.
 * - Adheres to the Open/Closed Principle (classes open for extension, closed for modification).
 *
 * Scenarios implemented:
 * 1. Beverage Order Customizer (Coffee Shop condiment stack with dynamic pricing).
 * 2. Data Stream Processing Pipeline (Encryption + Compression wrapper around raw storage).
 */
public class DecoratorPatternDemo {

    // =========================================================================
    // SCENARIO 1: BEVERAGE CUSTOMIZER
    // =========================================================================

    public interface Beverage {
        String getDescription();
        double cost();
    }

    public static class Espresso implements Beverage {
        @Override
        public String getDescription() { return "Rich Espresso"; }
        @Override
        public double cost() { return 1.99; }
    }

    public static class DarkRoast implements Beverage {
        @Override
        public String getDescription() { return "Dark Roast Coffee"; }
        @Override
        public double cost() { return 2.49; }
    }

    public abstract static class CondimentDecorator implements Beverage {
        protected final Beverage beverage;

        public CondimentDecorator(Beverage beverage) {
            this.beverage = beverage;
        }
    }

    public static class SteamedMilk extends CondimentDecorator {
        public SteamedMilk(Beverage beverage) { super(beverage); }
        @Override
        public String getDescription() { return beverage.getDescription() + " + Steamed Milk"; }
        @Override
        public double cost() { return beverage.cost() + 0.50; }
    }

    public static class Mocha extends CondimentDecorator {
        public Mocha(Beverage beverage) { super(beverage); }
        @Override
        public String getDescription() { return beverage.getDescription() + " + Chocolate Mocha"; }
        @Override
        public double cost() { return beverage.cost() + 0.65; }
    }

    public static class WhippedCream extends CondimentDecorator {
        public WhippedCream(Beverage beverage) { super(beverage); }
        @Override
        public String getDescription() { return beverage.getDescription() + " + Whipped Cream"; }
        @Override
        public double cost() { return beverage.cost() + 0.40; }
    }

    public static class CaramelDrizzle extends CondimentDecorator {
        public CaramelDrizzle(Beverage beverage) { super(beverage); }
        @Override
        public String getDescription() { return beverage.getDescription() + " + Caramel Drizzle"; }
        @Override
        public double cost() { return beverage.cost() + 0.55; }
    }

    // =========================================================================
    // SCENARIO 2: DATA STREAM PIPELINE (ENCRYPTION + COMPRESSION)
    // =========================================================================

    public interface DataSource {
        void writeData(String data);
        String readData();
    }

    public static class InMemoryDataSource implements DataSource {
        private String memoryBuffer = "";

        @Override
        public void writeData(String data) {
            this.memoryBuffer = data;
        }

        @Override
        public String readData() {
            return memoryBuffer;
        }
    }

    public abstract static class DataSourceDecorator implements DataSource {
        protected final DataSource wrappee;

        public DataSourceDecorator(DataSource source) {
            this.wrappee = source;
        }

        @Override
        public void writeData(String data) {
            wrappee.writeData(data);
        }

        @Override
        public String readData() {
            return wrappee.readData();
        }
    }

    public static class EncryptionDecorator extends DataSourceDecorator {
        public EncryptionDecorator(DataSource source) { super(source); }

        @Override
        public void writeData(String data) {
            byte[] encoded = Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
            super.writeData(new String(encoded, StandardCharsets.UTF_8));
        }

        @Override
        public String readData() {
            byte[] decoded = Base64.getDecoder().decode(super.readData());
            return new String(decoded, StandardCharsets.UTF_8);
        }
    }

    public static class CompressionDecorator extends DataSourceDecorator {
        public CompressionDecorator(DataSource source) { super(source); }

        @Override
        public void writeData(String data) {
            try {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                Deflater deflater = new Deflater();
                deflater.setInput(input);
                deflater.finish();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);
                byte[] buffer = new byte[1024];
                while (!deflater.finished()) {
                    int count = deflater.deflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                deflater.end();
                outputStream.close();

                super.writeData(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException("Compression error", e);
            }
        }

        @Override
        public String readData() {
            try {
                byte[] input = Base64.getDecoder().decode(super.readData());
                Inflater inflater = new Inflater();
                inflater.setInput(input);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);
                byte[] buffer = new byte[1024];
                while (!inflater.finished()) {
                    int count = inflater.inflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                inflater.end();
                outputStream.close();

                return outputStream.toString(StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException("Decompression error", e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      DECORATOR PATTERN DEMONSTRATION     ");
        System.out.println("==========================================");

        // Demo 1: Dynamic Beverage Building
        System.out.println("\n--- [1] Coffee Customization ---");
        Beverage order1 = new Espresso();
        System.out.printf("1. Plain: %s -> $%.2f%n", order1.getDescription(), order1.cost());

        // Decorate with Milk and Mocha
        order1 = new SteamedMilk(order1);
        order1 = new Mocha(order1);
        System.out.printf("2. Layered: %s -> $%.2f%n", order1.getDescription(), order1.cost());

        // Decorate Dark Roast with Milk, Mocha, Whip, Caramel
        Beverage luxuryDrink = new CaramelDrizzle(
            new WhippedCream(
                new Mocha(
                    new SteamedMilk(
                        new DarkRoast()
                    )
                )
            )
        );
        System.out.printf("3. Fully Loaded: %s -> $%.2f%n", luxuryDrink.getDescription(), luxuryDrink.cost());

        // Demo 2: Composable Data Pipeline (Raw -> Encrypted -> Compressed)
        System.out.println("\n--- [2] Composable Data Stream Pipeline ---");
        String originalSecret = "TopSecretPayload-SensitiveUserCredentials-2026";
        System.out.println("Original Data: " + originalSecret);

        // Stacked decorator: Compression on top of Encryption on top of Raw Storage
        DataSource pipeline = new CompressionDecorator(
            new EncryptionDecorator(
                new InMemoryDataSource()
            )
        );

        pipeline.writeData(originalSecret);
        System.out.println("Data successfully written through [Compress -> Encrypt -> Storage] pipeline.");

        String retrieved = pipeline.readData();
        System.out.println("Decoded & Read back: " + retrieved);
        System.out.println("Integrity Check: " + originalSecret.equals(retrieved));

        System.out.println("\nAll Decorator Pattern demonstrations completed successfully.");
    }
}
