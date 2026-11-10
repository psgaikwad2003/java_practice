public class constructorOverloading {

    private final String isbn;
    private final String title;
    private final String author;
    private final double price;
    private final int stock;

    public constructorOverloading() {
        this("000-000", "Untitled", "Unknown Author", 0.0, 0);
    }

    public constructorOverloading(String title) {
        this("000-000", title, "Unknown Author", 9.99, 10);
    }

    public constructorOverloading(String title, double price) {
        this("000-000", title, "Unknown Author", price, 10);
    }

    public constructorOverloading(String title, String author, double price) {
        this("000-000", title, author, price, 10);
    }

    public constructorOverloading(String isbn, String title, String author, double price, int stock) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative: " + price);
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative: " + stock);
        this.isbn = isbn != null ? isbn : "000-000";
        this.title = (title != null && !title.isBlank()) ? title.trim() : "Untitled";
        this.author = (author != null && !author.isBlank()) ? author.trim() : "Unknown Author";
        this.price = price;
        this.stock = stock;
    }

    public double calculateDiscountedPrice(double percentDiscount) {
        if (percentDiscount < 0 || percentDiscount > 100) return price;
        return price * (1.0 - percentDiscount / 100.0);
    }

    public void display() {
        System.out.printf("ISBN: %-10s | Title: %-25s | Author: %-16s | Price: $%6.2f | Stock: %3d%n",
            isbn, title, author, price, stock);
    }

    public static void main(String[] args) {
        System.out.println("=== Book Catalog Constructor Overloading ===");

        constructorOverloading b1 = new constructorOverloading();
        constructorOverloading b2 = new constructorOverloading("Clean Architecture");
        constructorOverloading b3 = new constructorOverloading("Effective Java", 45.00);
        constructorOverloading b4 = new constructorOverloading("Java Concurrency", "Brian Goetz", 52.50);
        constructorOverloading b5 = new constructorOverloading("978-0134685991", "Design Patterns", "GoF", 59.99, 25);

        constructorOverloading[] catalog = {b1, b2, b3, b4, b5};
        for (constructorOverloading book : catalog) {
            book.display();
        }

        System.out.println("\nDiscount Calculation Example:");
        System.out.printf("Effective Java with 15%% discount: $%.2f%n", b3.calculateDiscountedPrice(15));
    }
}
