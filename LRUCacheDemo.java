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
        System.out.println(" Testing Custom Generic LRU Cache (Capacity = 3)");
        System.out.println("==================================================");

        LRUCache<String, String> cache = new LRUCache<>(3);

        System.out.println("Putting: ('A', 'Apple'), ('B', 'Banana'), ('C', 'Cherry')");
        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cherry");
        cache.displayState();

        System.out.println("\nAccessing key 'A' (get('A')): " + cache.get("A")); // 'A' becomes MRU
        cache.displayState();

        System.out.println("\nPutting key 'D' (put('D', 'Date')) - Capacity full, LRU key 'B' evicted!");
        cache.put("D", "Date");
        cache.displayState();

        System.out.println("\nAccessing key 'B' (evicted key, get('B')): " + cache.get("B")); // null

        System.out.println("\nUpdating key 'C' (put('C', 'Cranberry'))");
        cache.put("C", "Cranberry"); // 'C' updated and becomes MRU
        cache.displayState();
    }
}

/**
 * Node class representing an entry in Doubly Linked List with Generic Types.
 */
class Node<K, V> {
    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

/**
 * Custom Generic LRU Cache class.
 */
class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        // Dummy head and tail nodes to avoid edge-case checks
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Retrieves the value associated with the key.
     * Moves the node to head (Most Recently Used).
     */
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.get(key);
        removeNode(node);
        addNodeToHead(node);
        return node.value;
    }

    /**
     * Inserts or updates key-value pair.
     * Evicts Least Recently Used (tail.prev) node if capacity limit exceeded.
     */
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> existingNode = map.get(key);
            existingNode.value = value;
            removeNode(existingNode);
            addNodeToHead(existingNode);
        } else {
            if (map.size() >= capacity) {
                // Evict LRU element (node right before dummy tail)
                Node<K, V> lruNode = tail.prev;
                map.remove(lruNode.key);
                removeNode(lruNode);
            }
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addNodeToHead(newNode);
        }
    }

    /**
     * Helper to remove node from its current position in doubly linked list.
     */
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * Helper to insert node right after dummy head (making it Most Recently Used).
     */
    private void addNodeToHead(Node<K, V> node) {
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
        Node<K, V> curr = head.next;
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
