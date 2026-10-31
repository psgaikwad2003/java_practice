import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Huffman Coding Lossless Compression Algorithm
 *
 * Constructs an optimal prefix code tree based on character frequencies
 * using a priority queue (min-heap). Provides full encoding, decoding,
 * entropy calculation, and compression efficiency statistics.
 */
public class HuffmanCoding {

    public static class HuffmanNode implements Comparable<HuffmanNode> {
        public final char ch;
        public final int frequency;
        public final HuffmanNode left;
        public final HuffmanNode right;

        // Leaf node constructor
        public HuffmanNode(char ch, int frequency) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }

        // Internal node constructor
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
            this.ch = '\0';
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    public static class CompressionResult {
        public final String originalText;
        public final String encodedBitString;
        public final HuffmanNode root;
        public final Map<Character, String> codeMap;
        public final int originalBits;
        public final int compressedBits;
        public final double compressionRatio;
        public final double spaceSavingsPercentage;
        public final double shannonEntropy;

        public CompressionResult(String originalText, String encodedBitString, HuffmanNode root,
                                 Map<Character, String> codeMap, int originalBits, int compressedBits,
                                 double shannonEntropy) {
            this.originalText = originalText;
            this.encodedBitString = encodedBitString;
            this.root = root;
            this.codeMap = codeMap;
            this.originalBits = originalBits;
            this.compressedBits = compressedBits;
            this.compressionRatio = originalBits == 0 ? 0 : (double) compressedBits / originalBits;
            this.spaceSavingsPercentage = originalBits == 0 ? 0 : (1.0 - this.compressionRatio) * 100.0;
            this.shannonEntropy = shannonEntropy;
        }
    }

    // Computes character frequencies
    public static Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        if (text == null) return freqMap;
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    // Builds Huffman Tree from frequency map using Min-PriorityQueue
    public static HuffmanNode buildTree(Map<Character, Integer> freqMap) {
        if (freqMap == null || freqMap.isEmpty()) return null;

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Special case: Only 1 unique character
        if (pq.size() == 1) {
            HuffmanNode single = pq.poll();
            return new HuffmanNode(single.frequency, single, null);
        }

        // Merge two lowest frequency trees until only root remains
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }

        return pq.poll();
    }

    // Generates prefix-free code map by traversing the Huffman Tree
    public static Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codeMap = new HashMap<>();
        if (root == null) return codeMap;

        // If tree has single unique char
        if (root.left != null && root.right == null) {
            codeMap.put(root.left.ch, "0");
            return codeMap;
        }

        buildCodesRecursive(root, "", codeMap);
        return codeMap;
    }

    private static void buildCodesRecursive(HuffmanNode node, String prefix, Map<Character, String> codeMap) {
        if (node == null) return;

        if (node.isLeaf()) {
            codeMap.put(node.ch, prefix.isEmpty() ? "0" : prefix);
            return;
        }

        buildCodesRecursive(node.left, prefix + "0", codeMap);
        buildCodesRecursive(node.right, prefix + "1", codeMap);
    }

    // Calculates theoretical Shannon Entropy of source text: H(X) = -sum(p_i * log2(p_i))
    public static double calculateShannonEntropy(String text, Map<Character, Integer> freqMap) {
        if (text == null || text.isEmpty()) return 0.0;
        int n = text.length();
        double entropy = 0.0;
        for (int freq : freqMap.values()) {
            double p = (double) freq / n;
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }

    // Encodes input text into binary string with performance metrics
    public static CompressionResult compress(String text) {
        if (text == null || text.isEmpty()) {
            return new CompressionResult("", "", null, new HashMap<>(), 0, 0, 0.0);
        }

        Map<Character, Integer> freqMap = buildFrequencyMap(text);
        HuffmanNode root = buildTree(freqMap);
        Map<Character, String> codeMap = generateCodes(root);

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(codeMap.get(c));
        }

        String bitString = encoded.toString();
        int originalBits = text.length() * 8; // Standard 8-bit ASCII / UTF-8 byte
        int compressedBits = bitString.length();
        double entropy = calculateShannonEntropy(text, freqMap);

        return new CompressionResult(text, bitString, root, codeMap, originalBits, compressedBits, entropy);
    }

    // Decodes binary bit string back to original plain text
    public static String decompress(String bitString, HuffmanNode root) {
        if (bitString == null || bitString.isEmpty() || root == null) return "";

        // Single unique char case
        if (root.left != null && root.right == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bitString.length(); i++) {
                sb.append(root.left.ch);
            }
            return sb.toString();
        }

        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;

        for (int i = 0; i < bitString.length(); i++) {
            char bit = bitString.charAt(i);
            current = (bit == '0') ? current.left : current.right;

            if (current != null && current.isLeaf()) {
                decoded.append(current.ch);
                current = root; // Reset to root for next character
            }
        }

        return decoded.toString();
    }

    // Visualizes Huffman Tree
    public static void printTree(HuffmanNode node, String prefix, boolean isTail) {
        if (node == null) return;

        String nodeDisplay;
        if (node.isLeaf()) {
            String charDisplay = (node.ch == ' ') ? "' '" : (node.ch == '\n' ? "'\\n'" : "'" + node.ch + "'");
            nodeDisplay = String.format("%s (freq: %d)", charDisplay, node.frequency);
        } else {
            nodeDisplay = String.format("[Internal] (weight: %d)", node.frequency);
        }

        System.out.println(prefix + (isTail ? "\\-- " : "+-- ") + nodeDisplay);

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTree(node.left, prefix + (isTail ? "    " : "|   "), node.right == null);
            }
            if (node.right != null) {
                printTree(node.right, prefix + (isTail ? "    " : "|   "), true);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("            HUFFMAN CODING COMPRESSION DEMO                  ");
        System.out.println("=============================================================");

        String[] testInputs = {
            "BEEP BOOP BEER!",
            "the quick brown fox jumps over the lazy dog",
            "ABRACADABRA",
            "AAAAAA"
        };

        for (int idx = 0; idx < testInputs.length; idx++) {
            String text = testInputs[idx];
            System.out.printf("\n--- Test Case %d: \"%s\" ---\n", idx + 1, text);

            CompressionResult result = compress(text);
            String decompressed = decompress(result.encodedBitString, result.root);

            System.out.println("Original text length   : " + text.length() + " chars");
            System.out.println("Original size (8-bit)  : " + result.originalBits + " bits");
            System.out.println("Compressed bitstream   : " + result.encodedBitString);
            System.out.println("Compressed size        : " + result.compressedBits + " bits");
            System.out.printf("Shannon Entropy        : %.3f bits/char\n", result.shannonEntropy);
            System.out.printf("Avg bits per char      : %.3f bits/char\n",
                    text.length() == 0 ? 0.0 : (double) result.compressedBits / text.length());
            System.out.printf("Compression Ratio      : %.2f%%\n", result.compressionRatio * 100);
            System.out.printf("Space Savings          : %.2f%%\n", result.spaceSavingsPercentage);
            System.out.println("Decompression matches  : " + text.equals(decompressed));

            System.out.println("\nGenerated Huffman Prefix Codes:");
            result.codeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    String label = (e.getKey() == ' ') ? "' '" : "'" + e.getKey() + "'";
                    System.out.printf("  %s -> %s (length: %d)\n", label, e.getValue(), e.getValue().length());
                });

            if (idx == 0) {
                System.out.println("\nHuffman Tree Visualization:");
                printTree(result.root, "", true);
            }
        }
        System.out.println("\n=============================================================");
    }
}
