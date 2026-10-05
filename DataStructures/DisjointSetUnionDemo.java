import java.util.*;

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
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI == rootJ) {
                return false;
            }

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

    public static List<Edge> kruskalMST(int vertices, List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        Collections.sort(edges);

        DSU dsu = new DSU(vertices);

        for (Edge edge : edges) {

            if (dsu.union(edge.src, edge.dest)) {
                mst.add(edge);
                if (mst.size() == vertices - 1) {
                    break;
                }
            }
        }

        return mst;
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   DISJOINT SET UNION (DSU / UNION-FIND)  ");
        System.out.println("==========================================");

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

        System.out.println("\n[2] Undirected Graph Cycle Detection:");
        DSU cycleDetector = new DSU(4);
        int[][] edges = {
            {0, 1},
            {1, 2},
            {2, 3},
            {3, 0}
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
