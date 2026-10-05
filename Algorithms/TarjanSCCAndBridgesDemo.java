package Algorithms;

import java.util.*;

public class TarjanSCCAndBridgesDemo {

    public static class DirectedGraph {
        private final int n;
        private final List<List<Integer>> adj;

        public DirectedGraph(int n) {
            this.n = n;
            adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }

        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }

        public List<List<Integer>> findSCCs() {
            int[] ids = new int[n];
            int[] low = new int[n];
            boolean[] onStack = new boolean[n];
            Deque<Integer> stack = new ArrayDeque<>();
            Arrays.fill(ids, -1);

            int[] timer = new int[]{0};
            List<List<Integer>> sccs = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (ids[i] == -1) {
                    dfsSCC(i, timer, ids, low, onStack, stack, sccs);
                }
            }
            return sccs;
        }

        private void dfsSCC(int u, int[] timer, int[] ids, int[] low, boolean[] onStack,
                            Deque<Integer> stack, List<List<Integer>> sccs) {
            ids[u] = low[u] = timer[0]++;
            stack.push(u);
            onStack[u] = true;

            for (int v : adj.get(u)) {
                if (ids[v] == -1) {
                    dfsSCC(v, timer, ids, low, onStack, stack, sccs);
                    low[u] = Math.min(low[u], low[v]);
                } else if (onStack[v]) {
                    low[u] = Math.min(low[u], ids[v]);
                }
            }

            if (ids[u] == low[u]) {
                List<Integer> scc = new ArrayList<>();
                while (true) {
                    int node = stack.pop();
                    onStack[node] = false;
                    scc.add(node);
                    if (node == u) break;
                }
                Collections.sort(scc);
                sccs.add(scc);
            }
        }
    }

    public static class NetworkGraph {
        private final int n;
        private final List<List<Integer>> adj;

        public record Bridge(int u, int v) {
            @Override
            public String toString() {
                return u + " <---> " + v;
            }
        }

        public NetworkGraph(int n) {
            this.n = n;
            adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }

        public void addEdge(int u, int v) {
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        public List<Bridge> findBridges() {
            int[] tin = new int[n];
            int[] low = new int[n];
            boolean[] visited = new boolean[n];
            int[] timer = new int[]{0};
            List<Bridge> bridges = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsBridge(i, -1, timer, tin, low, visited, bridges);
                }
            }
            return bridges;
        }

        private void dfsBridge(int u, int p, int[] timer, int[] tin, int[] low,
                               boolean[] visited, List<Bridge> bridges) {
            visited[u] = true;
            tin[u] = low[u] = timer[0]++;

            for (int v : adj.get(u)) {
                if (v == p) continue;
                if (visited[v]) {
                    low[u] = Math.min(low[u], tin[v]);
                } else {
                    dfsBridge(v, u, timer, tin, low, visited, bridges);
                    low[u] = Math.min(low[u], low[v]);
                    if (low[v] > tin[u]) {
                        bridges.add(new Bridge(Math.min(u, v), Math.max(u, v)));
                    }
                }
            }
        }

        public Set<Integer> findArticulationPoints() {
            int[] tin = new int[n];
            int[] low = new int[n];
            boolean[] visited = new boolean[n];
            int[] timer = new int[]{0};
            Set<Integer> articulationPoints = new TreeSet<>();

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsAP(i, -1, timer, tin, low, visited, articulationPoints);
                }
            }
            return articulationPoints;
        }

        private void dfsAP(int u, int p, int[] timer, int[] tin, int[] low,
                           boolean[] visited, Set<Integer> ap) {
            visited[u] = true;
            tin[u] = low[u] = timer[0]++;
            int children = 0;

            for (int v : adj.get(u)) {
                if (v == p) continue;
                if (visited[v]) {
                    low[u] = Math.min(low[u], tin[v]);
                } else {
                    dfsAP(v, u, timer, tin, low, visited, ap);
                    low[u] = Math.min(low[u], low[v]);
                    if (low[v] >= tin[u] && p != -1) {
                        ap.add(u);
                    }
                    children++;
                }
            }

            if (p == -1 && children > 1) {
                ap.add(u);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("   TARJAN'S ALGORITHMS: SCCs, BRIDGES & ARTICULATION POINTS  ");
        System.out.println("=============================================================");

        System.out.println("\n--- Part 1: Directed Graph SCC Detection ---");
        DirectedGraph dg = new DirectedGraph(8);
        dg.addEdge(0, 1);
        dg.addEdge(1, 2);
        dg.addEdge(2, 0);
        dg.addEdge(2, 3);
        dg.addEdge(3, 4);
        dg.addEdge(4, 5);
        dg.addEdge(5, 3);
        dg.addEdge(5, 6);
        dg.addEdge(6, 7);
        dg.addEdge(7, 6);

        List<List<Integer>> sccs = dg.findSCCs();
        System.out.println("Total Strongly Connected Components found: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            System.out.printf("  SCC #%d: %s%n", (i + 1), sccs.get(i));
        }

        System.out.println("\n--- Part 2: Network Infrastructure Critical Analysis ---");

        NetworkGraph network = new NetworkGraph(7);
        network.addEdge(0, 1);
        network.addEdge(1, 2);
        network.addEdge(2, 3);
        network.addEdge(3, 1);
        network.addEdge(1, 4);
        network.addEdge(4, 5);
        network.addEdge(5, 6);
        network.addEdge(6, 4);

        List<NetworkGraph.Bridge> bridges = network.findBridges();
        System.out.println("Critical Network Bridges (Severing disconnects topology):");
        for (NetworkGraph.Bridge b : bridges) {
            System.out.println("  [!] Critical Link: " + b);
        }

        Set<Integer> cutVertices = network.findArticulationPoints();
        System.out.println("Cut Vertices / Articulation Points (Single Points of Failure):");
        for (int ap : cutVertices) {
            System.out.println("  [!] SPOF Node ID: " + ap);
        }

        System.out.println("\nTarjan algorithms verification completed successfully.");
    }
}
