package DataStructures;

import java.util.Arrays;

public class FenwickTreeDemo {

    public static class FenwickTree {
        private final int size;
        private final long[] tree;

        public FenwickTree(int size) {
            this.size = size;
            this.tree = new long[size + 1];
        }

        public FenwickTree(int[] arr) {
            this(arr.length);

            for (int i = 0; i < arr.length; i++) {
                tree[i + 1] += arr[i];
                int parent = (i + 1) + ((i + 1) & -(i + 1));
                if (parent <= size) {
                    tree[parent] += tree[i + 1];
                }
            }
        }

        public void add(int index, long delta) {
            int i = index + 1;
            while (i <= size) {
                tree[i] += delta;
                i += i & -i;
            }
        }

        public long queryPrefix(int index) {
            if (index < 0) return 0;
            int i = Math.min(index + 1, size);
            long sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= i & -i;
            }
            return sum;
        }

        public long queryRange(int left, int right) {
            if (left > right || left < 0 || right >= size) return 0;
            return queryPrefix(right) - queryPrefix(left - 1);
        }

        public int lowerBound(long targetSum) {
            int idx = 0;
            long currentSum = 0;

            int mask = Integer.highestOneBit(size);

            for (; mask > 0; mask >>= 1) {
                int nextIdx = idx + mask;
                if (nextIdx <= size && currentSum + tree[nextIdx] < targetSum) {
                    idx = nextIdx;
                    currentSum += tree[nextIdx];
                }
            }
            return idx;
        }
    }

    public static class RangeUpdateBIT {
        private final FenwickTree bit;

        public RangeUpdateBIT(int size) {
            this.bit = new FenwickTree(size);
        }

        public void updateRange(int left, int right, long delta) {
            bit.add(left, delta);
            if (right + 1 < bit.size) {
                bit.add(right + 1, -delta);
            }
        }

        public long queryPoint(int index) {
            return bit.queryPrefix(index);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Fenwick Tree (Binary Indexed Tree) Demo ===\n");

        int[] array = {3, 2, -1, 6, 5, 4, -3, 3, 7, 2};
        System.out.println("Original Array: " + Arrays.toString(array));
        FenwickTree bit = new FenwickTree(array);

        System.out.println("Prefix sum up to index 3 (3+2-1+6): " + bit.queryPrefix(3));
        System.out.println("Range sum [2..5] (-1+6+5+4): " + bit.queryRange(2, 5));

        System.out.println("\nAdding +10 to index 3 (value 6 -> 16)...");
        bit.add(3, 10);
        System.out.println("New prefix sum up to index 3: " + bit.queryPrefix(3));
        System.out.println("New range sum [2..5]: " + bit.queryRange(2, 5));

        System.out.println("\n--- Binary Lifting / Lower Bound Search ---");
        int[] positiveArr = {1, 2, 4, 3, 5};
        System.out.println("Positive array: " + Arrays.toString(positiveArr));
        FenwickTree posBit = new FenwickTree(positiveArr);
        long target = 7;
        int boundIdx = posBit.lowerBound(target);
        System.out.printf("Smallest 0-based index with prefix sum >= %d is: %d (sum = %d)%n",
                target, boundIdx, posBit.queryPrefix(boundIdx));

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
