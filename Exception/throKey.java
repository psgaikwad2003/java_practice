class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }

    public InvalidAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}

class UnderageVoterException extends RuntimeException {
    private final int actualAge;

    public UnderageVoterException(int actualAge) {
        super("Voter is underage (age: " + actualAge + ", required: >= 18).");
        this.actualAge = actualAge;
    }

    public int getActualAge() {
        return actualAge;
    }
}

public class throKey {

    public static void validateVotingEligibility(int age) throws InvalidAgeException {
        if (age < 0 || age > 130) {
            throw new InvalidAgeException("Unrealistic human age supplied: " + age);
        }
        if (age < 18) {
            throw new UnderageVoterException(age);
        }
        System.out.println("Eligibility confirmed for age " + age + ": Eligible to cast ballot.");
    }

    public static void main(String[] args) {
        System.out.println("=== Throw Keyword & Custom Exception Handling ===");

        int[] testAges = {25, 16, -5, 72};

        for (int age : testAges) {
            System.out.println("\nValidating age: " + age);
            try {
                validateVotingEligibility(age);
            } catch (UnderageVoterException e) {
                System.out.println("[Unchecked Rejection] " + e.getMessage());
            } catch (InvalidAgeException e) {
                System.out.println("[Checked Error] " + e.getMessage());
            }
        }
    }
}
