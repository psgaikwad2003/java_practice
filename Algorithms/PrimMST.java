import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Prim's Minimum Spanning Tree (MST) Algorithm
 *
 * Implements two variations:
 * 1. Adjacency List + Min-PriorityQueue for sparse graphs: O((V + E) log V)
 * 2. Adjacency Matrix implementation for dense graphs: O(V^2)
 *
 * Supports connected graphs and Minimum Spanning Forests for disconnected components.
 */
public class PrimMST {

    public static class Edge {
        public final int u;
        public final int v;
        public final int weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return String.format("(%d - %d : %d)", u, v, weight);
        }
    }

    private static class NodeEntry implements Comparable<NodeEntry> {
        final int vertex;
        final int key; // Minimum weight to attach to current MST

        NodeEntry(int vertex, int key) {
            this.vertex = vertex;
            this.key = key;
        }

        @Override
        public int compareTo(NodeEntry other) {
            return Integer.compare(this.key, other.key);
        }
    }

    public static class MSTResult {
        public final List<Edge> mstEdges;
        public final int totalWeight;
        public final boolean isConnected;
        public final int connectedComponents;

        public MSTResult(List<Edge> mstEdges, int totalWeight, boolean isConnected, int connectedComponents) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.isConnected = isConnected;
            this.connectedComponents = connectedComponents;
        }
    }

    // -------------------------------------------------------------
    // 1. Sparse Graph Prim's Algorithm (Adjacency List + Min-Heap)
    // Time Complexity: O((V + E) log V), Space: O(V + E)
    // -------------------------------------------------------------
    public static MSTResult findMSTSparse(int vertices, List<List<Edge>> adj) {
        if (vertices <= 0 || adj == null) {
            return new MSTResult(new ArrayList<>(), 0, false, 0);
        }

        boolean[] inMST = new boolean[vertices];
        int[] key = new int[vertices];
        int[] parent = new int[vertices];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;
        int components = 0;

        // Loop over all vertices to support disconnected components (Spanning Forest)
        for (int start = 0; start < vertices; start++) {
            if (inMST[start]) continue;

            components++;
            key[start] = 0;
            pq.offer(new NodeEntry(start, 0));

            while (!pq.isEmpty()) {
                NodeEntry entry = pq.poll();
                int u = entry.vertex;

                if (inMST[u]) continue; // Stale heap entry
                inMST[u] = true;

                if (parent[u] != -1) {
                    mstEdges.add(new Edge(parent[u], u, key[u]));
                    totalWeight += key[u];
                }

                for (Edge edge : adj.get(u)) {
                    int v = (edge.u == u) ? edge.v : edge.u;
                    int weight = edge.weight;

                    if (!inMST[v] && weight < key[v]) {
                        key[v] = weight;
                        parent[v] = u;
                        pq.offer(new NodeEntry(v, key[v]));
                    }
                }
            }
        }

        boolean isConnected = (components == 1) && (mstEdges.size() == vertices - 1 || vertices == 1);
        return new MSTResult(mstEdges, totalWeight, isConnected, components);
    }

    // -------------------------------------------------------------
    // 2. Dense Graph Prim's Algorithm (Adjacency Matrix)
    // Time Complexity: O(V^2), Space: O(V)
    // -------------------------------------------------------------
    public static MSTResult findMSTDense(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return new MSTResult(new ArrayList<>(), 0, false, 0);
        }

        int vertices = matrix.length;
        boolean[] inMST = new boolean[vertices];
        int[] key = new int[vertices];
        int[] parent = new int[vertices];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;
        int components = 0;

        for (int start = 0; start < vertices; start++) {
            if (inMST[start]) continue;
            components++;
            key[start] = 0;

            for (int step = 0; step < vertices; step++) {
                int minKey = Integer.MAX_VALUE;
                int u = -1;

                // Pick minimum key vertex not yet in MST
                for (int v = 0; v < vertices; v++) {
                    if (!inMST[v] && key[v] < minKey) {
                        minKey = key[v];
                        u = v;
                    }
                }

                if (u == -1) break; // Finished current connected component

                inMST[u] = true;
                if (parent[u] != -1) {
                    mstEdges.add(new Edge(parent[u], u, key[u]));
                    totalWeight += key[u];
                }

                // Update neighbor keys
                for (int v = 0; v < vertices; v++) {
                    int weight = matrix[u][v];
                    if (weight != 0 && !inMST[v] && weight < key[v]) {
                        key[v] = weight;
                        parent[v] = u;
                    }
                }
            }
        }

        boolean isConnected = (components == 1) && (mstEdges.size() == vertices - 1 || vertices == 1);
        return new MSTResult(mstEdges, totalWeight, isConnected, components);
    }

    // Helper to build undirected adjacency list
    public static List<List<Edge>> createGraph(int vertices) {
        List<List<Edge>> adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());
        return adj;
    }

    public static void addUndirectedEdge(List<List<Edge>> adj, int u, int v, int weight) {
        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        adj.get(v).add(edge);
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("           PRIM'S MINIMUM SPANNING TREE (MST)                ");
        System.out.println("=============================================================");

        // Benchmark 1: Exact matching graph from KruskalMST.java (6 Vertices)
        System.out.println("\nBenchmark 1: 6-Vertex Graph (Matches Kruskal's Benchmark)");
        int vertices1 = 6;
        List<List<Edge>> adj1 = createGraph(vertices1);
        addUndirectedEdge(adj1, 0, 1, 4);
        addUndirectedEdge(adj1, 0, 2, 4);
        addUndirectedEdge(adj1, 1, 2, 2);
        addUndirectedEdge(adj1, 2, 3, 3);
        addUndirectedEdge(adj1, 2, 5, 2);
        addUndirectedEdge(adj1, 2, 4, 4);
        addUndirectedEdge(adj1, 3, 4, 3);
        addUndirectedEdge(adj1, 5, 4, 3);

        MSTResult res1 = findMSTSparse(vertices1, adj1);
        System.out.println("Connected MST Found   : " + res1.isConnected);
        System.out.println("Total MST Weight      : " + res1.totalWeight + " (Expected: 14)");
        System.out.println("MST Edges Selected    : " + res1.mstEdges);

        // Benchmark 2: Dense Graph Adjacency Matrix (5 Vertices)
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("Benchmark 2: Dense Graph (Adjacency Matrix O(V^2))");
        System.out.println("-------------------------------------------------------------");
        int[][] denseMatrix = {
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {6, 8, 0, 0, 9},
            {0, 5, 7, 9, 0}
        };

        MSTResult res2 = findMSTDense(denseMatrix);
        System.out.println("Connected MST Found   : " + res2.isConnected);
        System.out.println("Total MST Weight      : " + res2.totalWeight + " (Expected: 16)");
        System.out.println("MST Edges Selected    : " + res2.mstEdges);

        // Benchmark 3: Disconnected Graph (Minimum Spanning Forest)
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("Benchmark 3: Disconnected Graph (Spanning Forest)");
        System.out.println("-------------------------------------------------------------");
        int vertices3 = 5;
        List<List<Edge>> adj3 = createGraph(vertices3);
        // Component 1: 0 - 1
        addUndirectedEdge(adj3, 0, 1, 10);
        // Component 2: 2 - 3, 3 - 4
        addUndirectedEdge(adj3, 2, 3, 5);
        addUndirectedEdge(adj3, 3, 4, 7);

        MSTResult res3 = findMSTSparse(vertices3, adj3);
        System.out.println("Is Connected Graph    : " + res3.isConnected + " (Expected: false)");
        System.out.println("Connected Components  : " + res3.connectedComponents + " (Expected: 2)");
        System.out.println("Total Forest Weight   : " + res3.totalWeight + " (Expected: 22)");
        System.out.println("Forest Edges          : " + res3.mstEdges);

        System.out.println("\n=============================================================");
    }
}
