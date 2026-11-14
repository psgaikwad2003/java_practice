import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrimeNumber {

    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num <= 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;

        for (int i = 5; (long) i * i <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> sieveOfEratosthenes(int limit) {
        if (limit < 2) return new ArrayList<>();
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

    public static List<Long> segmentedSieve(long low, long high) {
        if (low < 2) low = 2;
        if (high < low) return new ArrayList<>();

        int limit = (int) Math.sqrt(high);
        List<Integer> basePrimes = sieveOfEratosthenes(limit);

        int range = (int) (high - low + 1);
        boolean[] isPrime = new boolean[range];
        Arrays.fill(isPrime, true);

        for (int p : basePrimes) {
            long start = Math.max((long) p * p, ((low + p - 1) / p) * (long) p);
            for (long j = start; j <= high; j += p) {
                isPrime[(int) (j - low)] = false;
            }
        }

        List<Long> result = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            if (isPrime[i]) {
                result.add(low + i);
            }
        }
        return result;
    }

    public static Map<Long, Integer> primeFactorization(long n) {
        Map<Long, Integer> factors = new LinkedHashMap<>();
        if (n <= 1) return factors;

        for (long d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                int count = 0;
                while (n % d == 0) {
                    count++;
                    n /= d;
                }
                factors.put(d, count);
            }
        }
        if (n > 1) {
            factors.put(n, 1);
        }
        return factors;
    }

    public static void main(String[] args) {
        System.out.println("=== Prime Number Algorithms & Segmented Sieve ===");

        int[] testNumbers = {2, 3, 4, 17, 29, 97, 100};
        for (int num : testNumbers) {
            System.out.printf("Is %3d prime? %s%n", num, isPrime(num) ? "YES" : "NO");
        }

        System.out.println("\n=== Segmented Sieve in Range [100, 150] ===");
        List<Long> primesInRange = segmentedSieve(100, 150);
        System.out.println("Primes: " + primesInRange);

        System.out.println("\n=== Prime Factorization of 360 ===");
        System.out.println("Factors: " + primeFactorization(360));
    }
}
