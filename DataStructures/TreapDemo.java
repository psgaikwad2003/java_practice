package DataStructures;

import java.util.*;

public class TreapDemo {

    public static class Treap<K extends Comparable<K>, V> {
        private static final Random RNG = new Random(42);

        public static class Node<K, V> {
            K key;
            V value;
            int priority;
            int size;
            Node<K, V> left;
            Node<K, V> right;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.priority = RNG.nextInt();
                this.size = 1;
            }

            void updateSize() {
                this.size = 1 + getSize(this.left) + getSize(this.right);
            }
        }

        private Node<K, V> root;

        private static <K, V> int getSize(Node<K, V> node) {
            return node == null ? 0 : node.size;
        }

        public int size() {
            return getSize(root);
        }

        public boolean isEmpty() {
            return root == null;
        }

        public record NodePair<K, V>(Node<K, V> left, Node<K, V> right) {}

        public NodePair<K, V> split(Node<K, V> node, K key) {
            if (node == null) {
                return new NodePair<>(null, null);
            }

            if (node.key.compareTo(key) <= 0) {
                NodePair<K, V> rightSplit = split(node.right, key);
                node.right = rightSplit.left;
                node.updateSize();
                return new NodePair<>(node, rightSplit.right);
            } else {
                NodePair<K, V> leftSplit = split(node.left, key);
                node.left = leftSplit.right;
                node.updateSize();
                return new NodePair<>(leftSplit.left, node);
            }
        }

        public Node<K, V> merge(Node<K, V> left, Node<K, V> right) {
            if (left == null) return right;
            if (right == null) return left;

            if (left.priority > right.priority) {
                left.right = merge(left.right, right);
                left.updateSize();
                return left;
            } else {
                right.left = merge(left, right.left);
                right.updateSize();
                return right;
            }
        }

        public void insert(K key, V value) {

            delete(key);

            Node<K, V> newNode = new Node<>(key, value);
            NodePair<K, V> parts = split(root, key);
            root = merge(merge(parts.left, newNode), parts.right);
        }

        public boolean delete(K key) {
            if (find(key) == null) return false;

            NodePair<K, V> p1 = split(root, key);

            Node<K, V> targetSubtree = p1.left;
            Node<K, V> removedLeft = removeMaxNode(targetSubtree);
            root = merge(removedLeft, p1.right);
            return true;
        }

        private Node<K, V> removeMaxNode(Node<K, V> node) {
            if (node == null) return null;
            if (node.right == null) {
                return node.left;
            }
            node.right = removeMaxNode(node.right);
            node.updateSize();
            return node;
        }

        public V find(K key) {
            Node<K, V> curr = root;
            while (curr != null) {
                int cmp = key.compareTo(curr.key);
                if (cmp == 0) return curr.value;
                curr = (cmp < 0) ? curr.left : curr.right;
            }
            return null;
        }

        public K findKthSmallest(int k) {
            if (k < 1 || k > size()) {
                throw new IndexOutOfBoundsException("Rank " + k + " out of bounds for size " + size());
            }
            return findKthSmallest(root, k).key;
        }

        private Node<K, V> findKthSmallest(Node<K, V> node, int k) {
            int leftSize = getSize(node.left);
            if (k == leftSize + 1) {
                return node;
            } else if (k <= leftSize) {
                return findKthSmallest(node.left, k);
            } else {
                return findKthSmallest(node.right, k - leftSize - 1);
            }
        }

        public List<K> getRange(K low, K high) {
            List<K> result = new ArrayList<>();
            inorderRange(root, low, high, result);
            return result;
        }

        private void inorderRange(Node<K, V> node, K low, K high, List<K> result) {
            if (node == null) return;
            if (node.key.compareTo(low) > 0) {
                inorderRange(node.left, low, high, result);
            }
            if (node.key.compareTo(low) >= 0 && node.key.compareTo(high) <= 0) {
                result.add(node.key);
            }
            if (node.key.compareTo(high) < 0) {
                inorderRange(node.right, low, high, result);
            }
        }

        public void printInOrder() {
            List<String> entries = new ArrayList<>();
            inorder(root, entries);
            System.out.println("In-Order Treap: " + entries);
        }

        private void inorder(Node<K, V> node, List<String> list) {
            if (node == null) return;
            inorder(node.left, list);
            list.add(node.key + "(sz=" + node.size + ")");
            inorder(node.right, list);
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("           RANDOMIZED TREAP (SPLIT & MERGE) DEMO             ");
        System.out.println("=============================================================");

        Treap<Integer, String> treap = new Treap<>();
        int[] keys = {50, 20, 80, 10, 30, 70, 90, 25, 35};

        System.out.println("\n--- Inserting elements into Treap ---");
        for (int k : keys) {
            treap.insert(k, "Val-" + k);
        }
        treap.printInOrder();
        System.out.println("Total elements in Treap: " + treap.size());

        System.out.println("\n--- Search Tests ---");
        System.out.println("Find(30): " + treap.find(30));
        System.out.println("Find(99): " + treap.find(99));

        System.out.println("\n--- Order Statistics (k-th smallest element) ---");
        for (int k = 1; k <= treap.size(); k++) {
            System.out.printf("Rank %d smallest: %d%n", k, treap.findKthSmallest(k));
        }

        System.out.println("\n--- Range Query [25, 75] ---");
        List<Integer> range = treap.getRange(25, 75);
        System.out.println("Keys in [25, 75]: " + range);

        System.out.println("\n--- Deletion Test ---");
        System.out.println("Deleting key 30...");
        treap.delete(30);
        treap.printInOrder();
        System.out.println("New size: " + treap.size());
        System.out.println("Find(30) after deletion: " + treap.find(30));

        System.out.println("\nTreap verification completed successfully.");
    }
}
