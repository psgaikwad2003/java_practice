import java.util.*;

/**
 * Custom Generic Min-Heap (Priority Queue) implementation from scratch.
 *
 * Characteristics:
 * - Backed by a dynamic array representing a complete binary tree.
 * - Parent-Child relationships:
 *     parent(i) = (i - 1) / 2
 *     leftChild(i) = 2 * i + 1
 *     rightChild(i) = 2 * i + 2
 * - Complexity:
 *     - Insert: O(log n)
 *     - Extract Min: O(log n)
 *     - Peek: O(1)
 *     - Build Heap from array: O(n) Floyd's algorithm
 */
public class MinHeapPriorityQueueDemo {

    public static class MinHeap<T extends Comparable<T>> {
        private Object[] heap;
        private int size;
        private final Comparator<? super T> comparator;
        private static final int DEFAULT_CAPACITY = 10;

        public MinHeap() {
            this(DEFAULT_CAPACITY, null);
        }

        public MinHeap(int initialCapacity) {
            this(initialCapacity, null);
        }

        public MinHeap(Comparator<? super T> comparator) {
            this(DEFAULT_CAPACITY, comparator);
        }

        public MinHeap(int initialCapacity, Comparator<? super T> comparator) {
            this.heap = new Object[Math.max(initialCapacity, 1)];
            this.size = 0;
            this.comparator = comparator;
        }

        /**
         * Floyd's O(n) linear-time heap construction.
         */
        @SuppressWarnings("unchecked")
        public static <E extends Comparable<E>> MinHeap<E> buildHeap(E[] elements) {
            MinHeap<E> minHeap = new MinHeap<>(elements.length);
            for (E elem : elements) {
                minHeap.heap[minHeap.size++] = elem;
            }
            // Sift down all non-leaf nodes starting from the bottom
            for (int i = (minHeap.size / 2) - 1; i >= 0; i--) {
                minHeap.heapifyDown(i);
            }
            return minHeap;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("Heap is empty.");
            }
            return (T) heap[0];
        }

        public void insert(T value) {
            ensureCapacity();
            heap[size] = value;
            heapifyUp(size);
            size++;
        }

        @SuppressWarnings("unchecked")
        public T extractMin() {
            if (isEmpty()) {
                throw new NoSuchElementException("Heap is empty.");
            }

            T minVal = (T) heap[0];
            heap[0] = heap[size - 1];
            heap[size - 1] = null; // Prevent memory leak
            size--;

            if (size > 0) {
                heapifyDown(0);
            }

            return minVal;
        }

        /**
         * Replaces the minimum root element with a new value in a single O(log n) pass.
         * More efficient than calling extractMin() followed by insert().
         *
         * @param newValue the value to insert at root
         * @return the previous minimum value
         */
        @SuppressWarnings("unchecked")
        public T replaceTop(T newValue) {
            if (isEmpty()) {
                throw new NoSuchElementException("Heap is empty.");
            }
            T oldMin = (T) heap[0];
            heap[0] = newValue;
            heapifyDown(0);
            return oldMin;
        }

        /**
         * In-place Heap Sort algorithm (O(n log n) time, O(1) auxiliary space).
         */
        public static <E extends Comparable<E>> void heapSort(E[] arr) {
            if (arr == null || arr.length <= 1) return;
            int n = arr.length;
            for (int i = (n / 2) - 1; i >= 0; i--) {
                maxHeapify(arr, n, i);
            }
            for (int i = n - 1; i > 0; i--) {
                E temp = arr[0];
                arr[0] = arr[i];
                arr[i] = temp;
                maxHeapify(arr, i, 0);
            }
        }

