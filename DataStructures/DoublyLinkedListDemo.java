package DataStructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedListDemo<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private final Node<T> head;
    private final Node<T> tail;
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

    public void insertAt(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            Node<T> succ = getNode(index);
            insertAfter(succ.prev, element);
        }
    }

    public T removeAt(int index) {
        checkElementIndex(index);
        return unlink(getNode(index));
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

    public Iterator<T> descendingIterator() {
        return new Iterator<>() {
            private Node<T> current = tail.prev;

            @Override
            public boolean hasNext() {
                return current != head;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.prev;
                return data;
            }
        };
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<T> curr = head.next; curr != tail; curr = curr.next) {
            result[i++] = curr.data;
        }
        return result;
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

        list.addLast("Beta");
        list.addFirst("Alpha");
        list.addLast("Gamma");
        list.addLast("Delta");
        System.out.println("After initial adds: " + list + " (Size: " + list.size() + ")");

        System.out.println("Element at index 0: " + list.get(0));
        System.out.println("Element at index 2: " + list.get(2));
        System.out.println("Contains 'Gamma'? " + list.contains("Gamma"));
        System.out.println("Contains 'Zeta'? " + list.contains("Zeta"));

        System.out.println("\nRemoved first: " + list.removeFirst());
        System.out.println("Removed last: " + list.removeLast());
        System.out.println("After removals: " + list);

        list.addLast("Omega");
        list.addFirst("Prime");
        System.out.println("Added 'Prime' and 'Omega': " + list);

        boolean removed = list.removeValue("Beta");
        System.out.println("Removed 'Beta' by value? " + removed + " -> " + list);

        System.out.println("\nReversing list in-place...");
        list.reverse();
        System.out.println("Reversed list: " + list);

        System.out.println("\n=== Index Insertion & Removal Tests ===");
        list.insertAt(1, "InsertedAt1");
        System.out.println("After insertAt(1, 'InsertedAt1'): " + list);
        String removedAt1 = list.removeAt(1);
        System.out.println("Removed at index 1: " + removedAt1 + " -> " + list);

        System.out.print("\nDescending Iteration: ");
        Iterator<String> descIt = list.descendingIterator();
        while (descIt.hasNext()) {
            System.out.print(descIt.next() + " ");
        }
        System.out.println();

        Object[] arr = list.toArray();
        System.out.println("toArray() length: " + arr.length + ", content: " + java.util.Arrays.toString(arr));
    }
}
