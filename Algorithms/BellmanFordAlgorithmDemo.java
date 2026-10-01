package Algorithms;

import java.util.*;

/**
 * Demonstrates the Bellman-Ford Algorithm for Single-Source Shortest Paths (SSSP).
 *
 * Capabilities:
 * 1. Computes shortest path from a source vertex to all other vertices in a directed/undirected graph.
 * 2. Accommodates negative edge weights (which Dijkstra's algorithm cannot handle).
 * 3. Detects negative-weight cycles and reconstructs the cycle path.
 * 4. Practical Real-World Application: Currency Arbitrage Detection using logarithmic transformation
 *    where finding a negative cycle identifies an infinite profit opportunity:
 *    R1 * R2 * ... * Rn > 1  <=>  -ln(R1) - ln(R2) - ... - ln(Rn) < 0
 *
 * Complexity:
 * - Time: O(V * E)
 * - Space: O(V)
 */
public class BellmanFordAlgorithmDemo {

    public static class Edge {
        public final int src;
        public final int dest;
        public final double weight;

        public Edge(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return String.format("(%d -> %d, w=%.2f)", src, dest, weight);
        }
    }

    public static class BellmanFordResult {
        public final double[] dist;
        public final int[] parent;
        public final boolean hasNegativeCycle;
        public final List<Integer> negativeCycle;

        public BellmanFordResult(double[] dist, int[] parent, boolean hasNegativeCycle, List<Integer> negativeCycle) {
            this.dist = dist;
            this.parent = parent;
            this.hasNegativeCycle = hasNegativeCycle;
            this.negativeCycle = negativeCycle;
        }

        public List<Integer> reconstructPath(int target) {
            if (dist[target] == Double.POSITIVE_INFINITY) {
                return Collections.emptyList();
            }
            LinkedList<Integer> path = new LinkedList<>();
            for (int at = target; at != -1; at = parent[at]) {
                path.addFirst(at);
            }
            return path;
        }
    }

    public static class Graph {
        private final int vertices;
        private final List<Edge> edges = new ArrayList<>();

        public Graph(int vertices) {
            this.vertices = vertices;
        }

        public void addEdge(int u, int v, double weight) {
            edges.add(new Edge(u, v, weight));
        }

        public int getVertices() {
            return vertices;
        }

        public List<Edge> getEdges() {
            return Collections.unmodifiableList(edges);
        }

        /**
         * Executes the Bellman-Ford algorithm from a single source.
         */
        public BellmanFordResult solve(int source) {
            double[] dist = new double[vertices];
            int[] parent = new int[vertices];
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(parent, -1);
            dist[source] = 0.0;

            // Step 1: Relax edges up to (V - 1) times
            for (int i = 0; i < vertices - 1; i++) {
                boolean updated = false;
                for (Edge edge : edges) {
                    if (dist[edge.src] != Double.POSITIVE_INFINITY && dist[edge.src] + edge.weight < dist[edge.dest]) {
                        dist[edge.dest] = dist[edge.src] + edge.weight;
                        parent[edge.dest] = edge.src;
                        updated = true;
                    }
                }
                // Early termination optimization if graph converged
                if (!updated) break;
            }

            // Step 2: Check for negative-weight cycles (V-th iteration)
            int cycleStartNode = -1;
            for (Edge edge : edges) {
                if (dist[edge.src] != Double.POSITIVE_INFINITY && dist[edge.src] + edge.weight < dist[edge.dest]) {
                    dist[edge.dest] = Double.NEGATIVE_INFINITY;
                    parent[edge.dest] = edge.src;
                    cycleStartNode = edge.dest;
                    break;
                }
            }

            List<Integer> cycle = new ArrayList<>();
            if (cycleStartNode != -1) {
                // To guarantee we are inside the cycle, walk back V steps
                int curr = cycleStartNode;
                for (int i = 0; i < vertices; i++) {
                    curr = parent[curr];
                }

                // Trace the cycle
                int start = curr;
                cycle.add(start);
                curr = parent[start];
                while (curr != start && curr != -1 && cycle.size() <= vertices + 1) {
                    cycle.add(curr);
                    curr = parent[curr];
                }
                cycle.add(start);
                Collections.reverse(cycle);
                return new BellmanFordResult(dist, parent, true, cycle);
            }

            return new BellmanFordResult(dist, parent, false, Collections.emptyList());
        }
    }

