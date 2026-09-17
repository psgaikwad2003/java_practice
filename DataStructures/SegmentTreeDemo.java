package DataStructures;

import java.util.Arrays;

/**
 * Demonstrates a Segment Tree data structure with Point Updates and Lazy Propagation for Range Updates.
 * 
 * Supports:
 * 1. Range Sum Queries: O(log N)
 * 2. Point Updates: O(log N)
 * 3. Range Updates with Lazy Propagation: O(log N)
 * 4. Range Minimum Queries (RMQ): O(log N)
 */
public class SegmentTreeDemo {

    /**
     * Segment Tree with Lazy Propagation for Range Addition and Range Sum Queries.
     */
    public static class LazySegmentTree {
        private final int n;
        private final long[] tree;
        private final long[] lazy;

        public LazySegmentTree(int[] arr) {
            this.n = arr.length;
            this.tree = new long[4 * n];
            this.lazy = new long[4 * n];
            if (n > 0) {
                build(arr, 0, 0, n - 1);
            }
        }

        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = start + (end - start) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            build(arr, leftChild, start, mid);
            build(arr, rightChild, mid + 1, end);
            tree[node] = tree[leftChild] + tree[rightChild];
        }

        private void pushDown(int node, int start, int end) {
            if (lazy[node] != 0) {
                long val = lazy[node];
                int mid = start + (end - start) / 2;
                int leftChild = 2 * node + 1;
                int rightChild = 2 * node + 2;

                // Propagate to left child
                lazy[leftChild] += val;
                tree[leftChild] += val * (mid - start + 1);

                // Propagate to right child
                lazy[rightChild] += val;
                tree[rightChild] += val * (end - mid);

                lazy[node] = 0; // Clear lazy mark
            }
        }

        /**
         * Updates a range [l..r] by adding value `delta`.
         * Time: O(log N)
         */
        public void updateRange(int l, int r, long delta) {
            updateRange(0, 0, n - 1, l, r, delta);
        }

        private void updateRange(int node, int start, int end, int l, int r, long delta) {
            if (r < start || end < l) {
                return; // Out of range
            }
            if (l <= start && end <= r) {
                tree[node] += delta * (end - start + 1);
                lazy[node] += delta;
                return;
            }

            pushDown(node, start, end);
            int mid = start + (end - start) / 2;
            updateRange(2 * node + 1, start, mid, l, r, delta);
            updateRange(2 * node + 2, mid + 1, end, l, r, delta);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }

        /**
         * Queries the sum in range [l..r].
         * Time: O(log N)
         */
        public long queryRange(int l, int r) {
            return queryRange(0, 0, n - 1, l, r);
        }

        private long queryRange(int node, int start, int end, int l, int r) {
            if (r < start || end < l) {
                return 0;
            }
            if (l <= start && end <= r) {
                return tree[node];
            }

            pushDown(node, start, end);
            int mid = start + (end - start) / 2;
            long leftSum = queryRange(2 * node + 1, start, mid, l, r);
            long rightSum = queryRange(2 * node + 2, mid + 1, end, l, r);
            return leftSum + rightSum;
        }
    }

    /**
     * Segment Tree for Range Minimum Queries (RMQ) with point updates.
     */
    public static class RMQSegmentTree {
        private final int n;
        private final int[] minTree;

        public RMQSegmentTree(int[] arr) {
            this.n = arr.length;
            this.minTree = new int[4 * n];
            if (n > 0) {
                build(arr, 0, 0, n - 1);
            }
        }

        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                minTree[node] = arr[start];
                return;
            }
            int mid = start + (end - start) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            minTree[node] = Math.min(minTree[2 * node + 1], minTree[2 * node + 2]);
        }

        public void update(int index, int value) {
            update(0, 0, n - 1, index, value);
        }

        private void update(int node, int start, int end, int idx, int val) {
            if (start == end) {
                minTree[node] = val;
                return;
            }
            int mid = start + (end - start) / 2;
            if (idx <= mid) {
                update(2 * node + 1, start, mid, idx, val);
            } else {
                update(2 * node + 2, mid + 1, end, idx, val);
            }
            minTree[node] = Math.min(minTree[2 * node + 1], minTree[2 * node + 2]);
        }

        public int queryMin(int l, int r) {
            return queryMin(0, 0, n - 1, l, r);
        }

        private int queryMin(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return Integer.MAX_VALUE;
            if (l <= start && end <= r) return minTree[node];

            int mid = start + (end - start) / 2;
            return Math.min(
                queryMin(2 * node + 1, start, mid, l, r),
                queryMin(2 * node + 2, mid + 1, end, l, r)
            );
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Segment Tree Demonstration ===\n");

        int[] data = {1, 3, 5, 7, 9, 11};
        System.out.println("Initial Array: " + Arrays.toString(data));

        // Part 1: Lazy Segment Tree (Range Sum & Range Update)
        System.out.println("\n--- 1. Lazy Segment Tree (Range Sum + Range Addition) ---");
        LazySegmentTree lazyTree = new LazySegmentTree(data);

        System.out.println("Query sum [1..3] (3 + 5 + 7): " + lazyTree.queryRange(1, 3));
        System.out.println("Query sum [0..5] (Total): " + lazyTree.queryRange(0, 5));

        System.out.println("\nAdding +10 to range [1..3]...");
        lazyTree.updateRange(1, 3, 10);
        // data becomes {1, 13, 15, 17, 9, 11}

        System.out.println("Query sum [1..3] (after +10): " + lazyTree.queryRange(1, 3));
        System.out.println("Query sum [0..5] (after +10): " + lazyTree.queryRange(0, 5));
        System.out.println("Query single element at index 2: " + lazyTree.queryRange(2, 2));

        // Part 2: Range Minimum Query (RMQ)
        System.out.println("\n--- 2. Range Minimum Query (RMQ) Tree ---");
        int[] rmqData = {2, 5, 1, 4, 9, 3};
        System.out.println("RMQ Array: " + Arrays.toString(rmqData));
        RMQSegmentTree rmqTree = new RMQSegmentTree(rmqData);

        System.out.println("Min in range [0..2]: " + rmqTree.queryMin(0, 2));
        System.out.println("Min in range [3..5]: " + rmqTree.queryMin(3, 5));
        System.out.println("Min in range [4..5]: " + rmqTree.queryMin(4, 5));

        System.out.println("Updating index 2 (value 1 -> 10)...");
        rmqTree.update(2, 10);
        System.out.println("New min in range [0..2]: " + rmqTree.queryMin(0, 2));
    }
}
