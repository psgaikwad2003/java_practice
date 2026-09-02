import java.util.EmptyStackException;
import java.util.NoSuchElementException;


public class CustomDataStructuresDemo {

    static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    
    public static class CustomStack<T> {
        private Node<T> top;
        private int size = 0;

        public void push(T element) {
            Node<T> newNode = new Node<>(element);
            newNode.next = top;
            top = newNode;
            size++;
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            T data = top.data;
            top = top.next;
            size--;
            return data;
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return top.data;
        }

        public boolean isEmpty() {
            return top == null;
        }

        public int size() {
            return size;
        }
    }

    
    public static class CustomQueue<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public void enqueue(T element) {
            Node<T> newNode = new Node<>(element);
            if (tail != null) {
                tail.next = newNode;
            }
            tail = newNode;
            if (head == null) {
                head = tail;
            }
            size++;
        }

        public T dequeue() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue is empty!");
            }
            T data = head.data;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
            return data;
        }

        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue is empty!");
            }
            return head.data;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int size() {
            return size;
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. Testing Custom Stack (LIFO)");
        System.out.println("==================================================");
        CustomStack<String> stack = new CustomStack<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");

        System.out.println("Top element (peek): " + stack.peek());
        System.out.println("Popped element: " + stack.pop());
        System.out.println("Popped element: " + stack.pop());
        System.out.println("Stack size: " + stack.size());

        System.out.println("\n==================================================");
        System.out.println(" 2. Testing Custom Queue (FIFO)");
        System.out.println("==================================================");
        CustomQueue<Integer> queue = new CustomQueue<>();
        queue.enqueue(100);
        queue.enqueue(200);
        queue.enqueue(300);

        System.out.println("Front element (peek): " + queue.peek());
        System.out.println("Dequeued element: " + queue.dequeue());
        System.out.println("Dequeued element: " + queue.dequeue());
        System.out.println("Queue size: " + queue.size());
    }
}
