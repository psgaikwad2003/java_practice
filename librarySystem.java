import java.util.Scanner;

// Library Management System - extends classObject.java and Encapsulation.java concepts
class Book {
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    // Issue and Return methods
    public void issueBook() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Book \"" + title + "\" issued successfully.");
        } else {
            System.out.println("Book \"" + title + "\" is already issued.");
        }
    }

    public void returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Book \"" + title + "\" returned successfully.");
        } else {
            System.out.println("Book \"" + title + "\" was not issued.");
        }
    }

    public void display() {
        System.out.println("ID: " + id + " | Title: " + title + " | Author: " + author
                + " | Status: " + (isAvailable ? "Available" : "Issued"));
    }
}

public class librarySystem {
    public static void main(String[] args) {

        Book[] books = {
            new Book(101, "Java Programming", "James Gosling"),
            new Book(102, "Data Structures", "Mark Allen Weiss"),
            new Book(103, "Clean Code", "Robert C. Martin")
        };

        System.out.println("========== Library Management System ==========");

        System.out.println("\n--- All Books ---");
        for (Book b : books) {
            b.display();
        }

        System.out.println("\n--- Issuing Books ---");
        books[0].issueBook();
        books[1].issueBook();
        books[0].issueBook(); // Try to issue again

        System.out.println("\n--- After Issuing ---");
        for (Book b : books) {
            b.display();
        }

        System.out.println("\n--- Returning Book ---");
        books[0].returnBook();

        System.out.println("\n--- Final Status ---");
        for (Book b : books) {
            b.display();
        }

        System.out.println("================================================");
    }
}
