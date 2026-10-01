package DataStructures;

import java.util.*;

/**
 * Demonstrates a Compressed Radix Tree (Patricia Trie / Compact Trie).
 *
 * Characteristics:
 * - Unlike standard Tries where each edge represents a single character, Radix Tree
 *   edges represent strings of varying lengths.
 * - Nodes with single children are compressed, drastically reducing node allocation and traversal depth.
 * - Ideal for: IP routing tables (longest prefix match), filesystem directory hierarchies,
 *   and high-throughput URL routing.
 *
 * Operations:
 * - insert(key, value): splits existing edges if a partial prefix match occurs.
 * - search(key): exact lookup in O(K) where K is key length.
 * - findLongestPrefixMatch(key): finds the most specific route/rule matching the query.
 * - delete(key): removes key and merges single-child nodes to maintain the radix property.
 */
public class RadixTreeDemo {

    public static class RadixTree<T> {
        private static class Node<T> {
            String edgeLabel;
            boolean isTerminal;
            T value;
            final Map<Character, Node<T>> children = new TreeMap<>();

            Node(String edgeLabel) {
                this.edgeLabel = edgeLabel;
            }
        }

        private final Node<T> root = new Node<>("");

        /**
         * Inserts a key-value mapping into the radix tree.
         */
        public void insert(String key, T value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            insert(root, key, value);
        }

        private void insert(Node<T> current, String remaining, T value) {
            if (remaining.isEmpty()) {
                current.isTerminal = true;
                current.value = value;
                return;
            }

            char firstChar = remaining.charAt(0);
            Node<T> child = current.children.get(firstChar);

            if (child == null) {
                // No existing edge starts with this char: create fresh child node
                Node<T> newNode = new Node<>(remaining);
                newNode.isTerminal = true;
                newNode.value = value;
                current.children.put(firstChar, newNode);
                return;
            }

            // Find longest common prefix between child's edgeLabel and remaining
            int commonLength = getCommonPrefixLength(child.edgeLabel, remaining);

            if (commonLength == child.edgeLabel.length()) {
                // Entire edge label matches: recurse further down
                insert(child, remaining.substring(commonLength), value);
            } else {
                // Partial match: split child's edge into two nodes
                String commonPrefix = child.edgeLabel.substring(0, commonLength);
                String childSuffix = child.edgeLabel.substring(commonLength);
                String remainingSuffix = remaining.substring(commonLength);

                Node<T> splitNode = new Node<>(commonPrefix);
                current.children.put(firstChar, splitNode);

                // Child keeps remainder of its label
                child.edgeLabel = childSuffix;
                splitNode.children.put(childSuffix.charAt(0), child);

                if (remainingSuffix.isEmpty()) {
                    splitNode.isTerminal = true;
                    splitNode.value = value;
                } else {
                    Node<T> newNode = new Node<>(remainingSuffix);
                    newNode.isTerminal = true;
                    newNode.value = value;
                    splitNode.children.put(remainingSuffix.charAt(0), newNode);
                }
            }
        }

        /**
         * Returns exact match value, or null if not present.
         */
        public T search(String key) {
            Node<T> current = root;
            String remaining = key;

            while (!remaining.isEmpty()) {
                char firstChar = remaining.charAt(0);
                Node<T> child = current.children.get(firstChar);
                if (child == null) return null;

                if (!remaining.startsWith(child.edgeLabel)) {
                    return null;
                }

                remaining = remaining.substring(child.edgeLabel.length());
                current = child;
            }

            return current.isTerminal ? current.value : null;
        }

        /**
         * Finds the longest registered prefix that matches the beginning of the query string.
         * Critical for IP CIDR routing tables!
         */
        public Map.Entry<String, T> findLongestPrefixMatch(String query) {
            Node<T> current = root;
            String remaining = query;
            StringBuilder matchedPrefix = new StringBuilder();
            String bestKey = null;
            T bestValue = null;

            if (root.isTerminal) {
                bestKey = "";
                bestValue = root.value;
            }

            while (!remaining.isEmpty()) {
                char firstChar = remaining.charAt(0);
                Node<T> child = current.children.get(firstChar);
                if (child == null) break;

                if (remaining.startsWith(child.edgeLabel)) {
                    matchedPrefix.append(child.edgeLabel);
                    remaining = remaining.substring(child.edgeLabel.length());
                    current = child;
                    if (current.isTerminal) {
                        bestKey = matchedPrefix.toString();
                        bestValue = current.value;
                    }
                } else {
                    break;
                }
            }

            if (bestKey == null) return null;
            return Map.entry(bestKey, bestValue);
        }

