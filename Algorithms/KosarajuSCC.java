import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Kosaraju's Strongly Connected Components (SCC) Algorithm
 *
 * Finds all maximal strongly connected subgraphs in a directed graph
 * using a two-pass Depth-First Search (DFS) algorithm with graph transposition.
 * Also constructs the Component Condensation DAG.
 *
 * Time Complexity: O(V + E)
 * Space Complexity: O(V + E)
 */
public class KosarajuSCC {

    public static class DirectedGraph {
        public final int vertices;
        public final List<List<Integer>> adj;

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v) {
            adj.get(u).add(v);
        }

        // Returns transposed graph with all edge directions reversed
        public DirectedGraph getTranspose() {
            DirectedGraph transposed = new DirectedGraph(vertices);
            for (int u = 0; u < vertices; u++) {
                for (int v : adj.get(u)) {
                    transposed.addEdge(v, u);
                }
            }
            return transposed;
        }
    }

    public static class SCCResult {
        public final List<List<Integer>> components;
        public final int count;
        public final int[] componentId; // Mapping from vertex -> SCC index
        public final List<Set<Integer>> condensationDAG; // Condensed graph of SCCs

        public SCCResult(List<List<Integer>> components, int[] componentId, List<Set<Integer>> condensationDAG) {
            this.components = components;
            this.count = components.size();
            this.componentId = componentId;
            this.condensationDAG = condensationDAG;
        }
    }

    // -------------------------------------------------------------
    // Kosaraju's Algorithm
    // -------------------------------------------------------------
    public static SCCResult findSCC(DirectedGraph graph) {
        if (graph == null || graph.vertices == 0) {
            return new SCCResult(Collections.emptyList(), new int[0], Collections.emptyList());
        }

        int n = graph.vertices;
        boolean[] visited = new boolean[n];
        Deque<Integer> finishOrderStack = new ArrayDeque<>();

        // Step 1: Forward DFS to order vertices by finish time
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                fillOrder(graph, i, visited, finishOrderStack);
            }
        }

        // Step 2: Get transposed graph (all edges reversed)
        DirectedGraph transposed = graph.getTranspose();

        // Step 3: DFS on transposed graph in order of finish time
        java.util.Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();
        int[] componentId = new int[n];
        int sccIndex = 0;

        while (!finishOrderStack.isEmpty()) {
            int u = finishOrderStack.pop();

            if (!visited[u]) {
                List<Integer> currentComponent = new ArrayList<>();
                dfsCollect(transposed, u, visited, currentComponent);
                for (int v : currentComponent) {
                    componentId[v] = sccIndex;
                }
                components.add(currentComponent);
                sccIndex++;
            }
        }

        // Step 4: Build Condensation DAG
        List<Set<Integer>> condensationDAG = new ArrayList<>(sccIndex);
        for (int i = 0; i < sccIndex; i++) {
            condensationDAG.add(new HashSet<>());
        }

        for (int u = 0; u < n; u++) {
            for (int v : graph.adj.get(u)) {
                int sccU = componentId[u];
                int sccV = componentId[v];
                if (sccU != sccV) {
                    condensationDAG.get(sccU).add(sccV);
                }
            }
        }

        return new SCCResult(components, componentId, condensationDAG);
    }

    // Pass 1: Post-order finish time collection
    private static void fillOrder(DirectedGraph graph, int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        for (int v : graph.adj.get(u)) {
            if (!visited[v]) {
                fillOrder(graph, v, visited, stack);
            }
        }
        stack.push(u);
    }

    // Pass 2: Component discovery in transposed graph
    private static void dfsCollect(DirectedGraph graph, int u, boolean[] visited, List<Integer> component) {
        visited[u] = true;
        component.add(u);
        for (int v : graph.adj.get(u)) {
            if (!visited[v]) {
                dfsCollect(graph, v, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("     KOSARAJU'S STRONGLY CONNECTED COMPONENTS (SCC) DEMO     ");
        System.out.println("=============================================================");

        // Graph 1: Classic 5-Vertex Digraph
        // 0 -> 2, 2 -> 1, 1 -> 0 (Cycle {0, 1, 2})
        // 0 -> 3, 3 -> 4
        System.out.println("\nGraph 1: 5-Vertex Digraph");
        DirectedGraph g1 = new DirectedGraph(5);
        g1.addEdge(1, 0);
        g1.addEdge(0, 2);
        g1.addEdge(2, 1);
        g1.addEdge(0, 3);
        g1.addEdge(3, 4);

        SCCResult res1 = findSCC(g1);
        System.out.println("Total SCCs Found: " + res1.count);
        for (int i = 0; i < res1.components.size(); i++) {
            System.out.println("  SCC " + i + ": " + res1.components.get(i));
        }

        System.out.println("\nCondensation DAG Edges (between SCCs):");
        for (int scc = 0; scc < res1.condensationDAG.size(); scc++) {
            for (int neighbor : res1.condensationDAG.get(scc)) {
                System.out.printf("  SCC %d %s -> SCC %d %s\n",
                        scc, res1.components.get(scc), neighbor, res1.components.get(neighbor));
            }
        }

        // Graph 2: 8-Vertex Digraph with Multiple Cycles
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("Graph 2: 8-Vertex Digraph (3 Complex Cycles)");
        System.out.println("-------------------------------------------------------------");
        DirectedGraph g2 = new DirectedGraph(8);
        // Cycle 1: 0 -> 1 -> 2 -> 0
        g2.addEdge(0, 1);
        g2.addEdge(1, 2);
        g2.addEdge(2, 0);

        // Cycle 2: 2 -> 3 -> 4 -> 2
        g2.addEdge(2, 3);
        g2.addEdge(3, 4);
        g2.addEdge(4, 2);

        // Connection
        g2.addEdge(4, 5);

        // Cycle 3: 5 -> 6 -> 7 -> 5
        g2.addEdge(5, 6);
        g2.addEdge(6, 7);
        g2.addEdge(7, 5);

        SCCResult res2 = findSCC(g2);
        System.out.println("Total SCCs Found: " + res2.count);
        for (int i = 0; i < res2.components.size(); i++) {
            System.out.println("  SCC " + i + ": " + res2.components.get(i));
        }

        System.out.println("\nCondensation DAG Edges:");
        for (int scc = 0; scc < res2.condensationDAG.size(); scc++) {
            for (int neighbor : res2.condensationDAG.get(scc)) {
                System.out.printf("  SCC %d %s -> SCC %d %s\n",
                        scc, res2.components.get(scc), neighbor, res2.components.get(neighbor));
            }
        }

        System.out.println("\n=============================================================");
    }
}
