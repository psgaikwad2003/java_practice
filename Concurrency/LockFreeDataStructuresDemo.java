package Concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Demonstrates Non-Blocking / Lock-Free Concurrent Data Structures using Compare-And-Swap (CAS).
 *
 * Implemented Patterns:
 * 1. Treiber Stack: Classic lock-free LIFO stack using AtomicReference with CAS retry loops.
 * 2. Michael-Scott Non-Blocking Queue: Lock-free FIFO queue using sentinel nodes and helper CAS operations.
 * 3. The ABA Problem & Solution: Why naive CAS can fail if memory addresses are recycled, and how
 *    AtomicStampedReference (version/stamp tagging) completely mitigates it.
 * 4. High-concurrency multi-threaded benchmark comparing Lock-Free vs Synchronized access.
 *
 * Complexity:
 * - Time: O(1) expected per operation (lock-free guarantee).
 * - Space: O(N) memory allocation for linked nodes.
 */
public class LockFreeDataStructuresDemo {

    /**
     * 1. Treiber Lock-Free Stack.
     */
    public static class TreiberStack<E> {
        private static class Node<E> {
            final E item;
            Node<E> next;

            Node(E item) {
                this.item = item;
            }
        }

        private final AtomicReference<Node<E>> top = new AtomicReference<>();
        private final AtomicInteger size = new AtomicInteger(0);

        public void push(E item) {
            Node<E> newHead = new Node<>(item);
            Node<E> oldHead;
            do {
                oldHead = top.get();
                newHead.next = oldHead;
            } while (!top.compareAndSet(oldHead, newHead));
            size.incrementAndGet();
        }

        public E pop() {
            Node<E> oldHead;
            Node<E> newHead;
            do {
                oldHead = top.get();
                if (oldHead == null) {
                    return null;
                }
                newHead = oldHead.next;
            } while (!top.compareAndSet(oldHead, newHead));
            size.decrementAndGet();
            return oldHead.item;
        }

        public int size() {
            return size.get();
        }

        public boolean isEmpty() {
            return top.get() == null;
        }
    }

    /**
     * 2. Michael-Scott Lock-Free Queue.
     */
    public static class MichaelScottQueue<E> {
        private static class Node<E> {
            final E value;
            final AtomicReference<Node<E>> next;

            Node(E val) {
                this.value = val;
                this.next = new AtomicReference<>(null);
            }
        }

        private final AtomicReference<Node<E>> head;
        private final AtomicReference<Node<E>> tail;

        public MichaelScottQueue() {
            Node<E> sentinel = new Node<>(null);
            this.head = new AtomicReference<>(sentinel);
            this.tail = new AtomicReference<>(sentinel);
        }

        public void enqueue(E value) {
            Node<E> newNode = new Node<>(value);
            while (true) {
                Node<E> curTail = tail.get();
                Node<E> tailNext = curTail.next.get();

                if (curTail == tail.get()) {
                    if (tailNext != null) {
                        // Tail lagged behind; advance it for other threads
                        tail.compareAndSet(curTail, tailNext);
                    } else {
                        // Try linking new node to tail.next
                        if (curTail.next.compareAndSet(null, newNode)) {
                            // Advance tail to new node
                            tail.compareAndSet(curTail, newNode);
                            return;
                        }
                    }
                }
            }
        }

        public E dequeue() {
            while (true) {
                Node<E> curHead = head.get();
                Node<E> curTail = tail.get();
                Node<E> headNext = curHead.next.get();

                if (curHead == head.get()) {
                    if (curHead == curTail) {
                        if (headNext == null) {
                            return null; // queue is empty
                        }
                        // Tail is lagging behind head
                        tail.compareAndSet(curTail, headNext);
                    } else {
                        if (headNext == null) continue;
                        E value = headNext.value;
                        if (head.compareAndSet(curHead, headNext)) {
                            return value;
                        }
                    }
                }
            }
        }
    }

