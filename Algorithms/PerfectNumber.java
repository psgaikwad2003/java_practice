import java.util.ArrayList;
import java.util.List;

public class PerfectNumber {

    public enum NumberCategory {
        DEFICIENT, PERFECT, ABUNDANT
    }

    public static long aliquotSum(long n) {
        if (n <= 1) return 0;
        long sum = 1;

        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                sum += i;
                long companion = n / i;
                if (companion != i) {
                    sum += companion;
                }
            }
        }
        return sum;
    }

    public static boolean isPerfect(long n) {
        return n > 1 && aliquotSum(n) == n;
    }

    public static NumberCategory classifyNumber(long n) {
        long sum = aliquotSum(n);
        if (sum == n) return NumberCategory.PERFECT;
        if (sum > n) return NumberCategory.ABUNDANT;
        return NumberCategory.DEFICIENT;
    }

    public static List<Long> generateEuclidEulerPerfects(int primeLimit) {
        List<Long> perfects = new ArrayList<>();
        // Mersenne primes p = 2, 3, 5, 7, 13, 17, 19
        int[] mersenneExponents = {2, 3, 5, 7, 13};

        for (int p : mersenneExponents) {
            if (p > primeLimit) break;
            long mersennePrime = (1L << p) - 1;
            long perfect = (1L << (p - 1)) * mersennePrime;
            perfects.add(perfect);
        }
        return perfects;
    }

    public static void main(String[] args) {
        System.out.println("=== Perfect, Deficient & Abundant Numbers ===");

        long[] testCases = {6, 12, 28, 496, 8128, 15};
        for (long n : testCases) {
            System.out.printf("Number: %-5d | Aliquot Sum: %-5d | Class: %s%n",
                n, aliquotSum(n), classifyNumber(n));
        }

        System.out.println("\n=== First 5 Even Perfect Numbers via Euclid-Euler Theorem ===");
        List<Long> generated = generateEuclidEulerPerfects(13);
        for (int i = 0; i < generated.size(); i++) {
            System.out.printf("Perfect #%d: %d%n", i + 1, generated.get(i));
        }
    }
}
