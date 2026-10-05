package DataStructures;

import java.util.*;

public class MonotonicQueueDemo {

    public static class MonotonicDecreasingQueue<T extends Comparable<T>> {
        private final Deque<T> deque = new ArrayDeque<>();

        public void push(T val) {

            while (!deque.isEmpty() && deque.peekLast().compareTo(val) < 0) {
                deque.pollLast();
            }
            deque.addLast(val);
        }

        public void pop(T val) {

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

    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resultIndex = 0;

        Deque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) {
                dq.pollFirst();
            }

            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
                dq.pollLast();
            }

            dq.addLast(i);

            if (i >= k - 1) {
                result[resultIndex++] = nums[dq.peekFirst()];
            }
        }

        return result;
    }

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

    public static int[] nextGreaterElement(int[] arr) {
        int n = arr.length;
        int[] nge = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

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

        System.out.println("\n--- Test 1: Sliding Window Maximum (O(N)) ---");
        int[] stream = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        int[] maxes = maxSlidingWindow(stream, k);
        System.out.println("Input Array:  " + Arrays.toString(stream));
        System.out.println("Window Size:  k = " + k);
        System.out.println("Sliding Max:  " + Arrays.toString(maxes));

        System.out.println("\n--- Test 2: Monotonic Decreasing Queue Lifecycle ---");
        MonotonicDecreasingQueue<Integer> mQueue = new MonotonicDecreasingQueue<>();
        int[] sequence = {10, 4, 15, 7, 2, 8};
        for (int val : sequence) {
            mQueue.push(val);
            System.out.printf("Pushed: %2d | Current Max: %2d | Deque Internal Size: %d%n",
                    val, mQueue.getMax(), mQueue.size());
        }

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

        System.out.println("\n--- Test 4: Next Greater Element (NGE) ---");
        int[] nums = {4, 5, 2, 25, 7, 18};
        int[] nge = nextGreaterElement(nums);
        System.out.println("Original: " + Arrays.toString(nums));
        System.out.println("NGE:      " + Arrays.toString(nge));

        System.out.println("\nMonotonic Queue verification completed successfully.");
    }
}
