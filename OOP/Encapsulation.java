class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

record TransactionRecord(String transactionId, String type, double amount, double resultingBalance, java.time.LocalDateTime timestamp) {
    @Override
    public String toString() {
        return String.format("[%s] %-10s | Amount: $%8.2f | Balance: $%8.2f | ID: %s",
            timestamp.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            type, amount, resultingBalance, transactionId);
    }
}

class BankAccount {
    private final String accountNumber;
    private double balance;
    private final java.util.List<TransactionRecord> transactionHistory;

    public BankAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionHistory = new java.util.ArrayList<>();
        if (initialBalance > 0) {
            transactionHistory.add(new TransactionRecord(
                java.util.UUID.randomUUID().toString().substring(0, 8),
                "INITIAL", initialBalance, balance, java.time.LocalDateTime.now()
            ));
        }
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        transactionHistory.add(new TransactionRecord(
            java.util.UUID.randomUUID().toString().substring(0, 8),
            "DEPOSIT", amount, balance, java.time.LocalDateTime.now()
        ));
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
        transactionHistory.add(new TransactionRecord(
            java.util.UUID.randomUUID().toString().substring(0, 8),
            "WITHDRAWAL", amount, balance, java.time.LocalDateTime.now()
        ));
        System.out.printf("Withdrew $%.2f from account %s. Remaining Balance: $%.2f%n", amount, accountNumber, balance);
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public java.util.List<TransactionRecord> getTransactionHistory() {
        return java.util.Collections.unmodifiableList(transactionHistory);
    }

    public void printStatement() {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("  STATEMENT FOR ACCOUNT: " + accountNumber);
        System.out.println("----------------------------------------------------------------------");
        if (transactionHistory.isEmpty()) {
            System.out.println("  (No transactions recorded)");
        } else {
            for (TransactionRecord record : transactionHistory) {
                System.out.println("  " + record);
            }
        }
        System.out.printf("  Current Settled Balance: $%.2f%n", balance);
        System.out.println("----------------------------------------------------------------------");
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