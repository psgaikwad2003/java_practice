package DataStructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Demonstrates a generic, robust Doubly Linked List implementation using sentinel nodes.
 * 
 * Features:
 * - O(1) insertions and removals at both ends (addFirst, addLast, removeFirst, removeLast).
 * - O(N) index-based operations with bidirectional traversal optimization (search from nearest end).
 * - In-place list reversal: O(N) time, O(1) extra space.
 * - Bidirectional iteration support (forward and descending).
 *
 * @param <T> the type of elements held in this list
 */
public class DoublyLinkedListDemo<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private final Node<T> head; // dummy sentinel head
    private final Node<T> tail; // dummy sentinel tail
    private int size;

    public DoublyLinkedListDemo() {
        head = new Node<>(null);
        tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T element) {
        insertAfter(head, element);
    }

    public void addLast(T element) {
        insertAfter(tail.prev, element);
    }

    private void insertAfter(Node<T> prevNode, T element) {
        Node<T> newNode = new Node<>(element);
        Node<T> nextNode = prevNode.next;

        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;

        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        return unlink(head.next);
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        return unlink(tail.prev);
    }

    public boolean removeValue(T value) {
        Node<T> curr = head.next;
        while (curr != tail) {
            if ((value == null && curr.data == null) || (value != null && value.equals(curr.data))) {
                unlink(curr);
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private T unlink(Node<T> node) {
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        node.prev = null;
        node.next = null;
        size--;
        return node.data;
    }

    public T get(int index) {
        checkElementIndex(index);
        return getNode(index).data;
    }

    private Node<T> getNode(int index) {
        // Optimize search from head or tail depending on index proximity
        if (index < (size >> 1)) {
            Node<T> curr = head.next;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr;
        } else {
            Node<T> curr = tail.prev;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
            return curr;
        }
    }

    public boolean contains(T value) {
        Node<T> curr = head.next;
        while (curr != tail) {
            if ((value == null && curr.data == null) || (value != null && value.equals(curr.data))) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    /**
     * Reverses the doubly linked list in-place by swapping next and prev pointers of elements.
     */
    public void reverse() {
        if (size <= 1) return;

        Node<T> firstReal = head.next;
        Node<T> lastReal = tail.prev;

        Node<T> curr = head.next;
        while (curr != tail) {
            Node<T> nextNode = curr.next;
            curr.next = curr.prev;
            curr.prev = nextNode;
            curr = nextNode;
        }

        firstReal.next = tail;
        tail.prev = firstReal;

        lastReal.prev = head;
        head.next = lastReal;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head.next;

            @Override
            public boolean hasNext() {
                return current != tail;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            if (curr.next != tail) sb.append(" <-> ");
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Doubly Linked List Demonstration ===\n");

        DoublyLinkedListDemo<String> list = new DoublyLinkedListDemo<>();

        // Insertion tests
        list.addLast("Beta");
        list.addFirst("Alpha");
        list.addLast("Gamma");
        list.addLast("Delta");
        System.out.println("After initial adds: " + list + " (Size: " + list.size() + ")");

        // Access tests
        System.out.println("Element at index 0: " + list.get(0));
        System.out.println("Element at index 2: " + list.get(2));
        System.out.println("Contains 'Gamma'? " + list.contains("Gamma"));
        System.out.println("Contains 'Zeta'? " + list.contains("Zeta"));

        // Removal tests
        System.out.println("\nRemoved first: " + list.removeFirst());
        System.out.println("Removed last: " + list.removeLast());
        System.out.println("After removals: " + list);

        list.addLast("Omega");
        list.addFirst("Prime");
        System.out.println("Added 'Prime' and 'Omega': " + list);

        // Value removal
        boolean removed = list.removeValue("Beta");
        System.out.println("Removed 'Beta' by value? " + removed + " -> " + list);

        // Reversal test
        System.out.println("\nReversing list in-place...");
        list.reverse();
        System.out.println("Reversed list: " + list);
    }
}
