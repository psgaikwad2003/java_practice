class BankAccount{

    private double balance;

    public void deposit(double amount){

        if(amount>0){
            balance+=amount;
            System.out.println("Deposited: "+amount);
        }
    }

    public double getBalance(){
        return balance;
    }
}

public class Encapsulation{

    public static void main(String[] args) {
        BankAccount acc = new BankAccount();
        acc.deposit(5000);
        System.out.println("Balance: "+acc.getBalance());
    }
}