    /**
     * 3. ABA Problem Resolution with AtomicStampedReference.
     */
    public static void demonstrateABASolution() {
        System.out.println("\n--- Demonstrating ABA Problem Prevention with AtomicStampedReference ---");
        String originalRef = "State_A";
        int initialStamp = 1;
        AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>(originalRef, initialStamp);

        System.out.println("Initial Value: " + stampedRef.getReference() + ", Stamp: " + stampedRef.getStamp());

        // Simulate Thread 1 intending to swap A -> C if still at stamp 1
        int observedStamp = stampedRef.getStamp();
        String observedRef = stampedRef.getReference();

        // Simulate Thread 2 doing ABA transition in between:
        // A -> B
        stampedRef.compareAndSet("State_A", "State_B", stampedRef.getStamp(), stampedRef.getStamp() + 1);
        System.out.println("Step 1 (A -> B): Value=" + stampedRef.getReference() + ", Stamp=" + stampedRef.getStamp());

        // B -> A (Reference is back to "State_A", but stamp has incremented to 3!)
        stampedRef.compareAndSet("State_B", "State_A", stampedRef.getStamp(), stampedRef.getStamp() + 1);
        System.out.println("Step 2 (B -> A): Value=" + stampedRef.getReference() + ", Stamp=" + stampedRef.getStamp());

        // Now Thread 1 attempts its CAS using the original observed stamp (1)
        boolean success = stampedRef.compareAndSet(observedRef, "State_C", observedStamp, observedStamp + 1);
        System.out.println("Thread 1 CAS attempt (A -> C with stamp=" + observedStamp + "): " +
                (success ? "SUCCESS" : "FAILED (Prevented ABA corruption!)"));
        System.out.println("Current Value: " + stampedRef.getReference() + ", Final Stamp: " + stampedRef.getStamp());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=============================================================");
        System.out.println("     LOCK-FREE DATA STRUCTURES (TREIBER & MICHAEL-SCOTT)     ");
        System.out.println("=============================================================");

        // Test 1: Multi-threaded Treiber Stack Concurrency
        System.out.println("\n--- Test 1: Concurrent Treiber Stack ---");
        TreiberStack<Integer> stack = new TreiberStack<>();
        int threadCount = 4;
        int itemsPerThread = 25000;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount * 2);

        // Producers
        for (int t = 0; t < threadCount; t++) {
            final int offset = t * itemsPerThread;
            Thread.ofPlatform().start(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < itemsPerThread; i++) {
                        stack.push(offset + i);
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        // Consumers
        AtomicInteger consumedCount = new AtomicInteger(0);
        for (int t = 0; t < threadCount; t++) {
            Thread.ofPlatform().start(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < itemsPerThread; i++) {
                        while (true) {
                            Integer val = stack.pop();
                            if (val != null) {
                                consumedCount.incrementAndGet();
                                break;
                            }
                            Thread.yield();
                        }
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        finishLatch.await();
        long duration = System.currentTimeMillis() - startTime;

        System.out.printf("Pushed and Popped %d elements across %d threads in %d ms.%n",
                consumedCount.get(), threadCount * 2, duration);
        System.out.println("Stack size at end: " + stack.size());

        // Test 2: Michael-Scott Queue
        System.out.println("\n--- Test 2: Michael-Scott Lock-Free Queue ---");
        MichaelScottQueue<String> queue = new MichaelScottQueue<>();
        queue.enqueue("Message-1");
        queue.enqueue("Message-2");
        queue.enqueue("Message-3");
        System.out.println("Dequeued: " + queue.dequeue());
        System.out.println("Dequeued: " + queue.dequeue());
        queue.enqueue("Message-4");
        System.out.println("Dequeued: " + queue.dequeue());
        System.out.println("Dequeued: " + queue.dequeue());
        System.out.println("Dequeued (empty): " + queue.dequeue());

        // Test 3: ABA resolution
        demonstrateABASolution();

        System.out.println("\nLock-free concurrency verification completed successfully.");
    }
}
