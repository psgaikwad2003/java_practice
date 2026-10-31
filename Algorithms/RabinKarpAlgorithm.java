import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rabin-Karp String and Grid Matching Algorithm
 *
 * Employs polynomial rolling hash to search for patterns:
 * 1. Single Pattern Search with Spurious Hit Tracking - Average O(N + M), Worst O(N * M)
 * 2. Multi-Pattern Search in a Single Pass - O(N + total_pattern_lengths)
 * 3. 2D Subgrid Matrix Matching using 2D Rolling Hash - O(R * C)
 */
public class RabinKarpAlgorithm {

    // Prime base (size of extended ASCII alphabet) and large prime modulus
    private static final int BASE = 256;
    private static final long MOD = 1_000_000_007L;

    public static class MatchResult {
        public final List<Integer> matchIndices;
        public final int spuriousHits;
        public final int hashEvaluations;

        public MatchResult(List<Integer> matchIndices, int spuriousHits, int hashEvaluations) {
            this.matchIndices = matchIndices;
            this.spuriousHits = spuriousHits;
            this.hashEvaluations = hashEvaluations;
        }
    }

    // -------------------------------------------------------------
    // 1. Single Pattern Search
    // -------------------------------------------------------------
    public static MatchResult search(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        if (text == null || pattern == null) return new MatchResult(indices, 0, 0);

        int n = text.length();
        int m = pattern.length();
        if (m == 0 || n < m) return new MatchResult(indices, 0, 0);

        int spuriousHits = 0;
        int hashEvaluations = 0;

        // Compute h = (BASE^(m - 1)) % MOD
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % MOD;
        }

