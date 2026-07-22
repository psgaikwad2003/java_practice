import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;
        int num1;
        int num2;

        do {

            System.out.println("\n========== Calculator ==========");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Modulus");
            System.out.println("6. Exit");
            System.out.println("================================");

            System.out.print("Enter Your Choice : ");
            choice = sc.nextInt();

            if (choice >= 1 && choice <= 5) {

                System.out.print("Enter First Number : ");
                num1 = sc.nextInt();

                System.out.print("Enter Second Number : ");
                num2 = sc.nextInt();

                switch (choice) {

                    case 1:
                        System.out.println("Addition = " + (num1 + num2));
                        break;

                    case 2:
                        System.out.println("Subtraction = " + (num1 - num2));
                        break;

                    case 3:
                        System.out.println("Multiplication = " + (num1 * num2));
                        break;

                    case 4:

                        if (num2 == 0) {

                            System.out.println("Division by Zero is not possible.");

                        } else {

                            double result = (double) num1 / num2;
                            System.out.println("Division = " + result);

                        }

                        break;

                    case 5:

                        if (num2 == 0) {

                            System.out.println("Modulus by Zero is not possible.");

                        } else {

                            System.out.println("Modulus = " + (num1 % num2));

                        }

                        break;
                }

            } else if (choice == 6) {

                System.out.println("Thank You For Using Calculator.");

            } else {

                System.out.println("Invalid Choice.");

            }

        } while (choice != 6);

        sc.close();

    }

}