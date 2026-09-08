import java.util.StringJoiner;

/**
 * Demonstrates a custom Generic Singly Linked List with essential operations:
 * - Insertion (head, tail, at index)
 * - Deletion (by value, by index)
 * - In-place Reversal
 * - Cycle Detection using Floyd's Cycle-Finding Algorithm (Tortoise and Hare)
 */
public class LinkedListDemo {

    public static class CustomLinkedList<T> {
        private static class Node<T> {
            T data;
            Node<T> next;

            Node(T data) {
                this.data = data;
                this.next = null;
            }
        }

        private Node<T> head;
        private Node<T> tail;
        private int size;

        public CustomLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Prepends an element to the front of the list in O(1) time.
         */
        public void insertFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
            size++;
        }

        /**
         * Appends an element to the end of the list in O(1) time using tail pointer.
         */
        public void insertLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        /**
         * Inserts an element at a specified 0-based index.
         */
        public void insertAt(int index, T data) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            if (index == 0) {
                insertFirst(data);
                return;
            }
            if (index == size) {
                insertLast(data);
                return;
            }

            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            Node<T> newNode = new Node<>(data);
            newNode.next = current.next;
            current.next = newNode;
            size++;
        }

        /**
         * Deletes the first occurrence of the specified value.
         */
        public boolean delete(T data) {
            if (isEmpty()) return false;

            if (head.data.equals(data)) {
                head = head.next;
                if (head == null) tail = null;
                size--;
                return true;
            }

            Node<T> current = head;
            while (current.next != null && !current.next.data.equals(data)) {
                current = current.next;
            }

            if (current.next != null) {
                if (current.next == tail) {
                    tail = current;
                }
                current.next = current.next.next;
                size--;
                return true;
            }

            return false;
        }

        /**
         * Reverses the linked list in-place in O(n) time and O(1) auxiliary space.
         */
        public void reverse() {
            Node<T> prev = null;
            Node<T> current = head;
            tail = head;

            while (current != null) {
                Node<T> nextNode = current.next;
                current.next = prev;
                prev = current;
                current = nextNode;
            }

            head = prev;
        }

        /**
         * Detects whether the linked list contains a cycle using Floyd's Tortoise and Hare algorithm.
         * Time Complexity: O(n), Space Complexity: O(1)
         */
        public boolean hasCycle() {
            if (head == null || head.next == null) return false;

            Node<T> slow = head;
            Node<T> fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;

                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Creates a cycle for demonstration purposes (pointing tail to node at targetIndex).
         */
        public void createCycleForDemo(int targetIndex) {
            if (targetIndex < 0 || targetIndex >= size) return;
            Node<T> target = head;
            for (int i = 0; i < targetIndex; i++) {
                target = target.next;
            }
            tail.next = target;
        }

        @Override
        public String toString() {
            if (isEmpty()) return "[]";
            StringJoiner joiner = new StringJoiner(" -> ", "[", " -> null]");
            Node<T> current = head;
            int count = 0;
            while (current != null && count < size + 2) { // prevent infinite loop if cycle exists
                joiner.add(String.valueOf(current.data));
                current = current.next;
                count++;
            }
            return joiner.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       Custom Generic Singly Linked List Demo     ");
        System.out.println("==================================================");

        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        // Insertions
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);
        list.insertFirst(5);
        list.insertAt(2, 15); // Insert 15 at index 2

        System.out.println("List after insertions: " + list);
        System.out.println("Size: " + list.size());

        // Deletions
        System.out.println("\n=== Deletion Demonstration ===");
        list.delete(15);
        System.out.println("After deleting 15: " + list);
        list.delete(5);
        System.out.println("After deleting 5 (head): " + list);

        // In-Place Reversal
        System.out.println("\n=== In-Place Reversal ===");
        System.out.println("Before reverse: " + list);
        list.reverse();
        System.out.println("After reverse:  " + list);

        // Cycle Detection Demonstration
        System.out.println("\n=== Floyd's Cycle Detection Algorithm ===");
        System.out.println("Does current list have cycle? " + list.hasCycle());

        System.out.println("Creating intentional cycle (tail -> node 1)...");
        list.createCycleForDemo(1);
        System.out.println("Does list have cycle now? " + list.hasCycle());
    }
}
