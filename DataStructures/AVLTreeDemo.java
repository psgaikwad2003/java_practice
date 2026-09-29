package DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates a Self-Balancing AVL (Adelson-Velsky and Landis) Binary Search Tree.
 *
 * An AVL tree maintains a balance factor in {-1, 0, 1} for every node by performing
 * tree rotations during insertions and deletions.
 *
 * Guaranteed Time Complexities:
 * - Search: O(log n)
 * - Insert: O(log n)
 * - Delete: O(log n)
 * - Space:  O(n)
 *
 * Rotations Covered:
 * 1. Left-Left (LL) Case   -> Right Rotation
 * 2. Right-Right (RR) Case -> Left Rotation
 * 3. Left-Right (LR) Case  -> Left Rotation on child, then Right Rotation on root
 * 4. Right-Left (RL) Case  -> Right Rotation on child, then Left Rotation on root
 */
public class AVLTreeDemo {

    public static class AVLTree<T extends Comparable<T>> {

        public static class Node<T> {
            T key;
            int height;
            Node<T> left;
            Node<T> right;

            Node(T key) {
                this.key = key;
                this.height = 1; // Leaf height is 1
            }
        }

        private Node<T> root;
        private int size;

        public AVLTree() {
            this.root = null;
            this.size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int height() {
            return height(root);
        }

        private int height(Node<T> node) {
            return node == null ? 0 : node.height;
        }

        private int getBalance(Node<T> node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }

        private void updateHeight(Node<T> node) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }

        /**
         * Right rotation (LL Case)
         *
         *       y                               x
         *      / \     Right Rotation          / \
         *     x   T3   -------------->        T1  y
         *    / \                                 / \
         *   T1  T2                              T2  T3
         */
        private Node<T> rightRotate(Node<T> y) {
            Node<T> x = y.left;
            Node<T> t2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = t2;

            // Update heights (y first, then x)
            updateHeight(y);
            updateHeight(x);

            return x;
        }

        /**
         * Left rotation (RR Case)
         *
         *     x                                 y
         *    / \       Left Rotation           / \
         *   T1  y      ------------->         x   T3
         *      / \                           / \
         *     T2  T3                        T1  T2
         */
        private Node<T> leftRotate(Node<T> x) {
            Node<T> y = x.right;
            Node<T> t2 = y.left;

            // Perform rotation
            y.left = x;
            x.right = t2;

            // Update heights (x first, then y)
            updateHeight(x);
            updateHeight(y);

            return y;
        }

        /**
         * Inserts a key into the AVL tree, balancing if needed.
         */
        public void insert(T key) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            root = insertRec(root, key);
        }

        private Node<T> insertRec(Node<T> node, T key) {
            // 1. Standard BST insertion
            if (node == null) {
                size++;
                return new Node<>(key);
            }

            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = insertRec(node.left, key);
            } else if (cmp > 0) {
                node.right = insertRec(node.right, key);
            } else {
                // Duplicate keys are not added
                return node;
            }

            // 2. Update height of current ancestor node
            updateHeight(node);

            // 3. Get balance factor to check if this node became unbalanced
            int balance = getBalance(node);

            // 4. Handle 4 rotation cases:

            // Case 1: Left-Left (LL)
            if (balance > 1 && key.compareTo(node.left.key) < 0) {
                return rightRotate(node);
            }

            // Case 2: Right-Right (RR)
            if (balance < -1 && key.compareTo(node.right.key) > 0) {
                return leftRotate(node);
            }

