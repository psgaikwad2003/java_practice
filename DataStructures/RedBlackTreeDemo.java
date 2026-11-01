import java.util.ArrayList;
import java.util.List;

/**
 * Red-Black Tree (Self-Balancing Binary Search Tree)
 *
 * Implements a balanced BST adhering to the 5 classic Red-Black invariants:
 * 1. Every node is either RED or BLACK.
 * 2. The root is always BLACK.
 * 3. All leaf nodes (sentinel NIL nodes) are BLACK.
 * 4. If a node is RED, both its children are BLACK (no two consecutive RED nodes).
 * 5. Every path from a node to descendant NIL leaves has the same number of BLACK nodes (Black Height).
 *
 * Worst-case Time Complexities:
 * - Search: O(log n)
 * - Insert: O(log n)
 * - Rotations: O(1) amortized
 */
public class RedBlackTreeDemo {

    public enum Color {
        RED, BLACK
    }

    public static class Node {
        public int key;
        public Color color;
        public Node left;
        public Node right;
        public Node parent;

        public Node(int key) {
            this.key = key;
            this.color = Color.RED;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        @Override
        public String toString() {
            return key + "(" + (color == Color.RED ? "R" : "B") + ")";
        }
    }

    public static class RedBlackTree {
        private final Node NIL;
        private Node root;
        private int size;

        public RedBlackTree() {
            NIL = new Node(0);
            NIL.color = Color.BLACK;
            NIL.left = null;
            NIL.right = null;
            root = NIL;
            size = 0;
        }

        // -------------------------------------------------------------
        // Left Rotation around node x
        // -------------------------------------------------------------
        private void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;

            if (y.left != NIL) {
                y.left.parent = x;
            }

            y.parent = x.parent;

            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }

            y.left = x;
            x.parent = y;
        }

        // -------------------------------------------------------------
        // Right Rotation around node y
        // -------------------------------------------------------------
        private void rightRotate(Node y) {
            Node x = y.left;
            y.left = x.right;

            if (x.right != NIL) {
                x.right.parent = y;
            }

            x.parent = y.parent;

            if (y.parent == null) {
                root = x;
            } else if (y == y.parent.right) {
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }

            x.right = y;
            y.parent = x;
        }

