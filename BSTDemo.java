import java.util.LinkedList;
import java.util.Queue;


public class BSTDemo {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    private TreeNode root;

    public void insert(int val) {
        root = insertRecursive(root, val);
    }

    private TreeNode insertRecursive(TreeNode current, int val) {
        if (current == null) {
            return new TreeNode(val);
        }
        if (val < current.val) {
            current.left = insertRecursive(current.left, val);
        } else if (val > current.val) {
            current.right = insertRecursive(current.right, val);
        }
        return current;
    }

    public boolean search(int val) {
        return searchRecursive(root, val);
    }

    private boolean searchRecursive(TreeNode current, int val) {
        if (current == null) return false;
        if (current.val == val) return true;
        return val < current.val 
            ? searchRecursive(current.left, val) 
            : searchRecursive(current.right, val);
    }

    public void inOrder() {
        System.out.print("In-Order Traversal (Sorted): ");
        inOrderRecursive(root);
        System.out.println();
    }

    private void inOrderRecursive(TreeNode node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.print(node.val + " ");
            inOrderRecursive(node.right);
        }
    }

    public void levelOrder() {
        System.out.print("Level-Order Traversal (BFS): ");
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            System.out.print(curr.val + " ");
            if (curr.left != null) queue.add(curr.left);
            if (curr.right != null) queue.add(curr.right);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Binary Search Tree (BST) Operations");
        System.out.println("==================================================");

        BSTDemo bst = new BSTDemo();
        int[] values = {50, 30, 70, 20, 40, 60, 80};

        System.out.print("Inserting values: ");
        for (int v : values) {
            System.out.print(v + " ");
            bst.insert(v);
        }
        System.out.println("\n");

        bst.inOrder();
        bst.levelOrder();

        System.out.println("\nSearch 40: " + bst.search(40)); 
        System.out.println("Search 90: " + bst.search(90)); 
    }
}
// Updated for demonstration
