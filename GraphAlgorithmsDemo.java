import java.util.*;

/**
 * GraphAlgorithmsDemo.java
 * Covers graph algorithms for interviews:
 *  1. Adjacency list representation
 *  2. BFS (Breadth-First Search)
 *  3. DFS (Depth-First Search) — iterative & recursive
 *  4. Shortest path (unweighted) using BFS
 *  5. Cycle detection in directed graph
 *  6. Topological sort (Kahn's algorithm)
 */
public class GraphAlgorithmsDemo {

    static class Graph {
        private final int vertices;
        private final Map<Integer, List<Integer>> adjList;
        private final boolean directed;

        Graph(int vertices, boolean directed) {
            this.vertices = vertices;
            this.directed = directed;
            this.adjList = new HashMap<>();
            for (int i = 0; i < vertices; i++) adjList.put(i, new ArrayList<>());
        }

        void addEdge(int from, int to) {
            adjList.get(from).add(to);
            if (!directed) adjList.get(to).add(from);
        }

        List<Integer> getNeighbors(int v) { return adjList.getOrDefault(v, Collections.emptyList()); }
        int getVertices() { return vertices; }

        void printGraph() {
            for (int v = 0; v < vertices; v++) {
                System.out.println("    " + v + " → " + adjList.get(v));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("       Graph Algorithms Deep Dive");
        System.out.println("══════════════════════════════════════════\n");

        demoBFS();
        demoDFS();
        demoShortestPath();
        demoCycleDetection();
        demoTopologicalSort();
    }

    // ── 1. BFS ───────────────────────────────────────────────
    static void demoBFS() {
        System.out.println("── 1. BFS (Breadth-First Search) ───────");
        Graph g = new Graph(6, false);
        g.addEdge(0, 1); g.addEdge(0, 2);
        g.addEdge(1, 3); g.addEdge(2, 4);
        g.addEdge(3, 5); g.addEdge(4, 5);

        System.out.println("  Graph:");
        g.printGraph();

        List<Integer> order = bfs(g, 0);
        System.out.println("  BFS from 0: " + order + "\n");
    }

    static List<Integer> bfs(Graph g, int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[g.getVertices()];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);
            for (int neighbor : g.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    // ── 2. DFS (iterative + recursive) ──────────────────────
    static void demoDFS() {
        System.out.println("── 2. DFS (Depth-First Search) ─────────");
        Graph g = new Graph(6, false);
        g.addEdge(0, 1); g.addEdge(0, 2);
        g.addEdge(1, 3); g.addEdge(2, 4);
        g.addEdge(3, 5); g.addEdge(4, 5);

        List<Integer> iterative = dfsIterative(g, 0);
        System.out.println("  DFS iterative from 0: " + iterative);

        List<Integer> recursive = new ArrayList<>();
        boolean[] visited = new boolean[g.getVertices()];
        dfsRecursive(g, 0, visited, recursive);
        System.out.println("  DFS recursive from 0: " + recursive + "\n");
    }

    static List<Integer> dfsIterative(Graph g, int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[g.getVertices()];
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);
        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (visited[current]) continue;
            visited[current] = true;
            result.add(current);
            List<Integer> neighbors = g.getNeighbors(current);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                if (!visited[neighbors.get(i)]) stack.push(neighbors.get(i));
            }
        }
        return result;
    }

    static void dfsRecursive(Graph g, int v, boolean[] visited, List<Integer> result) {
        visited[v] = true;
        result.add(v);
        for (int neighbor : g.getNeighbors(v)) {
            if (!visited[neighbor]) dfsRecursive(g, neighbor, visited, result);
        }
    }

    // ── 3. Shortest Path (unweighted BFS) ───────────────────
    static void demoShortestPath() {
        System.out.println("── 3. Shortest Path (BFS) ──────────────");
        Graph g = new Graph(7, false);
        g.addEdge(0, 1); g.addEdge(0, 2);
        g.addEdge(1, 3); g.addEdge(2, 3);
        g.addEdge(2, 4); g.addEdge(3, 5);
        g.addEdge(4, 6); g.addEdge(5, 6);

        int src = 0, dest = 6;
        List<Integer> path = shortestPath(g, src, dest);
        System.out.println("  Shortest 0→6: " + path);
        System.out.println("  Distance    : " + (path.size() - 1) + " edges\n");
    }

    static List<Integer> shortestPath(Graph g, int src, int dest) {
        boolean[] visited = new boolean[g.getVertices()];
        int[] parent = new int[g.getVertices()];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new LinkedList<>();

        visited[src] = true;
        queue.add(src);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == dest) break;
            for (int neighbor : g.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        for (int v = dest; v != -1; v = parent[v]) path.add(v);
        Collections.reverse(path);
        return path.get(0) == src ? path : Collections.emptyList();
    }

    // ── 4. Cycle Detection (directed graph) ─────────────────
    static void demoCycleDetection() {
        System.out.println("── 4. Cycle Detection (Directed) ───────");

        // Graph WITH cycle: 0→1→2→0
        Graph cyclic = new Graph(4, true);
        cyclic.addEdge(0, 1); cyclic.addEdge(1, 2);
        cyclic.addEdge(2, 0); cyclic.addEdge(2, 3);
        System.out.println("  Graph 1 has cycle? " + hasCycle(cyclic));

        // DAG (no cycle)
        Graph dag = new Graph(4, true);
        dag.addEdge(0, 1); dag.addEdge(0, 2);
        dag.addEdge(1, 3); dag.addEdge(2, 3);
        System.out.println("  Graph 2 has cycle? " + hasCycle(dag) + "\n");
    }

    static boolean hasCycle(Graph g) {
        int n = g.getVertices();
        int[] state = new int[n]; // 0=unvisited, 1=in-stack, 2=done

        for (int v = 0; v < n; v++) {
            if (state[v] == 0 && dfsCycle(g, v, state)) return true;
        }
        return false;
    }

    static boolean dfsCycle(Graph g, int v, int[] state) {
        state[v] = 1; // Mark as currently being explored
        for (int neighbor : g.getNeighbors(v)) {
            if (state[neighbor] == 1) return true;     // Back edge → cycle
            if (state[neighbor] == 0 && dfsCycle(g, neighbor, state)) return true;
        }
        state[v] = 2; // Fully explored
        return false;
    }

    // ── 5. Topological Sort (Kahn's BFS algorithm) ──────────
    static void demoTopologicalSort() {
        System.out.println("── 5. Topological Sort (Kahn's) ────────");
        // Course prerequisites: 0→2, 1→2, 2→3, 2→4, 3→5, 4→5
        Graph g = new Graph(6, true);
        g.addEdge(0, 2); g.addEdge(1, 2);
        g.addEdge(2, 3); g.addEdge(2, 4);
        g.addEdge(3, 5); g.addEdge(4, 5);

        List<Integer> sorted = topologicalSort(g);
        System.out.println("  Course order: " + sorted);
        System.out.println("  (Take 0,1 first → then 2 → 3,4 → finally 5)\n");
    }

    static List<Integer> topologicalSort(Graph g) {
        int n = g.getVertices();
        int[] inDegree = new int[n];

        // Calculate in-degrees
        for (int v = 0; v < n; v++) {
            for (int neighbor : g.getNeighbors(v)) inDegree[neighbor]++;
        }

        // Start with nodes having in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) queue.add(v);
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);
            for (int neighbor : g.getNeighbors(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) queue.add(neighbor);
            }
        }

        if (result.size() != n) throw new RuntimeException("Cycle detected — no topological order");
        return result;
    }
}
