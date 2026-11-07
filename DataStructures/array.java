import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class array {

    public static OptionalInt findSecondLargest(int[] arr) {
        if (arr == null || arr.length < 2) return OptionalInt.empty();
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        boolean found = false;

        for (int num : arr) {
            if (num > largest) {
                secondLargest = largest;
                largest = num;
                found = true;
            } else if (num > secondLargest && num != largest) {
                secondLargest = num;
                found = true;
            }
        }
        return (found && secondLargest != Integer.MIN_VALUE) ? OptionalInt.of(secondLargest) : OptionalInt.empty();
    }

    public static OptionalInt findSecondSmallest(int[] arr) {
        if (arr == null || arr.length < 2) return OptionalInt.empty();
        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;
        boolean found = false;

        for (int num : arr) {
            if (num < smallest) {
                secondSmallest = smallest;
                smallest = num;
                found = true;
            } else if (num < secondSmallest && num != smallest) {
                secondSmallest = num;
                found = true;
            }
        }
        return (found && secondSmallest != Integer.MAX_VALUE) ? OptionalInt.of(secondSmallest) : OptionalInt.empty();
    }

    public static double calculateMean(int[] arr) {
        if (arr == null || arr.length == 0) return 0.0;
        long sum = 0;
        for (int n : arr) sum += n;
        return (double) sum / arr.length;
    }

    public static double calculateVariance(int[] arr) {
        if (arr == null || arr.length <= 1) return 0.0;
        double mean = calculateMean(arr);
        double temp = 0;
        for (int n : arr) temp += (n - mean) * (n - mean);
        return temp / arr.length;
    }

    public static Map<Integer, Integer> getFrequencyMap(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        if (arr == null) return map;
        for (int n : arr) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        return map;
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 1, 5, 6};
        int sum = 0;
        int largest = arr[0];
        int secondLargest = Integer.MIN_VALUE;
        int smallest = arr[0];
        int secondSmallest = Integer.MAX_VALUE;
        int even = 0;
        int odd = 0;
        int positive = 0;
        int negative = 0;
        int zero = 0;

        System.out.println("Array Elements: " + Arrays.toString(arr));

        for (int num : arr) {
            sum += num;
            if (num > largest) {
                secondLargest = largest;
                largest = num;
            } else if (num > secondLargest && num != largest) {
                secondLargest = num;
            }

            if (num < smallest) {
                secondSmallest = smallest;
                smallest = num;
            } else if (num < secondSmallest && num != smallest) {
                secondSmallest = num;
            }

            if (num % 2 == 0) even++; else odd++;
            if (num > 0) positive++; else if (num < 0) negative++; else zero++;
        }

        System.out.println("Sum = " + sum);
        System.out.println("Average = " + calculateMean(arr));
        System.out.println("Largest = " + largest);
        System.out.println("Second Largest = " + secondLargest);
        System.out.println("Smallest = " + smallest);
        System.out.println("Second Smallest = " + secondSmallest);
        System.out.println("Even Count = " + even + " | Odd Count = " + odd);
        System.out.println("Variance = " + String.format("%.2f", calculateVariance(arr)));
        System.out.println("Standard Deviation = " + String.format("%.2f", Math.sqrt(calculateVariance(arr))));
        System.out.println("Frequency Map: " + getFrequencyMap(arr));
    }
}
