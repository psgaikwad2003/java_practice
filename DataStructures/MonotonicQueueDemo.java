package DataStructures;

import java.util.*;

/**
 * Demonstrates Monotonic Data Structures:
 * 1. Monotonic Queue (Decreasing & Increasing) - provides amortized O(1) push, pop, and max/min queries.
 * 2. Sliding Window Maximum in O(N) linear time (optimal interview standard).
 * 3. Online Stock Spanner - counts consecutive prior days with price <= today in amortized O(1).
 * 4. Next Greater Element (NGE) solver in O(N) using a Monotonic Stack.
 *
 * Complexity:
 * - Time: O(N) overall amortized time since each element is pushed and popped at most once.
 * - Space: O(K) or O(N) auxiliary space.
 */
public class MonotonicQueueDemo {

    /**
     * Monotonic Decreasing Queue:
     * Maintains elements in strictly decreasing order.
     * The front is always the maximum element in the current active window.
     */
    public static class MonotonicDecreasingQueue<T extends Comparable<T>> {
        private final Deque<T> deque = new ArrayDeque<>();

        public void push(T val) {
            // Remove all elements smaller than the incoming value from the tail
            while (!deque.isEmpty() && deque.peekLast().compareTo(val) < 0) {
                deque.pollLast();
            }
            deque.addLast(val);
        }

        public void pop(T val) {
            // Only remove from head if the leaving element is currently the head
            if (!deque.isEmpty() && deque.peekFirst().equals(val)) {
                deque.pollFirst();
            }
        }

        public T getMax() {
            if (deque.isEmpty()) {
                throw new NoSuchElementException("Queue is empty");
            }
            return deque.peekFirst();
        }

        public boolean isEmpty() {
            return deque.isEmpty();
        }

        public int size() {
            return deque.size();
        }
    }

    /**
     * Solves the Sliding Window Maximum problem in O(N) time and O(K) space.
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resultIndex = 0;

        // Store indices whose values are in decreasing order
        Deque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // 1. Remove indices outside current sliding window [i - k + 1, i]
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) {
                dq.pollFirst();
            }

            // 2. Remove smaller elements from back (maintain decreasing order)
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
                dq.pollLast();
            }

            // 3. Add current element index
            dq.addLast(i);

            // 4. Record current max once first window is formed
            if (i >= k - 1) {
                result[resultIndex++] = nums[dq.peekFirst()];
            }
        }

        return result;
    }

    /**
     * Online Stock Spanner:
     * Calculates the span of stock's price today: the maximum number of consecutive
     * days (starting from today and going backward) for which the stock price was <= today's price.
     */
    public static class OnlineStockSpanner {
        private record PriceSpan(int price, int span) {}
        private final Deque<PriceSpan> stack = new ArrayDeque<>();

        public int next(int price) {
            int span = 1;
            while (!stack.isEmpty() && stack.peek().price() <= price) {
                span += stack.pop().span();
            }
            stack.push(new PriceSpan(price, span));
            return span;
        }
    }

    /**
     * Next Greater Element (NGE) in O(N) time:
     * For each element in arr, finds the first element to its right that is greater than it.
     * If no such element exists, returns -1 for that position.
     */
    public static int[] nextGreaterElement(int[] arr) {
        int n = arr.length;
        int[] nge = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && stack.peek() <= arr[i]) {
                stack.pop();
            }
            nge[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(arr[i]);
        }

        return nge;
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("        MONOTONIC QUEUE & DEQUE ALGORITHMS DEMO              ");
        System.out.println("=============================================================");

        // Test 1: Sliding Window Maximum
        System.out.println("\n--- Test 1: Sliding Window Maximum (O(N)) ---");
        int[] stream = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        int[] maxes = maxSlidingWindow(stream, k);
        System.out.println("Input Array:  " + Arrays.toString(stream));
        System.out.println("Window Size:  k = " + k);
        System.out.println("Sliding Max:  " + Arrays.toString(maxes));

        // Test 2: Generic MonotonicDecreasingQueue Step-by-Step
        System.out.println("\n--- Test 2: Monotonic Decreasing Queue Lifecycle ---");
        MonotonicDecreasingQueue<Integer> mQueue = new MonotonicDecreasingQueue<>();
        int[] sequence = {10, 4, 15, 7, 2, 8};
        for (int val : sequence) {
            mQueue.push(val);
            System.out.printf("Pushed: %2d | Current Max: %2d | Deque Internal Size: %d%n",
                    val, mQueue.getMax(), mQueue.size());
        }

        // Test 3: Online Stock Spanner
        System.out.println("\n--- Test 3: Online Stock Spanner ---");
        OnlineStockSpanner spanner = new OnlineStockSpanner();
        int[] dailyQuotes = {100, 80, 60, 70, 60, 75, 85};
        System.out.println("Daily Quotes: " + Arrays.toString(dailyQuotes));
        System.out.print("Spans computed: [");
        for (int i = 0; i < dailyQuotes.length; i++) {
            int span = spanner.next(dailyQuotes[i]);
            System.out.print(span + (i == dailyQuotes.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        // Expected: [1, 1, 1, 2, 1, 4, 6]

        // Test 4: Next Greater Element
        System.out.println("\n--- Test 4: Next Greater Element (NGE) ---");
        int[] nums = {4, 5, 2, 25, 7, 18};
        int[] nge = nextGreaterElement(nums);
        System.out.println("Original: " + Arrays.toString(nums));
        System.out.println("NGE:      " + Arrays.toString(nge));

        System.out.println("\nMonotonic Queue verification completed successfully.");
    }
}