            // Case 3: Left-Right (LR)
            if (balance > 1 && key.compareTo(node.left.key) > 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Case 4: Right-Left (RL)
            if (balance < -1 && key.compareTo(node.right.key) < 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        /**
         * Deletes a key from the AVL tree, balancing if needed.
         */
        public boolean delete(T key) {
            if (key == null || !contains(key)) return false;
            root = deleteRec(root, key);
            size--;
            return true;
        }

        private Node<T> deleteRec(Node<T> node, T key) {
            if (node == null) return null;

            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = deleteRec(node.left, key);
            } else if (cmp > 0) {
                node.right = deleteRec(node.right, key);
            } else {
                // Node to be deleted found!

                // Case 1 & 2: One child or No child
                if (node.left == null || node.right == null) {
                    Node<T> temp = (node.left != null) ? node.left : node.right;
                    if (temp == null) {
                        // No child case
                        node = null;
                    } else {
                        // One child case
                        node = temp;
                    }
                } else {
                    // Case 3: Node with two children
                    // Get inorder successor (smallest in the right subtree)
                    Node<T> successor = getMinValueNode(node.right);
                    node.key = successor.key;
                    // Delete inorder successor
                    node.right = deleteRec(node.right, successor.key);
                }
            }

            // If the tree had only one node
            if (node == null) return null;

            // Update height
            updateHeight(node);

            // Check balance factor
            int balance = getBalance(node);

            // Handle 4 rotation cases for deletion:
            // Case 1: Left-Left
            if (balance > 1 && getBalance(node.left) >= 0) {
                return rightRotate(node);
            }

            // Case 2: Left-Right
            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Case 3: Right-Right
            if (balance < -1 && getBalance(node.right) <= 0) {
                return leftRotate(node);
            }

            // Case 4: Right-Left
            if (balance < -1 && getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        private Node<T> getMinValueNode(Node<T> node) {
            Node<T> curr = node;
            while (curr.left != null) {
                curr = curr.left;
            }
            return curr;
        }

        public boolean contains(T key) {
            if (key == null) return false;
            Node<T> curr = root;
            while (curr != null) {
                int cmp = key.compareTo(curr.key);
                if (cmp == 0) return true;
                curr = (cmp < 0) ? curr.left : curr.right;
            }
            return false;
        }

        public List<T> inOrder() {
            List<T> result = new ArrayList<>();
            inOrderRec(root, result);
            return result;
        }

        private void inOrderRec(Node<T> node, List<T> list) {
            if (node != null) {
                inOrderRec(node.left, list);
                list.add(node.key);
                inOrderRec(node.right, list);
            }
        }

        /**
         * Validates that the AVL property holds for all nodes in the tree.
         */
        public boolean isAVLValid() {
            return isAVLValidRec(root);
        }

        private boolean isAVLValidRec(Node<T> node) {
            if (node == null) return true;
            int balance = getBalance(node);
            if (Math.abs(balance) > 1) return false;
            return isAVLValidRec(node.left) && isAVLValidRec(node.right);
        }

        /**
         * Pretty-prints the AVL tree hierarchy with balance factors.
         */
        public void printTree() {
            System.out.println("Tree Visualization (size=" + size + ", height=" + height() + "):");
            if (root == null) {
                System.out.println("  (empty tree)");
                return;
            }
            printTreeRec(root, "", true);
        }

        private void printTreeRec(Node<T> node, String prefix, boolean isTail) {
            if (node != null) {
                System.out.println(prefix + (isTail ? "\\-- " : "+-- ") 
                        + node.key + " (h=" + node.height + ", bf=" + getBalance(node) + ")");
                
                List<Node<T>> children = new ArrayList<>();
                if (node.left != null || node.right != null) {
                    children.add(node.left);
                    children.add(node.right);
                }

                for (int i = 0; i < children.size(); i++) {
                    Node<T> child = children.get(i);
                    boolean isLast = (i == children.size() - 1);
                    if (child != null) {
                        printTreeRec(child, prefix + (isTail ? "    " : "|   "), isLast);
                    } else {
                        System.out.println(prefix + (isTail ? "    " : "|   ") + (isLast ? "\\-- (null)" : "+-- (null)"));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("         AVL Tree Data Structure Demo            ");
        System.out.println("=================================================\n");

        AVLTree<Integer> avl = new AVLTree<>();

        // Test 1: Triggering Left-Left and Right-Right Rotations
        System.out.println("1. Inserting elements causing Rotations (10, 20, 30, 40, 50, 25):");
        int[] valuesToInsert = {10, 20, 30, 40, 50, 25};
        for (int v : valuesToInsert) {
            System.out.println("--> Inserting: " + v);
            avl.insert(v);
        }

        System.out.println("\nAVL Structure after insertions:");
        avl.printTree();
        System.out.println("In-order Traversal (Sorted): " + avl.inOrder());
        System.out.println("Is AVL Property Valid? " + avl.isAVLValid());

        // Test 2: Search Operations
        System.out.println("\n2. Search Verification:");
        int[] testSearches = {25, 30, 99, 10};
        for (int target : testSearches) {
            System.out.println("Contains " + target + "? " + avl.contains(target));
        }

        // Test 3: Deletions and Re-balancing
        System.out.println("\n3. Deleting leaf node (10):");
        avl.delete(10);
        avl.printTree();
        System.out.println("Is AVL Property Valid? " + avl.isAVLValid());

        System.out.println("\n4. Deleting node with two children (30):");
        avl.delete(30);
        avl.printTree();
        System.out.println("Is AVL Property Valid? " + avl.isAVLValid());

        System.out.println("\n5. Inserting more elements (5, 15, 27, 60):");
        for (int v : new int[]{5, 15, 27, 60}) {
            avl.insert(v);
        }
        avl.printTree();
        System.out.println("In-order: " + avl.inOrder());
        System.out.println("Is AVL Property Valid? " + avl.isAVLValid());
        System.out.println("Tree height: " + avl.height() + ", Total nodes: " + avl.size());
    }
}
