public class array {
    public static void main(String[] args) {
        
        int[] arr = {5, 2, 9, 1, 5, 6};
        int sum = 0;

        int largest = arr[0];
        int secomdlargest = Integer.MIN_VALUE;

        int smallest = arr[0];
        int sccondsmallest = Integer.MAX_VALUE;

        int even = 0;
        int odd = 0;

        int positive = 0;
        int negative = 0;
        int zero = 0;

        System.out.println("Array Elements:");

        for (int i = 0; i < arr.length; i++) {

            System.out.print(arr[i] + " ");

            sum += arr[i];

            // Largest and Second Largest
            if (arr[i] > largest) {
                secondLargest = largest;
                largest = arr[i];
            } else if (arr[i] > secondLargest && arr[i] != largest) {
                secondLargest = arr[i];
            }

            // Smallest and Second Smallest
            if (arr[i] < smallest) {
                secondSmallest = smallest;
                smallest = arr[i];
            } else if (arr[i] < secondSmallest && arr[i] != smallest) {
                secondSmallest = arr[i];
            }

            // Even and Odd
            if (arr[i] % 2 == 0) {
                even++;
            } else {
                odd++;
            }

            // Positive, Negative and Zero
            if (arr[i] > 0) {
                positive++;
            } else if (arr[i] < 0) {
                negative++;
            } else {
                zero++;
            }

        }

        double average = (double) sum / arr.length;

        System.out.println("\n\nSum = " + sum);
        System.out.println("Average = " + average);
        System.out.println("Largest = " + largest);
        System.out.println("Second Largest = " + secondLargest);
        System.out.println("Smallest = " + smallest);
        System.out.println("Second Smallest = " + secondSmallest);
        System.out.println("Even Count = " + even);
        System.out.println("Odd Count = " + odd);
        System.out.println("Positive Count = " + positive);
        System.out.println("Negative Count = " + negative);
        System.out.println("Zero Count = " + zero);
        
    }
    
}
