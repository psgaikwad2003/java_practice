import java.util.*;

public class RecursionDemo {

    
    static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    
    static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    
    static Map<Integer, Long> memo = new HashMap<>();
    static long fibMemo(int n) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fibMemo(n - 1) + fibMemo(n - 2);
        memo.put(n, result);
        return result;
    }

    
    static int binarySearch(int[] arr, int target, int low, int high) {
        if (low > high) return -1;
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) return binarySearch(arr, target, mid + 1, high);
        return binarySearch(arr, target, low, mid - 1);
    }

    
    static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1, n2 = right - mid;
        int[] L = new int[n1], R = new int[n2];
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    
    static void hanoi(int n, char from, char to, char aux) {
        if (n == 1) {
            System.out.println("  Move disk 1 from " + from + " to " + to);
            return;
        }
        hanoi(n - 1, from, aux, to);
        System.out.println("  Move disk " + n + " from " + from + " to " + to);
        hanoi(n - 1, aux, to, from);
    }

    
    static double power(double base, int exp) {
        if (exp == 0) return 1;
        if (exp < 0) return 1.0 / power(base, -exp);
        if (exp % 2 == 0) {
            double half = power(base, exp / 2);
            return half * half;
        }
        return base * power(base, exp - 1);
    }

    
    static String reverseString(String s) {
        if (s.isEmpty()) return s;
        return reverseString(s.substring(1)) + s.charAt(0);
    }

    
    static boolean isPalindrome(String s, int left, int right) {
        if (left >= right) return true;
        if (s.charAt(left) != s.charAt(right)) return false;
        return isPalindrome(s, left + 1, right - 1);
    }

    
    static int sumOfDigits(int n) {
        if (n < 0) n = -n;
        if (n < 10) return n;
        return n % 10 + sumOfDigits(n / 10);
    }

    
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    
    static void flatten(Object[] arr, List<Integer> result) {
        for (Object element : arr) {
            if (element instanceof Object[]) flatten((Object[]) element, result);
            else if (element instanceof Integer) result.add((Integer) element);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Recursion Deep Dive Demo ===\n");

        
        System.out.println("--- Factorial ---");
        for (int i = 0; i <= 10; i++)
            System.out.println("  " + i + "! = " + factorial(i));

        
        System.out.println("\n--- Fibonacci: Naive vs Memoized ---");
        long t1 = System.currentTimeMillis();
        long naiveResult = fibonacci(35);
        long naiveTime = System.currentTimeMillis() - t1;

        long t2 = System.currentTimeMillis();
        long memoResult = fibMemo(35);
        long memoTime = System.currentTimeMillis() - t2;

        System.out.println("  fib(35) naive    = " + naiveResult + " (" + naiveTime + "ms)");
        System.out.println("  fib(35) memoized = " + memoResult + " (" + memoTime + "ms)");
        System.out.println("  fib(50) memoized = " + fibMemo(50));

        
        System.out.println("\n--- Recursive Binary Search ---");
        int[] sorted = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        System.out.println("  Array: " + Arrays.toString(sorted));
        System.out.println("  Search 23 -> index: " + binarySearch(sorted, 23, 0, sorted.length - 1));
        System.out.println("  Search 99 -> index: " + binarySearch(sorted, 99, 0, sorted.length - 1));

        
        System.out.println("\n--- Merge Sort (Recursive) ---");
        int[] arr = {64, 34, 25, 12, 22, 11, 90, 45};
        System.out.println("  Before: " + Arrays.toString(arr));
        mergeSort(arr, 0, arr.length - 1);
        System.out.println("  After:  " + Arrays.toString(arr));

        
        System.out.println("\n--- Tower of Hanoi (3 disks) ---");
        hanoi(3, 'A', 'C', 'B');
        System.out.println("  Total moves for 3 disks: " + ((int) Math.pow(2, 3) - 1));

        
        System.out.println("\n--- Recursive Power ---");
        System.out.println("  2^10 = " + (long) power(2, 10));
        System.out.println("  3^5  = " + (long) power(3, 5));
        System.out.println("  2^-3 = " + power(2, -3));

        
        System.out.println("\n--- String Recursion ---");
        String[] words = {"racecar", "hello", "madam", "world", "level"};
        for (String word : words) {
            System.out.println("  reverse(\"" + word + "\") = \"" + reverseString(word) + "\"" +
                               "  palindrome: " + isPalindrome(word, 0, word.length() - 1));
        }

        
        System.out.println("\n--- Sum of Digits & GCD ---");
        System.out.println("  sumDigits(12345) = " + sumOfDigits(12345));
        System.out.println("  sumDigits(9999)  = " + sumOfDigits(9999));
        System.out.println("  gcd(48, 18)      = " + gcd(48, 18));
        System.out.println("  gcd(100, 75)     = " + gcd(100, 75));

        System.out.println("\n=== Demo Complete ===");
    }
}
