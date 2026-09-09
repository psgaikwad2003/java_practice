import java.util.*;

/**
 * Demonstrates the Disjoint Set Union (DSU / Union-Find) data structure.
 *
 * Optimizations:
 * 1. Path Compression: Flattens the tree during find operations.
 * 2. Union by Rank: Attaches smaller depth tree under the root of the deeper tree.
 *
 * Applications:
 * - Cycle detection in undirected graphs.
 * - Kruskal's Minimum Spanning Tree (MST) algorithm.
 * - Dynamic network connectivity.
 *
 * Complexity:
 * - Time: Nearly O(1) amortized, specifically O(alpha(n)) per operation (Inverse Ackermann).
 * - Space: O(n) for parent and rank arrays.
 */
public class DisjointSetUnionDemo {

    public static class DSU {
        private final int[] parent;
        private final int[] rank;
        private final int[] size;
        private int componentCount;

        public DSU(int n) {
            this.parent = new int[n];
            this.rank = new int[n];
            this.size = new int[n];
            this.componentCount = n;

            for (int i = 0; i < n; i++) {
                parent[i] = i; // Each node is its own representative
                rank[i] = 0;
                size[i] = 1;
            }
        }

        /**
         * Finds the representative root of the set containing element i.
         * Applies Path Compression.
         */
        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]); // Path compression
            }
            return parent[i];
        }

        /**
         * Merges the sets containing elements i and j.
         * Applies Union by Rank.
         * Returns true if a new merge occurred, false if already in the same set.
         */
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI == rootJ) {
                return false; // Already in the same component
            }

            // Union by Rank
            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
                rank[rootI]++;
            }

            componentCount--;
            return true;
        }

        public boolean connected(int i, int j) {
            return find(i) == find(j);
        }

        public int getComponentCount() {
            return componentCount;
        }

        public int getSizeOfComponent(int i) {
            return size[find(i)];
        }
    }

    /**
     * Graph Edge for Kruskal's MST Algorithm.
     */
    public static class Edge implements Comparable<Edge> {
        int src;
        int dest;
        int weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return String.format("(%d -- %d, weight=%d)", src, dest, weight);
        }
    }

    /**
     * Kruskal's Minimum Spanning Tree Algorithm using DSU.
     */
    public static List<Edge> kruskalMST(int vertices, List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        Collections.sort(edges); // Sort edges by ascending weight

        DSU dsu = new DSU(vertices);

        for (Edge edge : edges) {
            // If endpoints belong to different components, including edge creates no cycle
            if (dsu.union(edge.src, edge.dest)) {
                mst.add(edge);
                if (mst.size() == vertices - 1) {
                    break; // Spanning tree complete
                }
            }
        }

        return mst;
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   DISJOINT SET UNION (DSU / UNION-FIND)  ");
        System.out.println("==========================================");

        // Demo 1: Basic Union and Connectivity
        int n = 7;
        DSU dsu = new DSU(n);
        System.out.println("\n[1] Dynamic Connectivity Simulation on " + n + " Nodes:");
        System.out.println("    Initial Components: " + dsu.getComponentCount());

        dsu.union(0, 1);
        dsu.union(1, 2);
        dsu.union(3, 4);
        dsu.union(5, 6);

        System.out.println("    After unions (0-1-2), (3-4), (5-6):");
        System.out.println("    Are 0 and 2 connected? " + dsu.connected(0, 2) + " (Expected: true)");
        System.out.println("    Are 0 and 3 connected? " + dsu.connected(0, 3) + " (Expected: false)");
        System.out.println("    Component count: " + dsu.getComponentCount() + " (Expected: 3)");
        System.out.println("    Size of component containing node 0: " + dsu.getSizeOfComponent(0) + " (Expected: 3)");

        // Demo 2: Cycle Detection in Undirected Graph
        System.out.println("\n[2] Undirected Graph Cycle Detection:");
        DSU cycleDetector = new DSU(4);
        int[][] edges = {
            {0, 1},
            {1, 2},
            {2, 3},
            {3, 0} // Creates cycle!
        };

        boolean hasCycle = false;
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (!cycleDetector.union(u, v)) {
                System.out.println("    Cycle detected when attempting to add edge: " + u + " -- " + v);
                hasCycle = true;
                break;
            }
        }
        if (!hasCycle) {
            System.out.println("    No cycles detected.");
        }

        // Demo 3: Kruskal's Minimum Spanning Tree (MST)
        System.out.println("\n[3] Kruskal's Minimum Spanning Tree (MST):");
        int mstVertices = 4;
        List<Edge> graphEdges = new ArrayList<>();
        graphEdges.add(new Edge(0, 1, 10));
        graphEdges.add(new Edge(0, 2, 6));
        graphEdges.add(new Edge(0, 3, 5));
        graphEdges.add(new Edge(1, 3, 15));
        graphEdges.add(new Edge(2, 3, 4));

        List<Edge> mst = kruskalMST(mstVertices, graphEdges);
        int totalWeight = 0;
        System.out.println("    Edges in Minimum Spanning Tree:");
        for (Edge e : mst) {
            System.out.println("      " + e);
            totalWeight += e.weight;
        }
        System.out.println("    Total Minimum Cost: " + totalWeight + " (Expected: 19)");

        System.out.println("\nAll DSU demonstrations completed successfully.");
    }
}
