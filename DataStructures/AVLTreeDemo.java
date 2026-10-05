package DataStructures;

import java.util.ArrayList;
import java.util.List;

public class AVLTreeDemo {

    public static class AVLTree<T extends Comparable<T>> {

        public static class Node<T> {
            T key;
            int height;
            Node<T> left;
            Node<T> right;

            Node(T key) {
                this.key = key;
                this.height = 1;
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

        private Node<T> rightRotate(Node<T> y) {
            Node<T> x = y.left;
            Node<T> t2 = x.right;

            x.right = y;
            y.left = t2;

            updateHeight(y);
            updateHeight(x);

            return x;
        }

        private Node<T> leftRotate(Node<T> x) {
            Node<T> y = x.right;
            Node<T> t2 = y.left;

            y.left = x;
            x.right = t2;

            updateHeight(x);
            updateHeight(y);

            return y;
        }

        public void insert(T key) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            root = insertRec(root, key);
        }

        private Node<T> insertRec(Node<T> node, T key) {

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

                return node;
            }

            updateHeight(node);

            int balance = getBalance(node);

            if (balance > 1 && key.compareTo(node.left.key) < 0) {
                return rightRotate(node);
            }

            if (balance < -1 && key.compareTo(node.right.key) > 0) {
                return leftRotate(node);
            }

            if (balance > 1 && key.compareTo(node.left.key) > 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            if (balance < -1 && key.compareTo(node.right.key) < 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

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

                if (node.left == null || node.right == null) {
                    Node<T> temp = (node.left != null) ? node.left : node.right;
                    if (temp == null) {

                        node = null;
                    } else {

                        node = temp;
                    }
                } else {

                    Node<T> successor = getMinValueNode(node.right);
                    node.key = successor.key;

                    node.right = deleteRec(node.right, successor.key);
                }
            }

            if (node == null) return null;

            updateHeight(node);

            int balance = getBalance(node);

            if (balance > 1 && getBalance(node.left) >= 0) {
                return rightRotate(node);
            }

            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            if (balance < -1 && getBalance(node.right) <= 0) {
                return leftRotate(node);
            }

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

        public boolean isAVLValid() {
            return isAVLValidRec(root);
        }

        private boolean isAVLValidRec(Node<T> node) {
            if (node == null) return true;
            int balance = getBalance(node);
            if (Math.abs(balance) > 1) return false;
            return isAVLValidRec(node.left) && isAVLValidRec(node.right);
        }

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

        System.out.println("\n2. Search Verification:");
        int[] testSearches = {25, 30, 99, 10};
        for (int target : testSearches) {
            System.out.println("Contains " + target + "? " + avl.contains(target));
        }

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
