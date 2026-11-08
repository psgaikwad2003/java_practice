import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicatesArray {

    public static int removeDuplicatesSorted(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        if (arr.length == 1) return 1;

        int writeIndex = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[writeIndex - 1]) {
                arr[writeIndex] = arr[i];
                writeIndex++;
            }
        }
        return writeIndex;
    }

    public static int[] removeDuplicatesUnsorted(int[] arr) {
        if (arr == null) return new int[0];
        Set<Integer> seen = new LinkedHashSet<>();
        for (int num : arr) {
            seen.add(num);
        }
        int[] result = new int[seen.size()];
        int idx = 0;
        for (int num : seen) {
            result[idx++] = num;
        }
        return result;
    }

    public static int[] removeDuplicatesStream(int[] arr) {
        if (arr == null) return new int[0];
        return Arrays.stream(arr).distinct().toArray();
    }

    public static <T> T[] removeDuplicatesGeneric(T[] arr) {
        if (arr == null) return null;
        Set<T> seen = new LinkedHashSet<>(Arrays.asList(arr));
        return seen.toArray(Arrays.copyOf(arr, seen.size()));
    }

    public static void main(String[] args) {
        System.out.println("=== In-Place Sorted Array Deduplication ===");
        int[] sorted = {1, 1, 2, 2, 3, 4, 4, 5, 5, 5};
        System.out.println("Original: " + Arrays.toString(sorted));
        int uniqueCount = removeDuplicatesSorted(sorted);
        System.out.print("Deduplicated: [");
        for (int i = 0; i < uniqueCount; i++) {
            System.out.print(sorted[i] + (i < uniqueCount - 1 ? ", " : ""));
        }
        System.out.println("] (Length: " + uniqueCount + ")");

        System.out.println("\n=== Unsorted Array Deduplication (Order Preserved) ===");
        int[] unsorted = {4, 2, 5, 2, 1, 4, 3, 5, 1};
        System.out.println("Original: " + Arrays.toString(unsorted));
        int[] uniqueUnsorted = removeDuplicatesUnsorted(unsorted);
        System.out.println("Deduplicated: " + Arrays.toString(uniqueUnsorted));

        System.out.println("\n=== Generic String Array Deduplication ===");
        String[] words = {"java", "spring", "java", "docker", "spring", "kubernetes"};
        String[] distinctWords = removeDuplicatesGeneric(words);
        System.out.println("Unique Words: " + Arrays.toString(distinctWords));
    }
}
