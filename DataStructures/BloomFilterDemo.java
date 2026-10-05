package DataStructures;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BloomFilterDemo {

    private final BitSet bitSet;
    private final int bitSetSize;
    private final int numHashFunctions;
    private int insertedElements;

    public BloomFilterDemo(int expectedInsertions, double falsePositiveRate) {
        if (expectedInsertions <= 0 || falsePositiveRate <= 0.0 || falsePositiveRate >= 1.0) {
            throw new IllegalArgumentException("Invalid Bloom Filter parameters");
        }

        this.bitSetSize = (int) Math.ceil(-1 * expectedInsertions * Math.log(falsePositiveRate) / (Math.log(2) * Math.log(2)));

        this.numHashFunctions = Math.max(1, (int) Math.round(((double) bitSetSize / expectedInsertions) * Math.log(2)));
        this.bitSet = new BitSet(bitSetSize);
        this.insertedElements = 0;
    }

    public void add(String element) {
        if (element == null) return;
        int[] hashes = getHashes(element);
        for (int hash : hashes) {
            bitSet.set(hash);
        }
        insertedElements++;
    }

    public boolean mightContain(String element) {
        if (element == null) return false;
        int[] hashes = getHashes(element);
        for (int hash : hashes) {
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    private int[] getHashes(String element) {
        int[] result = new int[numHashFunctions];
        int hash1 = element.hashCode();
        int hash2 = fnv1aHash(element);

        for (int i = 0; i < numHashFunctions; i++) {

            int combined = (hash1 + i * hash2) % bitSetSize;
            if (combined < 0) {
                combined += bitSetSize;
            }
            result[i] = combined;
        }
        return result;
    }

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

    public int getInsertedElements() {
        return insertedElements;
    }

    public double currentFalsePositiveRate() {
        if (bitSetSize == 0 || insertedElements == 0) return 0.0;
        double exponent = -1.0 * numHashFunctions * insertedElements / bitSetSize;
        return Math.pow(1.0 - Math.exp(exponent), numHashFunctions);
    }

    public void union(BloomFilterDemo other) {
        if (other == null) return;
        if (this.bitSetSize != other.bitSetSize || this.numHashFunctions != other.numHashFunctions) {
            throw new IllegalArgumentException("Cannot merge Bloom Filters with incompatible configurations.");
        }
        this.bitSet.or(other.bitSet);
        this.insertedElements += other.insertedElements;
    }

    public static void main(String[] args) {
        System.out.println("=== Bloom Filter Probabilistic Data Structure Demo ===\n");

        int expectedItems = 5000;
        double targetFPRate = 0.01;

        BloomFilterDemo bloomFilter = new BloomFilterDemo(expectedItems, targetFPRate);

        System.out.printf("Configuration:%n");
        System.out.printf("  Expected Items:      %d%n", expectedItems);
        System.out.printf("  Target FP Rate:      %.2f%%%n", targetFPRate * 100);
        System.out.printf("  Optimal Bits (m):    %d (%.2f KB)%n", bloomFilter.getBitSetSize(), bloomFilter.getBitSetSize() / 8192.0);
        System.out.printf("  Hash Functions (k):  %d%n%n", bloomFilter.getNumHashFunctions());

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

        int falseNegatives = 0;
        for (String item : groundTruth) {
            if (!bloomFilter.mightContain(item)) {
                falseNegatives++;
            }
        }
        System.out.printf("Verification: False Negatives count = %d (Should strictly be 0)%n", falseNegatives);

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
        System.out.printf("  Theoretical FP Rate: %.4f%%%n", bloomFilter.currentFalsePositiveRate() * 100);
        System.out.printf("  Target FP Rate was:  %.4f%%%n", targetFPRate * 100);

        System.out.println("\n[4] Bloom Filter Union Demo:");
        BloomFilterDemo bf1 = new BloomFilterDemo(1000, 0.01);
        BloomFilterDemo bf2 = new BloomFilterDemo(1000, 0.01);
        bf1.add("apple");
        bf1.add("banana");
        bf2.add("cherry");
        bf2.add("date");

        System.out.println("  Before merge, bf1 contains 'cherry'? " + bf1.mightContain("cherry"));
        bf1.union(bf2);
        System.out.println("  After union, bf1 contains 'cherry'? " + bf1.mightContain("cherry"));
        System.out.println("  After union, bf1 contains 'apple'?  " + bf1.mightContain("apple"));
    }
}
