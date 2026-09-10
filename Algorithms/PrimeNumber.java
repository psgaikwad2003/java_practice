import java.util.ArrayList;
import java.util.List;

public class PrimeNumber {

    /**
     * Efficiently checks if a number is prime using the 6k +/- 1 optimization.
     * Time Complexity: O(sqrt(n))
     *
     * @param num the integer to check
     * @return true if num is a prime number, false otherwise
     */
    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num <= 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;

        for (int i = 5; i * i <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds all prime numbers within an inclusive range [start, end] using trial division.
     *
     * @param start the start of the range (inclusive)
     * @param end   the end of the range (inclusive)
     * @return a list of prime numbers in [start, end]
     */
    public static List<Integer> findPrimesInRange(int start, int end) {
        List<Integer> primes = new ArrayList<>();
        for (int i = Math.max(2, start); i <= end; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * Generates all prime numbers up to {@code limit} using the Sieve of Eratosthenes.
     * More efficient than trial division for large ranges.
     * Time Complexity: O(n log log n), Space Complexity: O(n)
     *
     * @param limit the upper bound (inclusive)
     * @return a list of all primes up to limit
     * @throws IllegalArgumentException if limit is negative
     */
    public static List<Integer> sieveOfEratosthenes(int limit) {
        if (limit < 0) throw new IllegalArgumentException("Limit must be non-negative.");
        boolean[] isComposite = new boolean[limit + 1];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i <= limit; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (long j = (long) i * i; j <= limit; j += i) {
                    isComposite[(int) j] = true;
                }
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        System.out.println("=== Prime Number Verification & Demonstrations ===");

        int[] testNumbers = {-5, 0, 1, 2, 3, 4, 17, 29, 49, 97, 100};
        for (int num : testNumbers) {
            System.out.printf("Is %3d prime? %s%n", num, isPrime(num) ? "YES" : "NO");
        }

        System.out.println("\n=== Primes in Range [10, 50] ===");
        List<Integer> primes = findPrimesInRange(10, 50);
        System.out.println(primes);
        System.out.println("Total primes found: " + primes.size());

        System.out.println("\n=== Sieve of Eratosthenes up to 100 ===");
        List<Integer> sieve = sieveOfEratosthenes(100);
        System.out.println(sieve);
        System.out.println("Total primes up to 100: " + sieve.size());
    }
}
