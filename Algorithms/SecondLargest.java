public class SecondLargest {

    public static void main(String[] args) {

        int[] arr = {45, 78, 12, 90, 67, 90, 23};

        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] > largest) {

                secondLargest = largest;
                largest = arr[i];

            } else if (arr[i] > secondLargest && arr[i] != largest) {

                secondLargest = arr[i];

            }

        }

        System.out.println("Largest = " + largest);
        System.out.println("Second Largest = " + secondLargest);

    }

}