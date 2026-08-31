import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

/**
 * Demonstrates ForkJoinPool for divide-and-conquer parallel processing.
 */
public class ForkJoinPoolComplexDemo extends RecursiveTask<Integer> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10;

    public ForkJoinPoolComplexDemo(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            ForkJoinPoolComplexDemo left = new ForkJoinPoolComplexDemo(array, start, mid);
            ForkJoinPoolComplexDemo right = new ForkJoinPoolComplexDemo(array, mid, end);
            
            // Fork the left task and compute the right task directly
            left.fork();
            int rightResult = right.compute();
            int leftResult = left.join();
            
            return rightResult + leftResult;
        }
    }

    public static void main(String[] args) {
        int[] data = new int[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
        
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinPoolComplexDemo task = new ForkJoinPoolComplexDemo(data, 0, data.length);
        
        System.out.println("Starting ForkJoin parallel computation...");
        int result = pool.invoke(task);
        System.out.println("Total sum calculated: " + result);
    }
}
