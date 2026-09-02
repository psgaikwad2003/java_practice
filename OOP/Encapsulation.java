class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class BankAccount {
    private final String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.printf("Deposited $%.2f into account %s. New Balance: $%.2f%n", amount, accountNumber, balance);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                String.format("Insufficient funds! Attempted to withdraw $%.2f, but current balance is $%.2f", amount, balance)
            );
        }
        balance -= amount;
        System.out.printf("Withdrew $%.2f from account %s. Remaining Balance: $%.2f%n", amount, accountNumber, balance);
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Deprecated
    public double setBalance() {
        return getBalance();
    }
}

public class Encapsulation {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Encapsulation & Robust Exception Handling");
        System.out.println("==================================================");

        BankAccount acc = new BankAccount("ACC-987654", 1000.00);
        acc.deposit(500.00);

        try {
            acc.withdraw(300.00);
            System.out.println("Current Balance: $" + acc.getBalance());

            System.out.println("\nAttempting overdraw ($2000)...");
            acc.withdraw(2000.00);
        } catch (InsufficientFundsException e) {
            System.err.println("Caught Exception: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
        }
    }
}