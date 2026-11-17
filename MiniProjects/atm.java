import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class atm {

    public record Transaction(String type, double amount, double resultingBalance, String timestamp) {
        @Override
        public String toString() {
            return String.format("[%s] %-8s | Amount: $%7.2f | Balance: $%7.2f",
                timestamp, type, amount, resultingBalance);
        }
    }

    private double balance;
    private final String pin;
    private final List<Transaction> history;
    private boolean authenticated;

    public atm(double initialBalance, String pin) {
        this.balance = initialBalance;
        this.pin = pin;
        this.history = new ArrayList<>();
        this.authenticated = false;
        recordTransaction("INITIAL", initialBalance);
    }

    public boolean authenticate(String inputPin) {
        this.authenticated = (this.pin != null && this.pin.equals(inputPin));
        return this.authenticated;
    }

    public void deposit(double amount) {
        if (!authenticated) throw new IllegalStateException("Authentication required.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        balance += amount;
        recordTransaction("DEPOSIT", amount);
        System.out.printf("Successfully deposited $%.2f. New balance: $%.2f%n", amount, balance);
    }

    public boolean withdraw(int amount) {
        if (!authenticated) throw new IllegalStateException("Authentication required.");
        if (amount <= 0 || amount > balance) {
            System.out.println("Withdrawal rejected: Insufficient funds or invalid amount.");
            return false;
        }

        Map<Integer, Integer> dispensed = calculateDenominations(amount);
        balance -= amount;
        recordTransaction("WITHDRAW", amount);
        System.out.printf("Successfully withdrew $%d. Remaining balance: $%.2f%n", amount, balance);
        System.out.println("Dispensed Notes: " + dispensed);
        return true;
    }

    public static Map<Integer, Integer> calculateDenominations(int amount) {
        int[] notes = {2000, 500, 200, 100, 50, 20, 10};
        Map<Integer, Integer> dispensed = new LinkedHashMap<>();

        for (int note : notes) {
            if (amount >= note) {
                int count = amount / note;
                dispensed.put(note, count);
                amount %= note;
            }
        }
        return dispensed;
    }

    private void recordTransaction(String type, double amount) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        history.add(new Transaction(type, amount, balance, now));
    }

    public void printMiniStatement() {
        System.out.println("=== Mini Statement ===");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Automated Teller Machine Simulator ===");

        atm machine = new atm(15000, "1234");
        if (machine.authenticate("1234")) {
            System.out.println("User authenticated successfully.");
            machine.deposit(2500);
            machine.withdraw(3800);
            machine.withdraw(550);
            machine.printMiniStatement();
        } else {
            System.out.println("Authentication failed.");
        }
    }
}
