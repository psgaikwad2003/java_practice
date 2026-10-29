import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMST {

    public static class Edge implements Comparable<Edge> {
        public final int u;
        public final int v;
        public final int weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return "(" + u + " - " + v + " : " + weight + ")";
        }
    }

    public static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public static class MSTResult {
        public final List<Edge> mstEdges;
        public final int totalWeight;
        public final boolean isConnected;

        public MSTResult(List<Edge> mstEdges, int totalWeight, boolean isConnected) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.isConnected = isConnected;
        }
    }

    public static MSTResult findMST(int vertices, List<Edge> edges) {
        if (vertices <= 0 || edges == null) {
            return new MSTResult(Collections.emptyList(), 0, false);
        }

        List<Edge> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges);

        DSU dsu = new DSU(vertices);
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        for (Edge edge : sortedEdges) {
            if (dsu.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == vertices - 1) {
                    break;
                }
            }
        }

        boolean isConnected = (mstEdges.size() == vertices - 1) || (vertices == 1);
        return new MSTResult(mstEdges, totalWeight, isConnected);
    }

    public static void main(String[] args) {
        int vertices = 6;
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 4));
        edges.add(new Edge(1, 2, 2));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(2, 5, 2));
        edges.add(new Edge(2, 4, 4));
        edges.add(new Edge(3, 4, 3));
        edges.add(new Edge(5, 4, 3));

        System.out.println("Graph vertices: " + vertices + ", total edges: " + edges.size());
        MSTResult result = findMST(vertices, edges);

        System.out.println("Connected MST found: " + result.isConnected);
        System.out.println("Total MST Weight: " + result.totalWeight);
        System.out.println("MST Edges: " + result.mstEdges);
    }
}