        /**
         * Deletes a key from the tree and compresses nodes if needed.
         */
        public boolean delete(String key) {
            return delete(root, key);
        }

        private boolean delete(Node<T> current, String remaining) {
            if (remaining.isEmpty()) {
                if (!current.isTerminal) return false;
                current.isTerminal = false;
                current.value = null;
                return true;
            }

            char firstChar = remaining.charAt(0);
            Node<T> child = current.children.get(firstChar);
            if (child == null || !remaining.startsWith(child.edgeLabel)) {
                return false;
            }

            boolean deleted = delete(child, remaining.substring(child.edgeLabel.length()));
            if (!deleted) return false;

            // Cleanup & Compression logic:
            // 1. If child has no children and is not terminal, remove it
            if (child.children.isEmpty() && !child.isTerminal) {
                current.children.remove(firstChar);
            }
            // 2. If child has exactly 1 child and is not terminal, merge child with its single grandchild
            else if (child.children.size() == 1 && !child.isTerminal) {
                Node<T> grandchild = child.children.values().iterator().next();
                child.edgeLabel = child.edgeLabel + grandchild.edgeLabel;
                child.isTerminal = grandchild.isTerminal;
                child.value = grandchild.value;
                child.children.clear();
                child.children.putAll(grandchild.children);
            }

            return true;
        }

        private int getCommonPrefixLength(String s1, String s2) {
            int len = Math.min(s1.length(), s2.length());
            int i = 0;
            while (i < len && s1.charAt(i) == s2.charAt(i)) {
                i++;
            }
            return i;
        }

        public void printTree() {
            printTree(root, "", true);
        }

        private void printTree(Node<T> node, String indent, boolean isLast) {
            if (node != root) {
                String marker = node.isTerminal ? " [*] (val=" + node.value + ")" : "";
                System.out.println(indent + "+-- \"" + node.edgeLabel + "\"" + marker);
                indent += isLast ? "    " : "|   ";
            }
            List<Node<T>> list = new ArrayList<>(node.children.values());
            for (int i = 0; i < list.size(); i++) {
                printTree(list.get(i), indent, i == list.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("         COMPRESSED RADIX TREE (PATRICIA TRIE) DEMO          ");
        System.out.println("=============================================================");

        RadixTree<String> trie = new RadixTree<>();
        trie.insert("romane", "I");
        trie.insert("romanus", "II");
        trie.insert("romulus", "III");
        trie.insert("rubens", "IV");
        trie.insert("ruber", "V");
        trie.insert("rubicon", "VI");
        trie.insert("rubicundus", "VII");

        System.out.println("\n--- Compressed Tree Hierarchy ---");
        trie.printTree();

        System.out.println("\n--- Exact Search Tests ---");
        System.out.println("search('romanus'): " + trie.search("romanus"));
        System.out.println("search('rubicon'): " + trie.search("rubicon"));
        System.out.println("search('roma'):    " + trie.search("roma") + " (non-terminal prefix)");
        System.out.println("search('caesar'):  " + trie.search("caesar") + " (non-existent)");

        // Longest Prefix Matching (CIDR Network Router simulation)
        System.out.println("\n--- Network CIDR Routing Table Simulation ---");
        RadixTree<String> routingTable = new RadixTree<>();
        routingTable.insert("10.", "Gateway-Default-10");
        routingTable.insert("10.0.", "VPC-Subnet-A");
        routingTable.insert("10.0.1.", "Private-Database-Cluster");
        routingTable.insert("192.168.", "LAN-Default");
        routingTable.insert("192.168.1.", "Office-WiFi");
        routingTable.insert("192.168.1.100", "Admin-Workstation");

        String[] testPackets = {
            "10.0.1.45",
            "10.0.2.12",
            "10.5.8.1",
            "192.168.1.100",
            "192.168.1.55",
            "172.16.0.1"
        };

        for (String ip : testPackets) {
            Map.Entry<String, String> route = routingTable.findLongestPrefixMatch(ip);
            if (route != null) {
                System.out.printf("IP %-15s -> Matched Rule '%s' => Route: %s%n",
                        ip, route.getKey(), route.getValue());
            } else {
                System.out.printf("IP %-15s -> NO ROUTE FOUND (Drop Packet)%n", ip);
            }
        }

        // Deletion & Node Re-compression
        System.out.println("\n--- Node Deletion & Compression Test ---");
        System.out.println("Deleting 'rubicundus' and 'rubicon'...");
        trie.delete("rubicundus");
        trie.delete("rubicon");
        trie.printTree();

        System.out.println("\nRadix Tree verification completed successfully.");
    }
}
