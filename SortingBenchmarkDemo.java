import java.util.Arrays;
import java.util.Random;


public class SortingBenchmarkDemo {

    public static void main(String[] args) {
        int size = 5000;
        System.out.println("==================================================");
        System.out.println(" Benchmark: Sorting " + size + " Random Integers");
        System.out.println("==================================================");

        int[] original = generateRandomArray(size, 10000);

        int[] arr1 = Arrays.copyOf(original, original.length);
        long start = System.nanoTime();
        bubbleSort(arr1);
        long bubbleTime = System.nanoTime() - start;
        System.out.printf("%-18s : %8.2f ms%n", "Bubble Sort", bubbleTime / 1e6);

        int[] arr2 = Arrays.copyOf(original, original.length);
        start = System.nanoTime();
        selectionSort(arr2);
        long selectionTime = System.nanoTime() - start;
        System.out.printf("%-18s : %8.2f ms%n", "Selection Sort", selectionTime / 1e6);

        int[] arr3 = Arrays.copyOf(original, original.length);
        start = System.nanoTime();
        insertionSort(arr3);
        long insertionTime = System.nanoTime() - start;
        System.out.printf("%-18s : %8.2f ms%n", "Insertion Sort", insertionTime / 1e6);

        int[] arr4 = Arrays.copyOf(original, original.length);
        start = System.nanoTime();
        quickSort(arr4, 0, arr4.length - 1);
        long quickTime = System.nanoTime() - start;
        System.out.printf("%-18s : %8.2f ms%n", "Quick Sort", quickTime / 1e6);
    }

    private static int[] generateRandomArray(int size, int maxVal) {
        Random rand = new Random(42); 
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(maxVal);
        }
        return arr;
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
