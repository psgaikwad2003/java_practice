package DesignPatterns;

/**
 * Demonstrates the Factory Method and Abstract Factory Design Patterns.
 * 
 * 1. Factory Method: Defines an interface for creating an object, but lets subclasses
 *    decide which class to instantiate (Document Exporter Example).
 * 2. Abstract Factory: Provides an interface for creating families of related or
 *    dependent objects without specifying their concrete classes (Cross-Platform GUI Example).
 */
public class FactoryPatternDemo {

    // =========================================================================
    // PART 1: Factory Method Pattern (Document Exporters)
    // =========================================================================

    public interface Document {
        String formatContent(String rawText);
        String getExtension();
    }

    public static class PDFDocument implements Document {
        @Override
        public String formatContent(String rawText) {
            return "[PDF Header]\n  " + rawText + "\n[PDF Footer -- Embedded Fonts & Layout]";
        }

        @Override
        public String getExtension() {
            return ".pdf";
        }
    }

    public static class CSVDocument implements Document {
        @Override
        public String formatContent(String rawText) {
            return "ID,RECORD_DATA\n1,\"" + rawText.replace("\n", " ") + "\"";
        }

        @Override
        public String getExtension() {
            return ".csv";
        }
    }

    public static class JSONDocument implements Document {
        @Override
        public String formatContent(String rawText) {
            return "{\n  \"status\": \"success\",\n  \"payload\": \"" + rawText + "\"\n}";
        }

        @Override
        public String getExtension() {
            return ".json";
        }
    }

    public abstract static class DocumentCreator {
        // Factory Method
        public abstract Document createDocument();

        // Core business logic relying on product interface
        public void export(String content) {
            Document doc = createDocument();
            System.out.printf("Exporting file type '%s':%n%s%n%n", doc.getExtension(), doc.formatContent(content));
        }
    }

    public static class PDFDocumentCreator extends DocumentCreator {
        @Override
        public Document createDocument() {
            return new PDFDocument();
        }
    }

    public static class CSVDocumentCreator extends DocumentCreator {
        @Override
        public Document createDocument() {
            return new CSVDocument();
        }
    }

    public static class JSONDocumentCreator extends DocumentCreator {
        @Override
        public Document createDocument() {
            return new JSONDocument();
        }
    }

    // =========================================================================
    // PART 2: Abstract Factory Pattern (Cross-Platform UI Widgets)
    // =========================================================================

    public interface Button {
        void render();
        void onClick();
    }

    public interface Checkbox {
        void render();
        void toggle();
    }

    // Windows Theme
    public static class WindowsButton implements Button {
        @Override
        public void render() {
            System.out.println("  [Windows Button] Rendering classic Fluent rectangular button.");
        }
        @Override
        public void onClick() {
            System.out.println("  [Windows Button] Click sound triggered.");
        }
    }

    public static class WindowsCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("  [Windows Checkbox] Rendering square box with check tick.");
        }
        @Override
        public void toggle() {
            System.out.println("  [Windows Checkbox] Toggled checked state.");
        }
    }

    // MacOS Theme
    public static class MacButton implements Button {
        @Override
        public void render() {
            System.out.println("  [macOS Button] Rendering rounded Aqua smooth button.");
        }
        @Override
        public void onClick() {
            System.out.println("  [macOS Button] Haptic pulse effect triggered.");
        }
    }

    public static class MacCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("  [macOS Checkbox] Rendering rounded pill style toggle.");
        }
        @Override
        public void toggle() {
            System.out.println("  [macOS Checkbox] Smooth spring transition animated.");
        }
    }

    // Abstract Factory Interface
    public interface GUIFactory {
        Button createButton();
        Checkbox createCheckbox();
    }

    public static class WindowsFactory implements GUIFactory {
        @Override
        public Button createButton() {
            return new WindowsButton();
        }
        @Override
        public Checkbox createCheckbox() {
            return new WindowsCheckbox();
        }
    }

    public static class MacFactory implements GUIFactory {
        @Override
        public Button createButton() {
            return new MacButton();
        }
        @Override
        public Checkbox createCheckbox() {
            return new MacCheckbox();
        }
    }

    // Client application configured with Abstract Factory
    public static class Application {
        private final Button button;
        private final Checkbox checkbox;

        public Application(GUIFactory factory) {
            this.button = factory.createButton();
            this.checkbox = factory.createCheckbox();
        }

        public void paint() {
            button.render();
            button.onClick();
            checkbox.render();
            checkbox.toggle();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Factory Patterns Demonstration ===\n");

        // 1. Factory Method Demo
        System.out.println("--- 1. Factory Method: Document Exporters ---");
        DocumentCreator pdfCreator = new PDFDocumentCreator();
        pdfCreator.export("Financial Quarterly Report 2026");

        DocumentCreator jsonCreator = new JSONDocumentCreator();
        jsonCreator.export("UserData: { name: Alice, role: Admin }");

        DocumentCreator csvCreator = new CSVDocumentCreator();
        csvCreator.export("Transaction 499.99 Completed");

        // 2. Abstract Factory Demo
        System.out.println("--- 2. Abstract Factory: Multi-Platform GUI ---");
        System.out.println("Initializing Application on Windows platform:");
        GUIFactory winFactory = new WindowsFactory();
        Application winApp = new Application(winFactory);
        winApp.paint();

        System.out.println("\nInitializing Application on macOS platform:");
        GUIFactory macFactory = new MacFactory();
        Application macApp = new Application(macFactory);
        macApp.paint();
    }
}
