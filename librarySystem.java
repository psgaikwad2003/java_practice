import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


class Book {
    private final int id;
    private final String title;
    private final String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    
    public boolean issueBook() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Book \"" + title + "\" issued successfully.");
            return true;
        } else {
            System.out.println("Book \"" + title + "\" is already issued.");
            return false;
        }
    }

    public boolean returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Book \"" + title + "\" returned successfully.");
            return true;
        } else {
            System.out.println("Book \"" + title + "\" was not issued.");
            return false;
        }
    }

    public void display() {
        System.out.printf("ID: %-3d | Title: %-20s | Author: %-18s | Status: %s%n",
            id, title, author, (isAvailable ? "Available" : "Issued"));
    }
}

class Library {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public Optional<Book> findById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst();
    }

    public List<Book> searchByTitleOrAuthor(String keyword) {
        String lower = keyword.toLowerCase();
        return books.stream()
            .filter(b -> b.getTitle().toLowerCase().contains(lower) || b.getAuthor().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public void displayAllBooks() {
        books.forEach(Book::display);
    }
}

public class librarySystem {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book(101, "Java Programming", "James Gosling"));
        library.addBook(new Book(102, "Data Structures", "Mark Allen Weiss"));
        library.addBook(new Book(103, "Clean Code", "Robert C. Martin"));

        System.out.println("========== Library Management System ==========");

        System.out.println("\n--- All Books ---");
        library.displayAllBooks();

        System.out.println("\n--- Search Results for 'Clean' ---");
        library.searchByTitleOrAuthor("Clean").forEach(Book::display);

        System.out.println("\n--- Issuing Book ID 101 ---");
        library.findById(101).ifPresent(Book::issueBook);
        library.findById(101).ifPresent(Book::issueBook); 

        System.out.println("\n--- Returning Book ID 101 ---");
        library.findById(101).ifPresent(Book::returnBook);

        System.out.println("\n--- Final Library Catalog Status ---");
        library.displayAllBooks();

        System.out.println("================================================");
    }
}
