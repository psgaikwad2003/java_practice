package DataStructures;

import java.util.*;

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

                Node<T> newNode = new Node<>(remaining);
                newNode.isTerminal = true;
                newNode.value = value;
                current.children.put(firstChar, newNode);
                return;
            }

            int commonLength = getCommonPrefixLength(child.edgeLabel, remaining);

            if (commonLength == child.edgeLabel.length()) {

                insert(child, remaining.substring(commonLength), value);
            } else {

                String commonPrefix = child.edgeLabel.substring(0, commonLength);
                String childSuffix = child.edgeLabel.substring(commonLength);
                String remainingSuffix = remaining.substring(commonLength);

                Node<T> splitNode = new Node<>(commonPrefix);
                current.children.put(firstChar, splitNode);

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

            if (child.children.isEmpty() && !child.isTerminal) {
                current.children.remove(firstChar);
            }

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

        System.out.println("\n--- Node Deletion & Compression Test ---");
        System.out.println("Deleting 'rubicundus' and 'rubicon'...");
        trie.delete("rubicundus");
        trie.delete("rubicon");
        trie.printTree();

        System.out.println("\nRadix Tree verification completed successfully.");
    }
}
