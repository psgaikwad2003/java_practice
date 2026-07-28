import java.util.Scanner;

public class Program5 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Take array size
        System.out.print("Enter array size: ");
        int size = sc.nextInt();

        // Create array
        int[] numbers = new int[size];

        // Take array elements
        System.out.println("Enter " + size + " elements:");
        for (int i = 0; i < size; i++) {
            numbers[i] = sc.nextInt();
        }

        // Take search element
        System.out.print("Enter number to search: ");
        int search = sc.nextInt();

        boolean found = false;

        // Search using enhanced for loop
        for (int number : numbers) {
            if (number == search) {
                found = true;
                break;
            }
        }

        if (found)
            System.out.println(search + " Found");
        else
            System.out.println(search + " Not Found");

        sc.close();
    }
}