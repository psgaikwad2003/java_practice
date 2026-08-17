import java.util.*;

/**
 * HashMapInternalsDemo.java
 * 
 * Demonstrates how HashMap works internally in Java:
 *  - Hashing and bucket placement
 *  - Collision handling (chaining)
 *  - Custom hashCode() and equals() contract
 *  - Rehashing behavior when load factor is exceeded
 *  - ConcurrentModificationException pitfall
 *  - LinkedHashMap for insertion-order iteration
 *  - TreeMap for sorted-key iteration
 * 
 * Common interview topic: "Explain how HashMap works internally."
 */
public class HashMapInternalsDemo {

    // ─── Custom Key demonstrating hashCode/equals contract ───────────────
    static class EmployeeKey {
        private final int id;
        private final String department;

        EmployeeKey(int id, String department) {
            this.id = id;
            this.department = department;
        }

        /**
         * Two EmployeeKeys are equal if both id AND department match.
         * Rule: If a.equals(b) then a.hashCode() == b.hashCode()
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmployeeKey that = (EmployeeKey) o;
            return id == that.id && Objects.equals(department, that.department);
        }

        /**
         * Consistent with equals — uses the same fields.
         * A poor hash (e.g., always returning 1) would degrade O(1) to O(n).
         */
        @Override
        public int hashCode() {
            return Objects.hash(id, department);
        }

        @Override
        public String toString() {
            return "EmpKey{" + id + ", " + department + "}";
        }
    }

    // ─── Deliberately bad key — violates the hashCode/equals contract ────
    static class BadKey {
        int value;

        BadKey(int value) {
            this.value = value;
        }

