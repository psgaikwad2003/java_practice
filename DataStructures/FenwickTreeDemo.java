package DataStructures;

import java.util.Arrays;

/**
 * Demonstrates the Fenwick Tree (Binary Indexed Tree / BIT).
 * 
 * Efficiently computes prefix sums and supports point/range updates in O(log N) time
 * using only O(N) space and elegant bit manipulation with the lowest set bit (i & -i).
 * 
 * Features:
 * 1. Standard BIT: Point Update, Range Query.
 * 2. Range Update, Point Query via Difference Array.
 * 3. Binary Lifting on BIT (find index with target prefix sum in O(log N)).
 */
public class FenwickTreeDemo {

    /**
     * Standard Fenwick Tree for Point Updates and Range Queries.
     */
    public static class FenwickTree {
        private final int size;
        private final long[] tree;

        public FenwickTree(int size) {
            this.size = size;
            this.tree = new long[size + 1];
        }

        public FenwickTree(int[] arr) {
            this(arr.length);
            // O(N) construction
            for (int i = 0; i < arr.length; i++) {
                tree[i + 1] += arr[i];
                int parent = (i + 1) + ((i + 1) & -(i + 1));
                if (parent <= size) {
                    tree[parent] += tree[i + 1];
                }
            }
        }

        /**
         * Adds delta to element at 0-based index.
         * Time: O(log N)
         */
        public void add(int index, long delta) {
            int i = index + 1; // Convert to 1-based indexing
            while (i <= size) {
                tree[i] += delta;
                i += i & -i; // Move to parent covering larger range
            }
        }

        /**
         * Computes prefix sum of range [0..index] (inclusive).
         * Time: O(log N)
         */
        public long queryPrefix(int index) {
            if (index < 0) return 0;
            int i = Math.min(index + 1, size);
            long sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= i & -i; // Remove lowest set bit
            }
            return sum;
        }

        /**
         * Computes range sum of range [left..right] (inclusive, 0-based).
         * Time: O(log N)
         */
        public long queryRange(int left, int right) {
            if (left > right || left < 0 || right >= size) return 0;
            return queryPrefix(right) - queryPrefix(left - 1);
        }

        /**
         * Finds the smallest 0-based index where prefix sum is at least targetSum (all positive values assumed).
         * Uses binary lifting in O(log N) time.
         */
        public int lowerBound(long targetSum) {
            int idx = 0;
            long currentSum = 0;
            // Find highest power of 2 <= size
            int mask = Integer.highestOneBit(size);

            for (; mask > 0; mask >>= 1) {
                int nextIdx = idx + mask;
                if (nextIdx <= size && currentSum + tree[nextIdx] < targetSum) {
                    idx = nextIdx;
                    currentSum += tree[nextIdx];
                }
            }
            return idx; // 0-based index where sum is met
        }
    }

    /**
     * Fenwick Tree variant supporting Range Update and Point Query.
     */
    public static class RangeUpdateBIT {
        private final FenwickTree bit;

        public RangeUpdateBIT(int size) {
            this.bit = new FenwickTree(size);
        }

        /**
         * Adds delta to all elements in range [left..right] (0-based).
         */
        public void updateRange(int left, int right, long delta) {
            bit.add(left, delta);
            if (right + 1 < bit.size) {
                bit.add(right + 1, -delta);
            }
        }

        /**
         * Queries the value at 0-based index.
         */
        public long queryPoint(int index) {
            return bit.queryPrefix(index);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Fenwick Tree (Binary Indexed Tree) Demo ===\n");

        int[] array = {3, 2, -1, 6, 5, 4, -3, 3, 7, 2};
        System.out.println("Original Array: " + Arrays.toString(array));
        FenwickTree bit = new FenwickTree(array);

        // 1. Prefix and Range Queries
        System.out.println("Prefix sum up to index 3 (3+2-1+6): " + bit.queryPrefix(3));
        System.out.println("Range sum [2..5] (-1+6+5+4): " + bit.queryRange(2, 5));

        // 2. Point Update
        System.out.println("\nAdding +10 to index 3 (value 6 -> 16)...");
        bit.add(3, 10);
        System.out.println("New prefix sum up to index 3: " + bit.queryPrefix(3));
        System.out.println("New range sum [2..5]: " + bit.queryRange(2, 5));

        // 3. Binary Lifting Lower Bound on positive array
        System.out.println("\n--- Binary Lifting / Lower Bound Search ---");
        int[] positiveArr = {1, 2, 4, 3, 5};
        System.out.println("Positive array: " + Arrays.toString(positiveArr));
        FenwickTree posBit = new FenwickTree(positiveArr);
        long target = 7; // Prefix sums: [1, 3, 7, 10, 15]
        int boundIdx = posBit.lowerBound(target);
        System.out.printf("Smallest 0-based index with prefix sum >= %d is: %d (sum = %d)%n",
                target, boundIdx, posBit.queryPrefix(boundIdx));

        // 4. Range Update & Point Query variant
        System.out.println("\n--- Range Update BIT (Difference Array) ---");
        RangeUpdateBIT rubit = new RangeUpdateBIT(5);
        System.out.println("Adding +5 to range [1..3]...");
        rubit.updateRange(1, 3, 5);
        System.out.println("Adding +2 to range [2..4]...");
        rubit.updateRange(2, 4, 2);

        for (int i = 0; i < 5; i++) {
            System.out.printf("Element at index %d: %d%n", i, rubit.queryPoint(i));
        }
    }
}
