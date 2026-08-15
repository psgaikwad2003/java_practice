import java.util.HashMap;
import java.util.Map;

/**
 * Technical Interview Question: Design a Least Recently Used (LRU) Cache
 * 
 * Frequently asked in product & tech company interviews (Amazon, Microsoft, Google, Uber).
 * Requirements:
 * 1. get(key) - Get value of key if key exists, else return -1. Time complexity: O(1)
 * 2. put(key, value) - Update or insert key-value pair. If capacity exceeded, evict LRU item. Time complexity: O(1)
 * 
 * Data Structure Choice: HashMap + Doubly Linked List
 */
public class LRUCacheDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Testing Custom LRU Cache Implementation (Capacity = 3)");
        System.out.println("==================================================");

        LRUCache cache = new LRUCache(3);

        System.out.println("Putting: (1, 100), (2, 200), (3, 300)");
        cache.put(1, 100);
        cache.put(2, 200);
        cache.put(3, 300);
        cache.displayState();

        System.out.println("\nAccessing key 1 (get(1)): " + cache.get(1)); // 1 becomes Most Recently Used
        cache.displayState();

        System.out.println("\nPutting key 4 (put(4, 400)) - Capacity full, LRU key 2 evicted!");
        cache.put(4, 400);
        cache.displayState();

        System.out.println("\nAccessing key 2 (evicted key, get(2)): " + cache.get(2)); // -1

        System.out.println("\nUpdating key 3 (put(3, 350))");
        cache.put(3, 350); // 3 updated and becomes MRU
        cache.displayState();

        System.out.println("\nPutting key 5 (put(5, 500)) - Capacity full, LRU key 1 evicted!");
        cache.put(5, 500);
        cache.displayState();
    }
}

/**
 * Node class representing an entry in Doubly Linked List.
 */
class Node {
    int key;
    int value;
    Node prev;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

/**
 * Custom LRU Cache class.
 */
class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head;
    private final Node tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        // Dummy head and tail nodes to avoid edge-case checks
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Retrieves the value associated with the key.
     * Moves the node to head (Most Recently Used).
     */
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node node = map.get(key);
        removeNode(node);
        addNodeToHead(node);
        return node.value;
    }

    /**
     * Inserts or updates key-value pair.
     * Evicts Least Recently Used (tail.prev) node if capacity limit exceeded.
     */
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node existingNode = map.get(key);
            existingNode.value = value;
            removeNode(existingNode);
            addNodeToHead(existingNode);
        } else {
            if (map.size() >= capacity) {
                // Evict LRU element (node right before dummy tail)
                Node lruNode = tail.prev;
                map.remove(lruNode.key);
                removeNode(lruNode);
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addNodeToHead(newNode);
        }
    }

    /**
     * Helper to remove node from its current position in doubly linked list.
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * Helper to insert node right after dummy head (making it Most Recently Used).
     */
    private void addNodeToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * Helper to display current state of the cache from MRU to LRU.
     */
    public void displayState() {
        StringBuilder sb = new StringBuilder("Current Cache (MRU -> LRU): [");
        Node curr = head.next;
        while (curr != tail) {
            sb.append("{Key=").append(curr.key).append(", Val=").append(curr.value).append("}");
            if (curr.next != tail) {
                sb.append(" <-> ");
            }
            curr = curr.next;
        }
        sb.append("]");
        System.out.println(sb.toString());
    }
}
