import java.util.StringJoiner;

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
            while (current != null && count < size + 2) {
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

        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);
        list.insertFirst(5);
        list.insertAt(2, 15);

        System.out.println("List after insertions: " + list);
        System.out.println("Size: " + list.size());

        System.out.println("\n=== Deletion Demonstration ===");
        list.delete(15);
        System.out.println("After deleting 15: " + list);
        list.delete(5);
        System.out.println("After deleting 5 (head): " + list);

        System.out.println("\n=== In-Place Reversal ===");
        System.out.println("Before reverse: " + list);
        list.reverse();
        System.out.println("After reverse:  " + list);

        System.out.println("\n=== Floyd's Cycle Detection Algorithm ===");
        System.out.println("Does current list have cycle? " + list.hasCycle());

        System.out.println("Creating intentional cycle (tail -> node 1)...");
        list.createCycleForDemo(1);
        System.out.println("Does list have cycle now? " + list.hasCycle());
    }
}
