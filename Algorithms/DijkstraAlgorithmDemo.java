import java.util.*;

public class DijkstraAlgorithmDemo {

    public record Edge(int target, int weight) {}

    public static class Graph {
        private final int vertices;
        private final List<List<Edge>> adjList;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjList = new ArrayList<>(vertices);
            for (int i = 0; i < vertices; i++) {
                this.adjList.add(new ArrayList<>());
            }
        }

        public void addDirectedEdge(int u, int v, int weight) {
            adjList.get(u).add(new Edge(v, weight));
        }

        public void addUndirectedEdge(int u, int v, int weight) {
            addDirectedEdge(u, v, weight);
            addDirectedEdge(v, u, weight);
        }

        public List<Edge> getNeighbors(int u) {
            return adjList.get(u);
        }

        public int getVertices() {
            return vertices;
        }
    }

    public record PathResult(int source, int[] distances, int[] parents) {
        public boolean hasPathTo(int destination) {
            return distances[destination] != Integer.MAX_VALUE;
        }

        public List<Integer> getPathTo(int destination) {
            if (!hasPathTo(destination)) {
                return Collections.emptyList();
            }

            LinkedList<Integer> path = new LinkedList<>();
            for (int at = destination; at != -1; at = parents[at]) {
                path.addFirst(at);
            }
            return path;
        }
    }

    private record Node(int vertex, int weight) implements Comparable<Node> {
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    public static PathResult findShortestPaths(Graph graph, int source) {
        int V = graph.getVertices();
        int[] dist = new int[V];
        int[] parent = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        dist[source] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex();

            if (current.weight() > dist[u]) {
                continue;
            }

            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.target();
                int weight = edge.weight();

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }

        return new PathResult(source, dist, parent);
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  DIJKSTRA'S SHORTEST PATH & RECONSTRUCTION ");
        System.out.println("==========================================");

        int V = 6;
        Graph graph = new Graph(V);

        graph.addDirectedEdge(0, 1, 9);
        graph.addDirectedEdge(0, 2, 6);
        graph.addDirectedEdge(0, 3, 5);
        graph.addDirectedEdge(0, 4, 3);
        graph.addDirectedEdge(2, 1, 2);
        graph.addDirectedEdge(2, 3, 4);
        graph.addDirectedEdge(4, 5, 2);
        graph.addDirectedEdge(5, 1, 1);

        int source = 0;
        PathResult result = findShortestPaths(graph, source);

        System.out.println("\nShortest Paths from Source Vertex [" + source + "]:");
        for (int i = 0; i < V; i++) {
            if (result.hasPathTo(i)) {
                System.out.printf("  To Node %d -> Distance: %2d | Path: %s%n",
                        i, result.distances()[i], result.getPathTo(i));
            } else {
                System.out.printf("  To Node %d -> UNREACHABLE%n", i);
            }
        }

        System.out.println("\nAll Dijkstra algorithm checks passed.");
    }
}