        // Calculate initial hash values for pattern and first window of text
        long patternHash = 0;
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + text.charAt(i)) % MOD; // Note: pattern hash on pattern chars
        }
        // Correctly compute pattern hash
        patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }

        // Slide the pattern over the text
        for (int i = 0; i <= n - m; i++) {
            hashEvaluations++;

            if (patternHash == windowHash) {
                // Verify characters to avoid collision
                boolean exactMatch = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        exactMatch = false;
                        spuriousHits++;
                        break;
                    }
                }
                if (exactMatch) {
                    indices.add(i);
                }
            }

            // Calculate rolling hash for next window
            if (i < n - m) {
                // Remove leading char and add trailing char
                windowHash = (BASE * (windowHash - text.charAt(i) * h) + text.charAt(i + m)) % MOD;
                if (windowHash < 0) {
                    windowHash += MOD;
                }
            }
        }

        return new MatchResult(indices, spuriousHits, hashEvaluations);
    }

    // -------------------------------------------------------------
    // 2. Multi-Pattern Search (Uniform Pattern Length)
    // -------------------------------------------------------------
    public static Map<String, List<Integer>> searchMultipleUniform(String text, List<String> patterns) {
        Map<String, List<Integer>> results = new HashMap<>();
        if (text == null || patterns == null || patterns.isEmpty()) return results;

        for (String p : patterns) results.put(p, new ArrayList<>());

        int m = patterns.get(0).length();
        int n = text.length();
        if (n < m) return results;

        // Map pattern hash -> set of patterns with that hash
        Map<Long, List<String>> hashToPatterns = new HashMap<>();
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % MOD;
        }

        for (String p : patterns) {
            if (p.length() != m) throw new IllegalArgumentException("All patterns must have equal length for uniform search");
            long pHash = 0;
            for (int i = 0; i < m; i++) {
                pHash = (pHash * BASE + p.charAt(i)) % MOD;
            }
            hashToPatterns.computeIfAbsent(pHash, k -> new ArrayList<>()).add(p);
        }

        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }

        for (int i = 0; i <= n - m; i++) {
            if (hashToPatterns.containsKey(windowHash)) {
                String sub = text.substring(i, i + m);
                for (String candidate : hashToPatterns.get(windowHash)) {
                    if (candidate.equals(sub)) {
                        results.get(candidate).add(i);
                    }
                }
            }

            if (i < n - m) {
                windowHash = (BASE * (windowHash - text.charAt(i) * h) + text.charAt(i + m)) % MOD;
                if (windowHash < 0) windowHash += MOD;
            }
        }

        return results;
    }

    // -------------------------------------------------------------
    // 3. 2D Subgrid Pattern Matching
    // Searches for r x c subgrid within R x C matrix
    // -------------------------------------------------------------
    public static class GridPoint {
        public final int row;
        public final int col;

        public GridPoint(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    public static List<GridPoint> search2D(char[][] grid, char[][] pattern) {
        List<GridPoint> matches = new ArrayList<>();
        if (grid == null || pattern == null) return matches;

        int R = grid.length;
        int C = grid[0].length;
        int r = pattern.length;
        int c = pattern[0].length;

        if (R < r || C < c) return matches;

        long baseCol = 31;
        long baseRow = 101;

        // Precompute powers
        long hCol = 1;
        for (int i = 0; i < c - 1; i++) hCol = (hCol * baseCol) % MOD;
        long hRow = 1;
        for (int i = 0; i < r - 1; i++) hRow = (hRow * baseRow) % MOD;

        // Compute 2D hash of pattern
        long patternHash = 0;
        for (int i = 0; i < r; i++) {
            long rowHash = 0;
            for (int j = 0; j < c; j++) {
                rowHash = (rowHash * baseCol + pattern[i][j]) % MOD;
            }
            patternHash = (patternHash * baseRow + rowHash) % MOD;
        }

        // Precompute horizontal row hashes for every r x c window across each row
        long[][] rowHashes = new long[R][C - c + 1];
        for (int i = 0; i < R; i++) {
            long rHash = 0;
            for (int j = 0; j < c; j++) {
                rHash = (rHash * baseCol + grid[i][j]) % MOD;
            }
            rowHashes[i][0] = rHash;

            for (int j = 1; j <= C - c; j++) {
                rHash = (baseCol * (rHash - grid[i][j - 1] * hCol) + grid[i][j + c - 1]) % MOD;
                if (rHash < 0) rHash += MOD;
                rowHashes[i][j] = rHash;
            }
        }

        // For each column offset, roll vertically down rows
        for (int j = 0; j <= C - c; j++) {
            long gridHash = 0;
            for (int i = 0; i < r; i++) {
                gridHash = (gridHash * baseRow + rowHashes[i][j]) % MOD;
            }

            for (int i = 0; i <= R - r; i++) {
                if (gridHash == patternHash) {
                    // Character-by-character verification
                    boolean exact = true;
                    for (int pi = 0; pi < r && exact; pi++) {
                        for (int pj = 0; pj < c; pj++) {
                            if (grid[i + pi][j + pj] != pattern[pi][pj]) {
                                exact = false;
                                break;
                            }
                        }
                    }
                    if (exact) {
                        matches.add(new GridPoint(i, j));
                    }
                }

                if (i < R - r) {
                    gridHash = (baseRow * (gridHash - rowHashes[i][j] * hRow) + rowHashes[i + r][j]) % MOD;
                    if (gridHash < 0) gridHash += MOD;
                }
            }
        }

        return matches;
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("          RABIN-KARP STRING & 2D PATTERN MATCHING            ");
        System.out.println("=============================================================");

        // 1. Single Pattern Search Demo
        System.out.println("\n1. Single Pattern Matching:");
        String text = "AABAACAADAABAABA";
        String pattern = "AABA";
        System.out.println("Text   : " + text);
        System.out.println("Pattern: " + pattern);

        MatchResult result = search(text, pattern);
        System.out.println("Match Indices     : " + result.matchIndices);
        System.out.println("Hash Evaluations  : " + result.hashEvaluations);
        System.out.println("Spurious Hits     : " + result.spuriousHits);

        // 2. Overlapping Matches
        System.out.println("\n2. Overlapping Pattern Matching:");
        String repeated = "AAAAAAA";
        String sub = "AAA";
        System.out.println("Text   : " + repeated);
        System.out.println("Pattern: " + sub);
        System.out.println("Match Indices: " + search(repeated, sub).matchIndices + " (Expected: [0, 1, 2, 3, 4])");

        // 3. Multi-Pattern Search Demo
        System.out.println("\n3. Multi-Pattern Search (Uniform Length = 4):");
        String stream = "THE CAT AND THE HAT SAT ON THE FLAT MAT";
        List<String> queryPatterns = Arrays.asList("THAT", "FLAT", "THE ", "HAT ");
        System.out.println("Text    : " + stream);
        System.out.println("Queries : " + queryPatterns);
        Map<String, List<Integer>> multiResults = searchMultipleUniform(stream, queryPatterns);
        multiResults.forEach((p, locs) -> System.out.printf("  \"%s\" found at indices: %s\n", p, locs));

        // 4. 2D Subgrid Matching Demo
        System.out.println("\n4. 2D Subgrid Matching:");
        char[][] grid = {
            {'A', 'B', 'A', 'C', 'D'},
            {'E', 'F', 'G', 'H', 'I'},
            {'A', 'B', 'A', 'C', 'D'},
            {'J', 'K', 'L', 'M', 'N'}
        };
        char[][] pat2D = {
            {'A', 'B'},
            {'E', 'F'}
        };

        System.out.println("Large Grid (4x5):");
        for (char[] row : grid) {
            System.out.println("  " + Arrays.toString(row));
        }
        System.out.println("Search Pattern (2x2):");
        for (char[] row : pat2D) {
            System.out.println("  " + Arrays.toString(row));
        }

        List<GridPoint> matches2D = search2D(grid, pat2D);
        System.out.println("Pattern found at top-left coordinates: " + matches2D);

        System.out.println("\n=============================================================");
    }
}
