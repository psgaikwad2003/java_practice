import java.util.*;

/**
 * Demonstrates Topological Sorting for Directed Acyclic Graphs (DAGs).
 *
 * Algorithms implemented:
 * 1. Kahn's Algorithm (BFS approach using in-degrees).
 * 2. DFS-based Algorithm (using recursion stack & post-order reversal).
 * 3. Cycle Detection in directed graphs.
 * 4. Real-world scenario: Build dependency resolver for software packages.
 *
 * Complexity:
 * - Time: O(V + E) where V is vertices and E is edges.
 * - Space: O(V + E) for adjacency list and recursion/queue storage.
 */
public class TopologicalSortDemo {

    /**
     * Graph representation using an adjacency list.
     */
    public static class DirectedGraph {
        private final int vertices;
        private final List<List<Integer>> adj;

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.adj = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                this.adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v) {
            adj.get(u).add(v);
        }

        public int getVertices() {
            return vertices;
        }

        public List<Integer> getNeighbors(int u) {
            return adj.get(u);
        }

        /**
         * Prints the adjacency list representation of the graph to standard output.
         * Useful for debugging and visualizing the graph structure.
         */
        public void printAdjacencyList() {
            System.out.println("Graph Adjacency List ("+vertices+" vertices):");
            for (int u = 0; u < vertices; u++) {
                System.out.print("  " + u + " -> ");
                System.out.println(adj.get(u).isEmpty() ? "(no outgoing edges)" : adj.get(u));
            }
        }
        /**
         * Kahn's Algorithm (BFS based).
         * Returns topological order, or empty list if a cycle is detected.
         *
         * @return a topologically sorted list of vertex indices, or empty list if a cycle exists
         * @implNote Time Complexity: O(V + E)
         */
        public List<Integer> topologicalSortKahn() {
            int[] inDegree = new int[vertices];
            for (int u = 0; u < vertices; u++) {
                for (int v : adj.get(u)) {
                    inDegree[v]++;
                }
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < vertices; i++) {
                if (inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            List<Integer> order = new ArrayList<>();
            while (!queue.isEmpty()) {
                int node = queue.poll();
                order.add(node);

                for (int neighbor : adj.get(node)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            // If topological sort contains all vertices, no cycle exists
            if (order.size() != vertices) {
                return Collections.emptyList(); // Cycle detected!
            }

            return order;
        }

        /**
         * DFS-based Topological Sort.
         * Returns topological order, or empty list if a cycle is detected.
         */
        public List<Integer> topologicalSortDFS() {
            // 0 = unvisited, 1 = visiting (in recursion stack), 2 = visited
            int[] state = new int[vertices];
            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i < vertices; i++) {
                if (state[i] == 0) {
                    if (hasCycleDFS(i, state, stack)) {
                        return Collections.emptyList(); // Cycle found
                    }
                }
            }

            List<Integer> result = new ArrayList<>();
            while (!stack.isEmpty()) {
                result.add(stack.pop());
            }
            return result;
        }

        private boolean hasCycleDFS(int u, int[] state, Deque<Integer> stack) {
            state[u] = 1; // Mark as visiting

            for (int v : adj.get(u)) {
                if (state[v] == 1) {
                    return true; // Back-edge found -> cycle!
                }
                if (state[v] == 0 && hasCycleDFS(v, state, stack)) {
                    return true;
                }
            }

            state[u] = 2; // Mark as visited
            stack.push(u); // Push to stack in post-order
            return false;
        }
    }

    /**
     * Real-world application: Build Dependency Resolver.
     */
    public static class BuildDependencyResolver {
        private final Map<String, List<String>> dependencies = new HashMap<>();

        public void addDependency(String task, String prerequisite) {
            dependencies.computeIfAbsent(task, k -> new ArrayList<>()).add(prerequisite);
            dependencies.putIfAbsent(prerequisite, new ArrayList<>());
        }

        public List<String> resolveBuildOrder() {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> dependents = new HashMap<>();

            for (String task : dependencies.keySet()) {
                inDegree.putIfAbsent(task, 0);
                dependents.putIfAbsent(task, new ArrayList<>());
            }

            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                String task = entry.getKey();
                for (String prereq : entry.getValue()) {
                    dependents.get(prereq).add(task);
                    inDegree.put(task, inDegree.get(task) + 1);
                }
            }

            Queue<String> readyQueue = new LinkedList<>();
            for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
                if (entry.getValue() == 0) {
                    readyQueue.offer(entry.getKey());
                }
            }

            List<String> buildOrder = new ArrayList<>();
            while (!readyQueue.isEmpty()) {
                String current = readyQueue.poll();
                buildOrder.add(current);

                for (String dependent : dependents.get(current)) {
                    inDegree.put(dependent, inDegree.get(dependent) - 1);
                    if (inDegree.get(dependent) == 0) {
                        readyQueue.offer(dependent);
                    }
                }
            }

            if (buildOrder.size() != inDegree.size()) {
                throw new IllegalStateException("Circular dependency detected in build configuration!");
            }

            return buildOrder;
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     TOPOLOGICAL SORTING DEMONSTRATION    ");
        System.out.println("==========================================");

        // DAG Graph:
        // 5 -> 2, 5 -> 0
        // 4 -> 0, 4 -> 1
        // 2 -> 3
        // 3 -> 1
        DirectedGraph dag = new DirectedGraph(6);
        dag.addEdge(5, 2);
        dag.addEdge(5, 0);
        dag.addEdge(4, 0);
        dag.addEdge(4, 1);
        dag.addEdge(2, 3);
        dag.addEdge(3, 1);

        System.out.println("\n[1] Kahn's Algorithm (BFS) on DAG:");
        List<Integer> kahnOrder = dag.topologicalSortKahn();
        System.out.println("    Order: " + kahnOrder);

        System.out.println("\n[2] DFS-based Topological Sort on DAG:");
        List<Integer> dfsOrder = dag.topologicalSortDFS();
        System.out.println("    Order: " + dfsOrder);

        // Cyclic Graph test: 0 -> 1 -> 2 -> 0
        DirectedGraph cyclicGraph = new DirectedGraph(3);
        cyclicGraph.addEdge(0, 1);
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 0);

        System.out.println("\n[3] Cycle Detection Test:");
        List<Integer> cyclicOrder = cyclicGraph.topologicalSortKahn();
        System.out.println("    Kahn Result on Cyclic Graph: " + (cyclicOrder.isEmpty() ? "Cycle Detected (No valid topological order)" : cyclicOrder));

        // Real-world application: Build Dependency Resolution
        System.out.println("\n[4] Real-World Build Task Dependency Resolution:");
        BuildDependencyResolver resolver = new BuildDependencyResolver();
        // core -> common
        // api -> core
        // service -> core, service -> database
        // ui -> service, ui -> api
        resolver.addDependency("core", "common");
        resolver.addDependency("api", "core");
        resolver.addDependency("service", "core");
        resolver.addDependency("service", "database");
        resolver.addDependency("ui", "service");
        resolver.addDependency("ui", "api");

        List<String> buildOrder = resolver.resolveBuildOrder();
        System.out.println("    Resolved Build Pipeline: " + String.join(" -> ", buildOrder));

        System.out.println("\nAll Topological Sort demonstrations completed successfully.");
    }
}
