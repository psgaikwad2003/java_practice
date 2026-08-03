class Book{

    int bookId;
    String bookName;
    boolean isAvailable;

    Book(int bookId, String bookName){
        this.bookId = bookId;
        this.bookName = bookName;
    }

    void issuedBook(){

        if(!isAvailable){
            isAvailable = true;
            System.out.println("Book issued successfully.");
        }else{
            System.out.println("Book is not available for issuing.");
        }
    }
    void returnBook(){

        if(isAvailable){
            isAvailable = false;
            System.out.println("Book returned successfully.");
        }else{
            System.out.println("Book is not issued yet.");
        }
    }
    void displayBookDetails(){

        System.out.println("Book ID: " + bookId);
        System.out.println("Book Name: " + bookName);
        System.out.println("Availability: " + (isAvailable ? "Available" : "Not Available"));
    }
}

public class oops1 {
    
    public static void main(String[] args) {
        
        Book book1 = new Book(101, "Java Programming");
        Book book2 = new Book(102, "Python Programming");

        book1.displayBookDetails();
        book1.issuedBook();
        book1.displayBookDetails();
        book1.returnBook();
        book1.displayBookDetails();

        System.out.println();

        book2.displayBookDetails();
        book2.issuedBook();
        book2.displayBookDetails();
        book2.returnBook();
        book2.displayBookDetails();
    }
}
