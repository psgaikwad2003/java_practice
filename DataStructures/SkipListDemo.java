package DataStructures;

import java.util.*;

public class SkipListDemo {

    public static class SkipList<K extends Comparable<K>, V> {
        private static final int MAX_LEVEL = 16;
        private static final double P = 0.5;

        public static class Node<K, V> {
            public final K key;
            public V value;
            public final Node<K, V>[] forward;

            @SuppressWarnings("unchecked")
            public Node(K key, V value, int level) {
                this.key = key;
                this.value = value;
                this.forward = new Node[level + 1];
            }

            @Override
            public String toString() {
                return "[" + key + ":" + value + "]";
            }
        }

        private final Node<K, V> head;
        private int currentLevel;
        private int size;
        private final Random random;

        public SkipList() {
            this(new Random());
        }

        public SkipList(Random random) {
            this.random = random;
            this.head = new Node<>(null, null, MAX_LEVEL);
            this.currentLevel = 0;
            this.size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        private int randomLevel() {
            int level = 0;
            while (level < MAX_LEVEL && random.nextDouble() < P) {
                level++;
            }
            return level;
        }

        public V get(K key) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");

            Node<K, V> curr = head;
            for (int i = currentLevel; i >= 0; i--) {
                while (curr.forward[i] != null && curr.forward[i].key.compareTo(key) < 0) {
                    curr = curr.forward[i];
                }
            }

            curr = curr.forward[0];
            if (curr != null && curr.key.compareTo(key) == 0) {
                return curr.value;
            }
            return null;
        }

        public V searchWithTrace(K key) {
            System.out.printf("  Tracing search for key [%s]:%n", key);
            Node<K, V> curr = head;
            int totalSteps = 0;

            for (int i = currentLevel; i >= 0; i--) {
                System.out.printf("    Level %2d: Start at %s", i, (curr.key == null ? "Head" : "[" + curr.key + "]"));
                while (curr.forward[i] != null && curr.forward[i].key.compareTo(key) < 0) {
                    curr = curr.forward[i];
                    totalSteps++;
                    System.out.printf(" -> [%s]", curr.key);
                }
                System.out.println(" (drop down)");
            }

            curr = curr.forward[0];
            if (curr != null && curr.key.compareTo(key) == 0) {
                System.out.printf("    FOUND [%s -> %s] after %d traversal steps!%n", curr.key, curr.value, totalSteps);
                return curr.value;
            } else {
                System.out.printf("    NOT FOUND for key [%s] (checked %d steps)%n", key, totalSteps);
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        public void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");

            Node<K, V>[] update = new Node[MAX_LEVEL + 1];
            Node<K, V> curr = head;

            for (int i = currentLevel; i >= 0; i--) {
                while (curr.forward[i] != null && curr.forward[i].key.compareTo(key) < 0) {
                    curr = curr.forward[i];
                }
                update[i] = curr;
            }

            curr = curr.forward[0];

            if (curr != null && curr.key.compareTo(key) == 0) {
                curr.value = value;
                return;
            }

            int newLevel = randomLevel();

            if (newLevel > currentLevel) {
                for (int i = currentLevel + 1; i <= newLevel; i++) {
                    update[i] = head;
                }
                currentLevel = newLevel;
            }

            Node<K, V> newNode = new Node<>(key, value, newLevel);
            for (int i = 0; i <= newLevel; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }

            size++;
        }

        @SuppressWarnings("unchecked")
        public boolean remove(K key) {
            if (key == null) return false;

            Node<K, V>[] update = new Node[MAX_LEVEL + 1];
            Node<K, V> curr = head;

            for (int i = currentLevel; i >= 0; i--) {
                while (curr.forward[i] != null && curr.forward[i].key.compareTo(key) < 0) {
                    curr = curr.forward[i];
                }
                update[i] = curr;
            }

            curr = curr.forward[0];

            if (curr == null || curr.key.compareTo(key) != 0) {
                return false;
            }

            for (int i = 0; i <= currentLevel; i++) {
                if (update[i].forward[i] != curr) {
                    break;
                }
                update[i].forward[i] = curr.forward[i];
            }

            while (currentLevel > 0 && head.forward[currentLevel] == null) {
                currentLevel--;
            }

            size--;
            return true;
        }

        public List<Map.Entry<K, V>> range(K fromKey, K toKey) {
            List<Map.Entry<K, V>> result = new ArrayList<>();
            if (fromKey == null || toKey == null || fromKey.compareTo(toKey) > 0) {
                return result;
            }

            Node<K, V> curr = head;
            for (int i = currentLevel; i >= 0; i--) {
                while (curr.forward[i] != null && curr.forward[i].key.compareTo(fromKey) < 0) {
                    curr = curr.forward[i];
                }
            }

            curr = curr.forward[0];
            while (curr != null && curr.key.compareTo(toKey) <= 0) {
                result.add(Map.entry(curr.key, curr.value));
                curr = curr.forward[0];
            }

            return result;
        }

        public void printStructure() {
            System.out.printf("SkipList Structure (size=%d, maxLevel=%d):%n", size, currentLevel);
            if (size == 0) {
                System.out.println("  (empty skip list)");
                return;
            }

            for (int i = currentLevel; i >= 0; i--) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("  Level %2d: Head", i));
                Node<K, V> curr = head.forward[i];
                while (curr != null) {
                    sb.append(" ---> [").append(curr.key).append("]");
                    curr = curr.forward[i];
                }
                sb.append(" ---> NIL");
                System.out.println(sb);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("          Probabilistic Skip List Demo           ");
        System.out.println("=================================================\n");

        Random random = new Random(42);
        SkipList<Integer, String> skipList = new SkipList<>(random);

        System.out.println("1. Inserting keys into SkipList (10, 20, 30, 40, 50, 25, 15, 35, 60, 5):");
        int[] keys = {10, 20, 30, 40, 50, 25, 15, 35, 60, 5};
        for (int k : keys) {
            skipList.put(k, "Val-" + k);
        }

        skipList.printStructure();

        System.out.println("2. Express Lane Search Tracing:");
        skipList.searchWithTrace(35);
        skipList.searchWithTrace(60);
        skipList.searchWithTrace(99);

        System.out.println("\n3. Range Query [15 to 40]:");
        List<Map.Entry<Integer, String>> rangeResults = skipList.range(15, 40);
        System.out.println("  Matches: " + rangeResults);

        System.out.println("\n4. Deletion of elements (25, 40, 5):");
        for (int k : new int[]{25, 40, 5}) {
            boolean removed = skipList.remove(k);
            System.out.printf("  Removed %d? %b%n", k, removed);
        }

        System.out.println("\nSkipList structure after deletions:");
        skipList.printStructure();

        System.out.println("5. Final Membership Verification:");
        for (int k : new int[]{5, 10, 15, 20, 25, 30, 35, 40, 50, 60}) {
            System.out.printf("  Key %2d present? %s%n", k, skipList.get(k) != null);
        }
    }
}
