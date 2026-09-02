import java.util.Scanner;

public class atm {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        int balance=10000;
        int choice;
        int amount;

        do{
            System.out.println("ATM Machine");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice=sc.nextInt();

            switch(choice){
                case 1:
                    System.out.println("Your balance is: "+balance);
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    amount=sc.nextInt();
                    balance+=amount;
                    System.out.println("Amount deposited successfully.");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    amount=sc.nextInt();
                    if(amount>balance){
                        System.out.println("Insufficient balance.");
                    }else{
                        balance-=amount;
                        System.out.println("Amount withdrawn successfully.");
                    }
                    break;
                case 4:
                    System.out.println("Thank you for using ATM Machine.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            if (choice ==4){
                break;
            }
        }while(true);
        sc.close();
    }
    
}
