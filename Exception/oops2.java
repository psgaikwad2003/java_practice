class ATM{

    double balance = 1000;

    void deposit(double amount){

        balance += amount;

    }
    void withdraw(double amount){
        
        if(amount > balance){
            System.out.println("Insufficient balance. Withdrawal failed.");
        }else{
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: " + balance);
        }
    }
    void checkBalance(){

        System.out.println("Current balance: " + balance);
    }
}



public class oops2 {
    public static void main(String[] args) {
        
        ATM atm = new ATM();
        atm.deposit(100);
        atm.checkBalance();
        atm.withdraw(500);
    }
}
