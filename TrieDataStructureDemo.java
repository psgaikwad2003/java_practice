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

    public static void main(String[] args) {
        System.out.println("Trie Data Structure Implementation");
        String[] keys = {"the", "a", "there", "answer", "any", "by", "bye", "their"};
        root = new TrieNode();
        for (String key : keys) insert(key);
        System.out.println("the --- " + search("the"));
        System.out.println("these --- " + search("these"));
        System.out.println("their --- " + search("their"));
        System.out.println("thaw --- " + search("thaw"));
    }
}