        // equals uses 'value', but hashCode is Object's default (memory-based).
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return value == ((BadKey) o).value;
        }
        // No hashCode override — CONTRACT VIOLATION!
    }

    // ─── Force collisions by making every key hash to the same bucket ────
    static class CollidingKey {
        private final String name;

        CollidingKey(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            return 42; // Every instance maps to the same bucket
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return Objects.equals(name, ((CollidingKey) o).name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════");
        System.out.println("        HashMap Internals Deep Dive");
        System.out.println("════════════════════════════════════════════════\n");

        demoBasicOperations();
        demoHashCodeEqualsContract();
        demoBrokenContract();
        demoCollisionBehavior();
        demoRehashing();
        demoOrderedMaps();
        demoConcurrentModification();
    }

    /**
     * 1. Basic put/get/containsKey — O(1) average time.
     */
    static void demoBasicOperations() {
        System.out.println("── 1. Basic HashMap Operations ─────────────────");

        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = {"java", "python", "java", "go", "python", "java"};

        for (String w : words) {
            wordCount.merge(w, 1, Integer::sum); // idiomatic frequency count
        }

        System.out.println("Word frequencies : " + wordCount);
        System.out.println("Contains 'java'? : " + wordCount.containsKey("java"));
        System.out.println("getOrDefault 'rust': " + wordCount.getOrDefault("rust", 0));

        // computeIfAbsent — lazy initialization pattern
        Map<String, List<String>> groupedAnagrams = new HashMap<>();
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        for (String s : input) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            groupedAnagrams.computeIfAbsent(sorted, k -> new ArrayList<>()).add(s);
        }
        System.out.println("Grouped anagrams : " + groupedAnagrams);
        System.out.println();
    }

    /**
     * 2. Proper hashCode/equals contract — lookups work correctly.
     */
    static void demoHashCodeEqualsContract() {
        System.out.println("── 2. Correct hashCode/equals Contract ─────────");

        Map<EmployeeKey, String> directory = new HashMap<>();
        directory.put(new EmployeeKey(101, "Engineering"), "Alice");
        directory.put(new EmployeeKey(102, "Marketing"), "Bob");
        directory.put(new EmployeeKey(101, "Engineering"), "Alice Updated");

        // Lookup with a NEW object that is .equals() to the stored key
        EmployeeKey lookupKey = new EmployeeKey(101, "Engineering");
        System.out.println("Lookup result  : " + directory.get(lookupKey));
        System.out.println("Map size (no dup): " + directory.size());
        System.out.println();
    }

    /**
     * 3. What happens when the contract is BROKEN.
     */
    static void demoBrokenContract() {
        System.out.println("── 3. Broken Contract — Lost Entries ───────────");

        Map<BadKey, String> broken = new HashMap<>();
        BadKey key1 = new BadKey(1);
        broken.put(key1, "value-1");

        // New BadKey with same value — equals() says true, but hashCode differs
        BadKey key2 = new BadKey(1);
        System.out.println("key1.equals(key2)        : " + key1.equals(key2));
        System.out.println("key1.hashCode==key2.hash  : " + (key1.hashCode() == key2.hashCode()));
        System.out.println("Lookup with equal key     : " + broken.get(key2)); // null!
        System.out.println("⚠ Entry is LOST because hashCode contract is violated.\n");
    }

    /**
     * 4. Collision handling — all keys land in the same bucket.
     *    Java 8+: bucket converts from linked list → red-black tree at threshold 8.
     */
    static void demoCollisionBehavior() {
        System.out.println("── 4. Hash Collision Behavior ──────────────────");

        Map<CollidingKey, Integer> collisionMap = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            collisionMap.put(new CollidingKey("key-" + i), i);
        }

        System.out.println("All 12 keys share hashCode=42");
        System.out.println("Map size         : " + collisionMap.size());
        System.out.println("Lookup 'key-5'   : " + collisionMap.get(new CollidingKey("key-5")));
        System.out.println("Note: Java 8+ converts chain to tree when bucket size ≥ 8\n");
    }

    /**
     * 5. Rehashing — capacity doubles when size > capacity × loadFactor.
     *    Default: initialCapacity=16, loadFactor=0.75 → rehash at size 13.
     */
    static void demoRehashing() {
        System.out.println("── 5. Rehashing & Capacity ─────────────────────");

        // Small initial capacity to trigger rehash quickly
        Map<Integer, String> map = new HashMap<>(4, 0.75f);
        for (int i = 1; i <= 10; i++) {
            map.put(i, "val-" + i);
        }

        System.out.println("Inserted 10 entries into initial capacity 4");
        System.out.println("Multiple rehashes occurred: 4 → 8 → 16");
        System.out.println("Final map size   : " + map.size());
        System.out.println("Tip: Pre-size with new HashMap<>(expectedSize / 0.75 + 1)\n");
    }

    /**
     * 6. LinkedHashMap (insertion order) vs TreeMap (sorted order).
     */
    static void demoOrderedMaps() {
        System.out.println("── 6. LinkedHashMap vs TreeMap ─────────────────");

        Map<String, Integer> linked = new LinkedHashMap<>();
        Map<String, Integer> sorted = new TreeMap<>();

        String[] langs = {"Python", "Java", "Go", "Rust", "C++"};
        for (int i = 0; i < langs.length; i++) {
            linked.put(langs[i], i + 1);
            sorted.put(langs[i], i + 1);
        }

        System.out.println("LinkedHashMap (insertion) : " + linked.keySet());
        System.out.println("TreeMap       (sorted)    : " + sorted.keySet());

        // LinkedHashMap as LRU cache (access-order mode)
        Map<String, String> lru = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3; // evict when size exceeds 3
            }
        };
        lru.put("A", "1");
        lru.put("B", "2");
        lru.put("C", "3");
        lru.get("A");          // A becomes most-recently-used
        lru.put("D", "4");    // B is evicted (least recently used)
        System.out.println("LRU cache after eviction : " + lru);
        System.out.println();
    }

    /**
     * 7. ConcurrentModificationException — and how to avoid it.
     */
    static void demoConcurrentModification() {
        System.out.println("── 7. ConcurrentModificationException ──────────");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 45);
        scores.put("Carol", 92);
        scores.put("Dave", 38);

        // ❌ WRONG: modifying map during enhanced for-loop
        // for (Map.Entry<String, Integer> e : scores.entrySet()) {
        //     if (e.getValue() < 50) scores.remove(e.getKey()); // throws!
        // }

        // ✅ CORRECT approach 1: Iterator.remove()
        Iterator<Map.Entry<String, Integer>> it = scores.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() < 50) {
                it.remove();
            }
        }
        System.out.println("After removing <50 (iterator): " + scores);

        // ✅ CORRECT approach 2: removeIf (Java 8+)
        scores.put("Eve", 42);
        scores.entrySet().removeIf(e -> e.getValue() < 50);
        System.out.println("After removeIf(<50)          : " + scores);
        System.out.println();
    }
}
