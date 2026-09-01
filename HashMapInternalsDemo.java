import java.util.*;


public class HashMapInternalsDemo {

    
    static class EmployeeKey {
        private final int id;
        private final String department;

        EmployeeKey(int id, String department) {
            this.id = id;
            this.department = department;
        }

        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmployeeKey that = (EmployeeKey) o;
            return id == that.id && Objects.equals(department, that.department);
        }

        
        @Override
        public int hashCode() {
            return Objects.hash(id, department);
        }

        @Override
        public String toString() {
            return "EmpKey{" + id + ", " + department + "}";
        }
    }

    
    static class BadKey {
        int value;

        BadKey(int value) {
            this.value = value;
        }

        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return value == ((BadKey) o).value;
        }
        
    }

    
    static class CollidingKey {
        private final String name;

        CollidingKey(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            return 42; 
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

    
    static void demoBasicOperations() {
        System.out.println("── 1. Basic HashMap Operations ─────────────────");

        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = {"java", "python", "java", "go", "python", "java"};

        for (String w : words) {
            wordCount.merge(w, 1, Integer::sum); 
        }

        System.out.println("Word frequencies : " + wordCount);
        System.out.println("Contains 'java'? : " + wordCount.containsKey("java"));
        System.out.println("getOrDefault 'rust': " + wordCount.getOrDefault("rust", 0));

        
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

    
    static void demoHashCodeEqualsContract() {
        System.out.println("── 2. Correct hashCode/equals Contract ─────────");

        Map<EmployeeKey, String> directory = new HashMap<>();
        directory.put(new EmployeeKey(101, "Engineering"), "Alice");
        directory.put(new EmployeeKey(102, "Marketing"), "Bob");
        directory.put(new EmployeeKey(101, "Engineering"), "Alice Updated");

        
        EmployeeKey lookupKey = new EmployeeKey(101, "Engineering");
        System.out.println("Lookup result  : " + directory.get(lookupKey));
        System.out.println("Map size (no dup): " + directory.size());
        System.out.println();
    }

    
    static void demoBrokenContract() {
        System.out.println("── 3. Broken Contract — Lost Entries ───────────");

        Map<BadKey, String> broken = new HashMap<>();
        BadKey key1 = new BadKey(1);
        broken.put(key1, "value-1");

        
        BadKey key2 = new BadKey(1);
        System.out.println("key1.equals(key2)        : " + key1.equals(key2));
        System.out.println("key1.hashCode==key2.hash  : " + (key1.hashCode() == key2.hashCode()));
        System.out.println("Lookup with equal key     : " + broken.get(key2)); 
        System.out.println("⚠ Entry is LOST because hashCode contract is violated.\n");
    }

    
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

    
    static void demoRehashing() {
        System.out.println("── 5. Rehashing & Capacity ─────────────────────");

        
        Map<Integer, String> map = new HashMap<>(4, 0.75f);
        for (int i = 1; i <= 10; i++) {
            map.put(i, "val-" + i);
        }

        System.out.println("Inserted 10 entries into initial capacity 4");
        System.out.println("Multiple rehashes occurred: 4 → 8 → 16");
        System.out.println("Final map size   : " + map.size());
        System.out.println("Tip: Pre-size with new HashMap<>(expectedSize / 0.75 + 1)\n");
    }

    
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

        
        Map<String, String> lru = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3; 
            }
        };
        lru.put("A", "1");
        lru.put("B", "2");
        lru.put("C", "3");
        lru.get("A");          
        lru.put("D", "4");    
        System.out.println("LRU cache after eviction : " + lru);
        System.out.println();
    }

    
    static void demoConcurrentModification() {
        System.out.println("── 7. ConcurrentModificationException ──────────");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 45);
        scores.put("Carol", 92);
        scores.put("Dave", 38);

        
        
        
        

        
        Iterator<Map.Entry<String, Integer>> it = scores.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() < 50) {
                it.remove();
            }
        }
        System.out.println("After removing <50 (iterator): " + scores);

        
        scores.put("Eve", 42);
        scores.entrySet().removeIf(e -> e.getValue() < 50);
        System.out.println("After removeIf(<50)          : " + scores);
        System.out.println();
    }
}
