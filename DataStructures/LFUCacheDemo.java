package DataStructures;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Demonstrates a Least Frequently Used (LFU) Cache with O(1) time complexity
 * for both get and put operations.
 * 
 * Features:
 * - Evicts the key with the lowest access frequency.
 * - Ties in frequency are broken using LRU (Least Recently Used) order.
 * - Tracks minFrequency for immediate O(1) victim identification.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class LFUCacheDemo<K, V> {

    private final int capacity;
    private int minFrequency;
    private final Map<K, V> values;
    private final Map<K, Integer> counts;
    private final Map<Integer, LinkedHashSet<K>> frequencyLists;

    public LFUCacheDemo(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        this.capacity = capacity;
        this.minFrequency = 0;
        this.values = new HashMap<>();
        this.counts = new HashMap<>();
        this.frequencyLists = new HashMap<>();
        this.frequencyLists.put(1, new LinkedHashSet<>());
    }

    /**
     * Retrieves value associated with key and increments its frequency.
     * Time: O(1)
     *
     * @param key the lookup key
     * @return the value or null if not found
     */
    public V get(K key) {
        if (!values.containsKey(key)) {
            return null;
        }

        // Increment count and update frequency lists
        int count = counts.get(key);
        counts.put(key, count + 1);

        frequencyLists.get(count).remove(key);
        if (count == minFrequency && frequencyLists.get(count).isEmpty()) {
            minFrequency++;
        }

        frequencyLists.computeIfAbsent(count + 1, k -> new LinkedHashSet<>()).add(key);
        return values.get(key);
    }

    /**
     * Inserts or updates key-value pair. Evicts LFU (and LRU on tie) if at capacity.
     * Time: O(1)
     *
     * @param key key
     * @param value value
     */
    public void put(K key, V value) {
        if (values.containsKey(key)) {
            values.put(key, value);
            get(key); // reuse get to increment frequency
            return;
        }

        if (values.size() >= capacity) {
            // Evict least frequently used (and oldest in that frequency set)
            LinkedHashSet<K> minList = frequencyLists.get(minFrequency);
            K evictKey = minList.iterator().next();
            minList.remove(evictKey);
            values.remove(evictKey);
            counts.remove(evictKey);
        }

        // Insert new entry
        values.put(key, value);
        counts.put(key, 1);
        minFrequency = 1;
        frequencyLists.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    }

    public int size() {
        return values.size();
    }

    public int getFrequency(K key) {
        return counts.getOrDefault(key, 0);
    }

    @Override
    public String toString() {
        return "LFUCache{size=" + values.size() + ", minFreq=" + minFrequency + ", items=" + values + "}";
    }

    public static void main(String[] args) {
        System.out.println("=== LFU (Least Frequently Used) Cache Demonstration ===\n");

        LFUCacheDemo<Integer, String> cache = new LFUCacheDemo<>(3);

        System.out.println("1. Inserting (1, 'One'), (2, 'Two'), (3, 'Three')...");
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        System.out.println("State: " + cache);

        System.out.println("\n2. Accessing key 1 twice and key 2 once...");
        cache.get(1);
        cache.get(1);
        cache.get(2);
        System.out.printf("Frequencies: Key 1 -> %d, Key 2 -> %d, Key 3 -> %d%n",
                cache.getFrequency(1), cache.getFrequency(2), cache.getFrequency(3));

        System.out.println("\n3. Inserting key 4 (Capacity full, key 3 has freq 1 -> should evict key 3)...");
        cache.put(4, "Four");
        System.out.println("State: " + cache);
        System.out.println("Get key 3 (evicted): " + cache.get(3));
        System.out.println("Get key 4 (present): " + cache.get(4));

        System.out.println("\n4. Accessing key 4 and inserting key 5...");
        cache.get(4); // Freq 4 becomes 2
        // Current freqs: Key 1: 3, Key 2: 2, Key 4: 2.
        // Between Key 2 and Key 4 (both freq 2), Key 2 was accessed earlier -> Key 2 is evicted!
        cache.put(5, "Five");
        System.out.println("State: " + cache);
        System.out.println("Get key 2 (evicted by tie-breaking LRU): " + cache.get(2));
        System.out.println("Get key 1: " + cache.get(1));
        System.out.println("Get key 4: " + cache.get(4));
        System.out.println("Get key 5: " + cache.get(5));
    }
}