        // -------------------------------------------------------------
        // Insert Key
        // -------------------------------------------------------------
        public void insert(int key) {
            Node newNode = new Node(key);
            newNode.left = NIL;
            newNode.right = NIL;
            newNode.color = Color.RED;

            Node parent = null;
            Node current = root;

            while (current != NIL) {
                parent = current;
                if (newNode.key < current.key) {
                    current = current.left;
                } else if (newNode.key > current.key) {
                    current = current.right;
                } else {
                    return; // Duplicate keys not inserted
                }
            }

            newNode.parent = parent;

            if (parent == null) {
                root = newNode;
            } else if (newNode.key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            size++;
            fixAfterInsert(newNode);
        }

        // Fixes Red-Black violations after insertion
        private void fixAfterInsert(Node k) {
            while (k.parent != null && k.parent.color == Color.RED) {
                if (k.parent == k.parent.parent.left) {
                    Node uncle = k.parent.parent.right;

                    // Case 1: Uncle is RED -> Recolor
                    if (uncle != null && uncle.color == Color.RED) {
                        k.parent.color = Color.BLACK;
                        uncle.color = Color.BLACK;
                        k.parent.parent.color = Color.RED;
                        k = k.parent.parent;
                    } else {
                        // Case 2: Uncle is BLACK, k is right child -> Triangle configuration
                        if (k == k.parent.right) {
                            k = k.parent;
                            leftRotate(k);
                        }
                        // Case 3: Uncle is BLACK, k is left child -> Line configuration
                        k.parent.color = Color.BLACK;
                        k.parent.parent.color = Color.RED;
                        rightRotate(k.parent.parent);
                    }
                } else {
                    // Symmetric cases: parent is right child of grandparent
                    Node uncle = k.parent.parent.left;

                    // Case 1: Uncle is RED -> Recolor
                    if (uncle != null && uncle.color == Color.RED) {
                        k.parent.color = Color.BLACK;
                        uncle.color = Color.BLACK;
                        k.parent.parent.color = Color.RED;
                        k = k.parent.parent;
                    } else {
                        // Case 2: Uncle is BLACK, k is left child -> Triangle configuration
                        if (k == k.parent.left) {
                            k = k.parent;
                            rightRotate(k);
                        }
                        // Case 3: Uncle is BLACK, k is right child -> Line configuration
                        k.parent.color = Color.BLACK;
                        k.parent.parent.color = Color.RED;
                        leftRotate(k.parent.parent);
                    }
                }
            }

            root.color = Color.BLACK;
        }

        // -------------------------------------------------------------
        // Search
        // -------------------------------------------------------------
        public boolean search(int key) {
            Node current = root;
            while (current != NIL) {
                if (key == current.key) return true;
                current = (key < current.key) ? current.left : current.right;
            }
            return false;
        }

        // -------------------------------------------------------------
        // Traversal
        // -------------------------------------------------------------
        public List<Integer> inOrderTraversal() {
            List<Integer> result = new ArrayList<>();
            inOrderHelper(root, result);
            return result;
        }

        private void inOrderHelper(Node node, List<Integer> result) {
            if (node == NIL) return;
            inOrderHelper(node.left, result);
            result.add(node.key);
            inOrderHelper(node.right, result);
        }

        // -------------------------------------------------------------
        // Invariant Validation
        // Asserts that all 5 Red-Black properties hold
        // -------------------------------------------------------------
        public boolean validateRedBlackProperties() {
            if (root == NIL) return true;

            // Invariant 2: Root is BLACK
            if (root.color != Color.BLACK) {
                System.err.println("Violation: Root is not BLACK!");
                return false;
            }

            // Invariant 4 & 5: No consecutive RED nodes & Equal black-height
            int blackHeight = checkProperties(root);
            return blackHeight != -1;
        }

        private int checkProperties(Node node) {
            if (node == NIL) return 1; // NIL is BLACK

            // Invariant 4: No consecutive RED nodes
            if (node.color == Color.RED) {
                if (node.left.color == Color.RED || node.right.color == Color.RED) {
                    System.err.println("Violation: Red node " + node.key + " has a red child!");
                    return -1;
                }
            }

            int leftBlackHeight = checkProperties(node.left);
            int rightBlackHeight = checkProperties(node.right);

            if (leftBlackHeight == -1 || rightBlackHeight == -1) return -1;

            // Invariant 5: Black height must be identical across left and right subtrees
            if (leftBlackHeight != rightBlackHeight) {
                System.err.println("Violation: Black height mismatch at node " + node.key);
                return -1;
            }

            return leftBlackHeight + (node.color == Color.BLACK ? 1 : 0);
        }

        public void printTree() {
            printHelper(root, "", true);
        }

        private void printHelper(Node node, String prefix, boolean isTail) {
            if (node == NIL) return;

            System.out.println(prefix + (isTail ? "\\-- " : "+-- ") + node.key + " (" + (node.color == Color.RED ? "RED" : "BLACK") + ")");

            if (node.left != NIL || node.right != NIL) {
                if (node.left != NIL) {
                    printHelper(node.left, prefix + (isTail ? "    " : "|   "), node.right == NIL);
                }
                if (node.right != NIL) {
                    printHelper(node.right, prefix + (isTail ? "    " : "|   "), true);
                }
            }
        }

        public int size() {
            return size;
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("         RED-BLACK TREE SELF-BALANCING BST DEMO              ");
        System.out.println("=============================================================");

        RedBlackTree tree = new RedBlackTree();
        int[] keys = {10, 20, 30, 15, 25, 5, 1, 40, 50, 35};

        System.out.println("Inserting keys sequentially: " + java.util.Arrays.toString(keys));
        for (int key : keys) {
            tree.insert(key);
            boolean valid = tree.validateRedBlackProperties();
            if (!valid) {
                System.out.println("Property violation after inserting " + key);
                return;
            }
        }

        System.out.println("\nAll 5 Red-Black invariants hold: true");
        System.out.println("Tree Size: " + tree.size() + " nodes");
        System.out.println("In-Order Traversal (Sorted): " + tree.inOrderTraversal());

        System.out.println("\nRed-Black Tree Visualization:");
        tree.printTree();

        System.out.println("\nSearch Queries:");
        int[] queryKeys = {15, 25, 99, 1, 42};
        for (int q : queryKeys) {
            System.out.printf("  search(%2d) -> %b\n", q, tree.search(q));
        }

        System.out.println("\n=============================================================");
    }
}
