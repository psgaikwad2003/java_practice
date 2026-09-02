import java.util.HashMap;
import java.util.Map;


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

        System.out.println("\nAccessing key 'A' (get('A')): " + cache.get("A")); 
        cache.displayState();

        System.out.println("\nPutting key 'D' (put('D', 'Date')) - Capacity full, LRU key 'B' evicted!");
        cache.put("D", "Date");
        cache.displayState();

        System.out.println("\nAccessing key 'B' (evicted key, get('B')): " + cache.get("B")); 

        System.out.println("\nUpdating key 'C' (put('C', 'Cranberry'))");
        cache.put("C", "Cranberry"); 
        cache.displayState();
    }
}


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


class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.get(key);
        removeNode(node);
        addNodeToHead(node);
        return node.value;
    }

    
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> existingNode = map.get(key);
            existingNode.value = value;
            removeNode(existingNode);
            addNodeToHead(existingNode);
        } else {
            if (map.size() >= capacity) {
                
                Node<K, V> lruNode = tail.prev;
                map.remove(lruNode.key);
                removeNode(lruNode);
            }
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addNodeToHead(newNode);
        }
    }

    
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    
    private void addNodeToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    
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