        private static <E extends Comparable<E>> void maxHeapify(E[] arr, int n, int i) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && arr[left].compareTo(arr[largest]) > 0) {
                largest = left;
            }
            if (right < n && arr[right].compareTo(arr[largest]) > 0) {
                largest = right;
            }
            if (largest != i) {
                E swap = arr[i];
                arr[i] = arr[largest];
                arr[largest] = swap;
                maxHeapify(arr, n, largest);
            }
        }

        /**
         * Checks if the heap contains the specified element.
         *
         * @param value element to search for
         * @return true if found, false otherwise
         */
        public boolean contains(T value) {
            return indexOf(value) != -1;
        }

        /**
         * Removes a single instance of the specified element from the heap, if present.
         *
         * @param value element to be removed
         * @return true if an element was removed
         */
        public boolean remove(T value) {
            int index = indexOf(value);
            if (index == -1) return false;

            int lastIdx = size - 1;
            if (index == lastIdx) {
                heap[lastIdx] = null;
                size--;
                return true;
            }

            heap[index] = heap[lastIdx];
            heap[lastIdx] = null;
            size--;

            heapifyDown(index);
            heapifyUp(index);
            return true;
        }

        private int indexOf(T value) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(heap[i], value)) {
                    return i;
                }
            }
            return -1;
        }

        private void heapifyUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (compare(index, parentIndex) < 0) {
                    swap(index, parentIndex);
                    index = parentIndex;
                } else {
                    break;
                }
            }
        }

        private void heapifyDown(int index) {
            while (leftChildIndex(index) < size) {
                int smallestChildIndex = leftChildIndex(index);
                int rightChild = rightChildIndex(index);

                if (rightChild < size && compare(rightChild, smallestChildIndex) < 0) {
                    smallestChildIndex = rightChild;
                }

                if (compare(index, smallestChildIndex) > 0) {
                    swap(index, smallestChildIndex);
                    index = smallestChildIndex;
                } else {
                    break;
                }
            }
        }

        private int leftChildIndex(int index) {
            return (2 * index) + 1;
        }

        private int rightChildIndex(int index) {
            return (2 * index) + 2;
        }

        @SuppressWarnings("unchecked")
        private int compare(int i, int j) {
            if (comparator != null) {
                return comparator.compare((T) heap[i], (T) heap[j]);
            }
            return ((T) heap[i]).compareTo((T) heap[j]);
        }

        private void swap(int i, int j) {
            Object temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        private void ensureCapacity() {
            if (size >= heap.length) {
                heap = Arrays.copyOf(heap, heap.length * 2);
            }
        }
    }

    /**
     * Application: Find K largest elements using Min-Heap of size K.
     */
    public static List<Integer> findKLargest(int[] nums, int k) {
        if (nums == null || k <= 0 || k > nums.length) {
            return Collections.emptyList();
        }

        MinHeap<Integer> minHeap = new MinHeap<>(k);
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.insert(num);
            } else if (num > minHeap.peek()) {
                minHeap.extractMin();
                minHeap.insert(num);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.extractMin());
        }
        return result;
    }

    /**
     * Priority Task item for demonstration.
     */
    public static class Task implements Comparable<Task> {
        private final String name;
        private final int priority; // Lower integer = higher priority

        public Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.priority, other.priority);
        }

        @Override
        public String toString() {
            return String.format("[Priority %d] %s", priority, name);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     CUSTOM GENERIC MIN-HEAP DEMO         ");
        System.out.println("==========================================");

        // Demo 1: Basic Insert & Extract-Min
        System.out.println("\n[1] Basic Insertions & Priority Extractions:");
        MinHeap<Integer> heap = new MinHeap<>();
        int[] values = {15, 10, 20, 8, 12, 25, 5};
        for (int v : values) {
            heap.insert(v);
        }

        System.out.print("    Extracted in ascending order: ");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " ");
        }
        System.out.println();

        // Demo 2: Linear-time O(n) Floyd Heapify
        System.out.println("\n[2] Floyd's O(n) Batch Build Heap:");
        Integer[] rawData = {40, 10, 30, 50, 20, 15, 5};
        MinHeap<Integer> builtHeap = MinHeap.buildHeap(rawData);
        System.out.print("    Extracted from O(n) built heap: ");
        while (!builtHeap.isEmpty()) {
            System.out.print(builtHeap.extractMin() + " ");
        }
        System.out.println();

        // Demo 3: K-Largest Elements using size-K Min-Heap
        int[] numbers = {3, 2, 1, 5, 6, 4, 9, 8, 7};
        int k = 4;
        System.out.println("\n[3] Find Top " + k + " Largest Elements:");
        System.out.println("    Input: " + Arrays.toString(numbers));
        System.out.println("    Top " + k + " elements: " + findKLargest(numbers, k));

        // Demo 4: Custom Priority Queue Simulation
        System.out.println("\n[4] Operating System Task Scheduler Simulation:");
        MinHeap<Task> scheduler = new MinHeap<>();
        scheduler.insert(new Task("Render UI Frame", 3));
        scheduler.insert(new Task("Respond to Hardware Interrupt", 0));
        scheduler.insert(new Task("Background Disk Cleanup", 5));
        scheduler.insert(new Task("Process Network Packet", 1));

        while (!scheduler.isEmpty()) {
            System.out.println("    Executing: " + scheduler.extractMin());
        }

        // Demo 5: Arbitrary Element Removal & Contains
        System.out.println("\n[5] Arbitrary Element Removal and Contains Check:");
        MinHeap<Integer> removalHeap = new MinHeap<>();
        int[] vals = {50, 30, 20, 15, 10, 8, 16};
        for (int v : vals) removalHeap.insert(v);
        System.out.println("    Contains 20? " + removalHeap.contains(20));
        System.out.println("    Removing 20: " + removalHeap.remove(20));
        System.out.println("    Contains 20 after removal? " + removalHeap.contains(20));

        // Demo 6: Max-Heap using Reverse Comparator
        System.out.println("\n[6] Max-Heap using Custom Reverse Comparator:");
        MinHeap<Integer> maxHeap = new MinHeap<>(Comparator.<Integer>reverseOrder());
        for (int v : new int[]{12, 45, 19, 99, 3}) maxHeap.insert(v);
        System.out.print("    Extracted in descending order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.extractMin() + " ");
        }
        System.out.println();

        // Demo 7: Single-pass replaceTop
        System.out.println("\n[7] Single-Pass replaceTop Optimization:");
        MinHeap<Integer> topHeap = new MinHeap<>();
        for (int v : new int[]{10, 20, 30, 40}) topHeap.insert(v);
        System.out.println("    Current peek: " + topHeap.peek());
        int oldMin = topHeap.replaceTop(25);
        System.out.println("    Replaced top " + oldMin + " with 25. New peek: " + topHeap.peek());

        // Demo 8: In-place Heap Sort
        Integer[] unsorted = {64, 25, 12, 22, 11, 90};
        System.out.println("\n[8] In-Place Heap Sort:");
        System.out.println("    Original: " + Arrays.toString(unsorted));
        MinHeap.heapSort(unsorted);
        System.out.println("    Sorted:   " + Arrays.toString(unsorted));

        System.out.println("\nAll Min-Heap Priority Queue tests passed successfully.");
    }
}
