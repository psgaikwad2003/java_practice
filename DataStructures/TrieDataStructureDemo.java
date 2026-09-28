public class TrieDataStructureDemo {
    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEndOfWord;
        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < 26; i++) children[i] = null;
        }
    }

    static TrieNode root;

    static void insert(String key) {
        TrieNode pCrawl = root;
        for (int i = 0; i < key.length(); i++) {
            int index = key.charAt(i) - 'a';
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new TrieNode();
            pCrawl = pCrawl.children[index];
        }
        pCrawl.isEndOfWord = true;
    }

    static boolean search(String key) {
        TrieNode pCrawl = root;
        for (int i = 0; i < key.length(); i++) {
            int index = key.charAt(i) - 'a';
            if (pCrawl.children[index] == null)
                return false;
            pCrawl = pCrawl.children[index];
        }
        return (pCrawl != null && pCrawl.isEndOfWord);
    }

    /**
     * Returns true if there is any word in the trie that starts with the given prefix.
     *
     * @param prefix the prefix to query
     * @return true if prefix exists
     */
    static boolean startsWith(String prefix) {
        if (prefix == null || root == null) return false;
        TrieNode pCrawl = root;
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            if (pCrawl.children[index] == null)
                return false;
            pCrawl = pCrawl.children[index];
        }
        return pCrawl != null;
    }

    /**
     * Deletes a key from the trie, pruning child branches that have become unused.
     *
     * @param key word to delete
     * @return true if word was present and successfully deleted
     */
    static boolean delete(String key) {
        if (key == null || root == null) return false;
        return deleteHelper(root, key, 0);
    }

    private static boolean deleteHelper(TrieNode current, String key, int depth) {
        if (current == null) return false;

        if (depth == key.length()) {
            if (!current.isEndOfWord) return false;
            current.isEndOfWord = false;
            return true;
        }

        int index = key.charAt(depth) - 'a';
        TrieNode child = current.children[index];
        if (child == null) return false;

        boolean deleted = deleteHelper(child, key, depth + 1);

        // If child node is not end-of-word and has no children, prune it
        if (deleted && !child.isEndOfWord && hasNoChildren(child)) {
            current.children[index] = null;
        }

        return deleted;
    }

    private static boolean hasNoChildren(TrieNode node) {
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) return false;
        }
        return true;
    }

    /**
     * Returns all words stored in the Trie that begin with the given prefix (Autocomplete).
     *
     * @param prefix the prefix query
     * @return list of matching complete words
     */
    static java.util.List<String> wordsWithPrefix(String prefix) {
        java.util.List<String> results = new java.util.ArrayList<>();
        if (prefix == null || root == null) return results;

        TrieNode curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            int idx = prefix.charAt(i) - 'a';
            if (curr.children[idx] == null) return results;
            curr = curr.children[idx];
        }

        collectWords(curr, new StringBuilder(prefix), results);
        return results;
    }

    private static void collectWords(TrieNode node, StringBuilder sb, java.util.List<String> results) {
        if (node == null) return;
        if (node.isEndOfWord) {
            results.add(sb.toString());
        }
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                sb.append((char) ('a' + i));
                collectWords(node.children[i], sb, results);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    /**
     * Counts the total number of words present in the Trie.
     */
    static int countWords() {
        return countWordsHelper(root);
    }

    private static int countWordsHelper(TrieNode node) {
        if (node == null) return 0;
        int count = node.isEndOfWord ? 1 : 0;
        for (int i = 0; i < 26; i++) {
            count += countWordsHelper(node.children[i]);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Trie Data Structure Implementation ===");
        String[] keys = {"the", "a", "there", "answer", "any", "by", "bye", "their"};
        root = new TrieNode();
        for (String key : keys) insert(key);

        System.out.println("Initial word count: " + countWords());

        System.out.println("\n--- Search Tests ---");
        System.out.println("the --- " + search("the"));
        System.out.println("these --- " + search("these"));
        System.out.println("their --- " + search("their"));
        System.out.println("thaw --- " + search("thaw"));

        System.out.println("\n--- Prefix Search (startsWith) ---");
        System.out.println("startsWith('th'): " + startsWith("th"));
        System.out.println("startsWith('an'): " + startsWith("an"));
        System.out.println("startsWith('xyz'): " + startsWith("xyz"));

        System.out.println("\n--- Autocomplete Suggestions ---");
        System.out.println("Prefix 'th' -> " + wordsWithPrefix("th"));
        System.out.println("Prefix 'an' -> " + wordsWithPrefix("an"));

        System.out.println("\n--- Deletion Tests ---");
        System.out.println("Deleting 'their': " + delete("their"));
        System.out.println("Search 'their' after deletion: " + search("their"));
        System.out.println("Search 'the' after deleting 'their': " + search("the"));
        System.out.println("Word count after deletion: " + countWords());
    }
}