    /**
     * Real-world financial currency arbitrage detection.
     */
    public static class CurrencyArbitrageDetector {
        private final String[] currencies;
        private final double[][] exchangeRates;

        public CurrencyArbitrageDetector(String[] currencies, double[][] exchangeRates) {
            this.currencies = currencies;
            this.exchangeRates = exchangeRates;
        }

        public void detectArbitrage() {
            int n = currencies.length;
            // Build graph where edge weight = -ln(exchangeRate)
            Graph g = new Graph(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && exchangeRates[i][j] > 0) {
                        double weight = -Math.log(exchangeRates[i][j]);
                        g.addEdge(i, j, weight);
                    }
                }
            }

            System.out.println("Analyzing currency exchange rates for arbitrage opportunities...");
            BellmanFordResult result = g.solve(0);

            if (result.hasNegativeCycle) {
                System.out.println(">>> ARBITRAGE OPPORTUNITY DETECTED! <<<");
                List<Integer> cycle = result.negativeCycle;
                double multiplier = 1.0;
                System.out.print("Arbitrage path: ");
                for (int i = 0; i < cycle.size(); i++) {
                    System.out.print(currencies[cycle.get(i)]);
                    if (i < cycle.size() - 1) {
                        System.out.print(" -> ");
                        int from = cycle.get(i);
                        int to = cycle.get(i + 1);
                        multiplier *= exchangeRates[from][to];
                    }
                }
                System.out.println();
                System.out.printf("Effective compounded return factor: %.4fx (Profit: +%.2f%%)%n",
                        multiplier, (multiplier - 1.0) * 100);
            } else {
                System.out.println("No currency arbitrage opportunity found (market is efficient).");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("       BELLMAN-FORD SSSP & NEGATIVE CYCLE DEMO               ");
        System.out.println("=============================================================");

        // Test 1: Standard Graph with Negative Weights (No Cycle)
        System.out.println("\n--- Test 1: SSSP with Negative Edge Weights ---");
        Graph g1 = new Graph(5);
        g1.addEdge(0, 1, -1);
        g1.addEdge(0, 2, 4);
        g1.addEdge(1, 2, 3);
        g1.addEdge(1, 3, 2);
        g1.addEdge(1, 4, 2);
        g1.addEdge(3, 2, 5);
        g1.addEdge(3, 1, 1);
        g1.addEdge(4, 3, -3);

        BellmanFordResult res1 = g1.solve(0);
        System.out.println("Has negative cycle: " + res1.hasNegativeCycle);
        for (int i = 0; i < g1.getVertices(); i++) {
            System.out.printf("Distance 0 -> %d: %.1f | Path: %s%n",
                    i, res1.dist[i], res1.reconstructPath(i));
        }

        // Test 2: Graph with Negative Cycle
        System.out.println("\n--- Test 2: Graph with Negative Weight Cycle ---");
        Graph g2 = new Graph(4);
        g2.addEdge(0, 1, 1);
        g2.addEdge(1, 2, -1);
        g2.addEdge(2, 3, -1);
        g2.addEdge(3, 1, -1); // 1 -> 2 -> 3 -> 1 weight = -3 (Negative Cycle)

        BellmanFordResult res2 = g2.solve(0);
        System.out.println("Has negative cycle: " + res2.hasNegativeCycle);
        if (res2.hasNegativeCycle) {
            System.out.println("Detected Negative Cycle: " + res2.negativeCycle);
        }

        // Test 3: Currency Arbitrage
        System.out.println("\n--- Test 3: Forex Arbitrage Detection Engine ---");
        String[] currencies = {"USD", "EUR", "GBP", "JPY"};
        // Exchange rate table: [from][to]
        double[][] rates = {
            // USD,   EUR,    GBP,    JPY
            { 1.00,  0.92,   0.79,  155.20 }, // USD
            { 1.09,  1.00,   0.86,  169.10 }, // EUR
            { 1.27,  1.17,   1.00,  196.40 }, // GBP
            { 0.0065, 0.0060, 0.0052, 1.00 } // JPY (slightly distorted to trigger arbitrage)
        };

        CurrencyArbitrageDetector detector = new CurrencyArbitrageDetector(currencies, rates);
        detector.detectArbitrage();

        System.out.println("\nBellman-Ford verification completed successfully.");
    }
}
