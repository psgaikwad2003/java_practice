package DataStructures;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Demonstrates a space-efficient probabilistic Bloom Filter.
 * 
 * Guarantees:
 * - No False Negatives: If mightContain returns false, the item is definitely NOT in the set.
 * - Possible False Positives: If mightContain returns true, the item MIGHT be in the set.
 * 
 * Features:
 * - Optimal bit size (m) and hash function count (k) calculation based on capacity and target error rate.
 * - Kirsch-Mitzenmacher double-hashing optimization to simulate k independent hash functions.
 * - Empirical false positive rate testing against theoretical predictions.
 */
public class BloomFilterDemo {

    private final BitSet bitSet;
    private final int bitSetSize;
    private final int numHashFunctions;
    private int insertedElements;

    /**
     * Constructs a Bloom Filter configured for expected insertions and desired false positive probability.
     *
     * @param expectedInsertions expected number of elements (n)
     * @param falsePositiveRate desired false positive rate (e.g. 0.01 for 1%)
     */
    public BloomFilterDemo(int expectedInsertions, double falsePositiveRate) {
        if (expectedInsertions <= 0 || falsePositiveRate <= 0.0 || falsePositiveRate >= 1.0) {
            throw new IllegalArgumentException("Invalid Bloom Filter parameters");
        }

        // m = - (n * ln(p)) / (ln(2)^2)
        this.bitSetSize = (int) Math.ceil(-1 * expectedInsertions * Math.log(falsePositiveRate) / (Math.log(2) * Math.log(2)));
        // k = (m / n) * ln(2)
        this.numHashFunctions = Math.max(1, (int) Math.round(((double) bitSetSize / expectedInsertions) * Math.log(2)));
        this.bitSet = new BitSet(bitSetSize);
        this.insertedElements = 0;
    }

    /**
     * Adds an element into the Bloom Filter.
     *
     * @param element the string element
     */
    public void add(String element) {
        if (element == null) return;
        int[] hashes = getHashes(element);
        for (int hash : hashes) {
            bitSet.set(hash);
        }
        insertedElements++;
    }

    /**
     * Checks if the element might be in the set.
     *
     * @param element the string element
     * @return true if the element might be present, false if it definitely is not
     */
    public boolean mightContain(String element) {
        if (element == null) return false;
        int[] hashes = getHashes(element);
        for (int hash : hashes) {
            if (!bitSet.get(hash)) {
                return false; // Definitely not present
            }
        }
        return true; // Might be present
    }

    /**
     * Kirsch-Mitzenmacher optimization: generates k hash values using two 32-bit hashes.
     */
    private int[] getHashes(String element) {
        int[] result = new int[numHashFunctions];
        int hash1 = element.hashCode();
        int hash2 = fnv1aHash(element);

        for (int i = 0; i < numHashFunctions; i++) {
            // Combined hash: (hash1 + i * hash2) % m
            int combined = (hash1 + i * hash2) % bitSetSize;
            if (combined < 0) {
                combined += bitSetSize;
            }
            result[i] = combined;
        }
        return result;
    }

    /**
     * 32-bit FNV-1a alternative hash function for hash independence.
     */
    private int fnv1aHash(String data) {
        final int FNV_PRIME = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++) {
            hash ^= data.charAt(i);
            hash *= FNV_PRIME;
        }
        return hash;
    }

    public int getBitSetSize() {
        return bitSetSize;
    }

    public int getNumHashFunctions() {
        return numHashFunctions;
    }

    public int getBitsSetCount() {
        return bitSet.cardinality();
    }

    public static void main(String[] args) {
        System.out.println("=== Bloom Filter Probabilistic Data Structure Demo ===\n");

        int expectedItems = 5000;
        double targetFPRate = 0.01; // 1%

        BloomFilterDemo bloomFilter = new BloomFilterDemo(expectedItems, targetFPRate);

        System.out.printf("Configuration:%n");
        System.out.printf("  Expected Items:      %d%n", expectedItems);
        System.out.printf("  Target FP Rate:      %.2f%%%n", targetFPRate * 100);
        System.out.printf("  Optimal Bits (m):    %d (%.2f KB)%n", bloomFilter.getBitSetSize(), bloomFilter.getBitSetSize() / 8192.0);
        System.out.printf("  Hash Functions (k):  %d%n%n", bloomFilter.getNumHashFunctions());

        // 1. Insert known items
        Set<String> groundTruth = new HashSet<>();
        for (int i = 0; i < expectedItems; i++) {
            String item = "user-" + i + "@example.com";
            groundTruth.add(item);
            bloomFilter.add(item);
        }

        System.out.printf("Inserted %d items into Bloom Filter.%n", expectedItems);
        System.out.printf("Bits set in bitset: %d / %d (%.1f%% density)%n%n",
                bloomFilter.getBitsSetCount(), bloomFilter.getBitSetSize(),
                (100.0 * bloomFilter.getBitsSetCount() / bloomFilter.getBitSetSize()));

        // 2. Test False Negatives (Must ALWAYS be 0)
        int falseNegatives = 0;
        for (String item : groundTruth) {
            if (!bloomFilter.mightContain(item)) {
                falseNegatives++;
            }
        }
        System.out.printf("Verification: False Negatives count = %d (Should strictly be 0)%n", falseNegatives);

        // 3. Test False Positives on unseen random UUIDs
        int testCount = 10000;
        int falsePositives = 0;
        for (int i = 0; i < testCount; i++) {
            String randomItem = "unknown-" + UUID.randomUUID();
            if (bloomFilter.mightContain(randomItem)) {
                falsePositives++;
            }
        }

        double empiricalFPRate = (double) falsePositives / testCount;
        System.out.printf("Empirical Test on %d unseen keys:%n", testCount);
        System.out.printf("  False Positives:     %d%n", falsePositives);
        System.out.printf("  Empirical FP Rate:   %.4f%%%n", empiricalFPRate * 100);
        System.out.printf("  Target FP Rate was:  %.4f%%%n", targetFPRate * 100);
    }
}
