import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Edmonds-Karp Maximum Flow & Minimum Cut Algorithm
 *
 * Implements the Ford-Fulkerson method using Breadth-First Search (BFS)
 * to find the shortest augmenting paths.
 *
 * Complexity:
 * - Time: O(V * E^2)
 * - Space: O(V + E)
 *
 * Capabilities:
 * 1. Compute Maximum Flow from Source to Sink
 * 2. Identify the Minimum Cut (S-T cut edges and partition sets)
 * 3. Solve Maximum Bipartite Matching via reduction to network flow
 */
public class MaxFlowEdmondsKarp {

    public static class Edge {
        public final int from;
        public final int to;
        public final int capacity;
        public int flow;
        public Edge reverse;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int remainingCapacity() {
            return capacity - flow;
        }

        public void augment(int bottleNeck) {
            this.flow += bottleNeck;
            this.reverse.flow -= bottleNeck;
        }

        @Override
        public String toString() {
            return String.format("%d -> %d [flow: %d/%d]", from, to, flow, capacity);
        }
    }

    public static class FlowNetwork {
        public final int numVertices;
        public final List<List<Edge>> adj;
        public final List<Edge> edges;

        public FlowNetwork(int numVertices) {
            this.numVertices = numVertices;
            this.adj = new ArrayList<>(numVertices);
            this.edges = new ArrayList<>();
            for (int i = 0; i < numVertices; i++) {
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int capacity) {
            if (capacity < 0) throw new IllegalArgumentException("Edge capacity must be non-negative");
            Edge forward = new Edge(from, to, capacity);
            Edge backward = new Edge(to, from, 0); // Residual reverse edge
            forward.reverse = backward;
            backward.reverse = forward;

            adj.get(from).add(forward);
            adj.get(to).add(backward);
            edges.add(forward);
        }
    }

    public static class MaxFlowResult {
        public final int maxFlow;
        public final List<Edge> minCutEdges;
        public final boolean[] reachableInResidual;
        public final int totalCutCapacity;

        public MaxFlowResult(int maxFlow, List<Edge> minCutEdges, boolean[] reachableInResidual, int totalCutCapacity) {
            this.maxFlow = maxFlow;
            this.minCutEdges = minCutEdges;
            this.reachableInResidual = reachableInResidual;
            this.totalCutCapacity = totalCutCapacity;
        }
    }

    // -------------------------------------------------------------
    // Edmonds-Karp Max-Flow and Min-Cut Computation
    // -------------------------------------------------------------
    public static MaxFlowResult computeMaxFlow(FlowNetwork graph, int source, int sink) {
        int n = graph.numVertices;
        int maxFlow = 0;

        while (true) {
            // BFS to find shortest augmenting path in terms of edge count
            Edge[] parentEdge = new Edge[n];
            Queue<Integer> queue = new ArrayDeque<>();
            queue.offer(source);

            while (!queue.isEmpty() && parentEdge[sink] == null) {
                int u = queue.poll();

                for (Edge edge : graph.adj.get(u)) {
                    if (edge.remainingCapacity() > 0 && parentEdge[edge.to] == null && edge.to != source) {
                        parentEdge[edge.to] = edge;
                        queue.offer(edge.to);
                    }
                }
            }

            // If sink was not reached, no augmenting path remains
            if (parentEdge[sink] == null) break;

            // Find bottleneck capacity along the path
            int bottleNeck = Integer.MAX_VALUE;
            for (Edge edge = parentEdge[sink]; edge != null; edge = parentEdge[edge.from]) {
                bottleNeck = Math.min(bottleNeck, edge.remainingCapacity());
            }

            // Augment flow along the path
            for (Edge edge = parentEdge[sink]; edge != null; edge = parentEdge[edge.from]) {
                edge.augment(bottleNeck);
            }

            maxFlow += bottleNeck;
        }

        // Min-Cut Computation via BFS in residual graph from source
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge edge : graph.adj.get(u)) {
                if (edge.remainingCapacity() > 0 && !visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.offer(edge.to);
                }
            }
        }

        // Find all original edges crossing the cut from reachable (S) to unreachable (T)
        List<Edge> minCutEdges = new ArrayList<>();
        int totalCutCapacity = 0;

        for (Edge edge : graph.edges) {
            if (visited[edge.from] && !visited[edge.to]) {
                minCutEdges.add(edge);
                totalCutCapacity += edge.capacity;
            }
        }

        return new MaxFlowResult(maxFlow, minCutEdges, visited, totalCutCapacity);
    }

    // -------------------------------------------------------------
    // Maximum Bipartite Matching Reduction
    // -------------------------------------------------------------
    public static class MatchingResult {
        public final int maxMatches;
        public final List<String> matchedPairs;

        public MatchingResult(int maxMatches, List<String> matchedPairs) {
            this.maxMatches = maxMatches;
            this.matchedPairs = matchedPairs;
        }
    }

    /**
     * Solves Maximum Bipartite Matching:
     * @param applicants Array of applicant names (Set U)
     * @param jobs Array of job names (Set V)
     * @param compatibility Matrix where compatibility[i][j] == true if applicant i is qualified for job j
     */
    public static MatchingResult solveBipartiteMatching(String[] applicants, String[] jobs, boolean[][] compatibility) {
        int uCount = applicants.length;
        int vCount = jobs.length;
        int totalNodes = uCount + vCount + 2;

        int source = 0;
        int sink = totalNodes - 1;

        FlowNetwork network = new FlowNetwork(totalNodes);

        // Connect source to each applicant (capacity 1)
        for (int i = 0; i < uCount; i++) {
            network.addEdge(source, 1 + i, 1);
        }

        // Connect applicant to jobs (capacity 1)
        for (int i = 0; i < uCount; i++) {
            for (int j = 0; j < vCount; j++) {
                if (compatibility[i][j]) {
                    network.addEdge(1 + i, 1 + uCount + j, 1);
                }
            }
        }

        // Connect each job to sink (capacity 1)
        for (int j = 0; j < vCount; j++) {
            network.addEdge(1 + uCount + j, sink, 1);
        }

        MaxFlowResult flowRes = computeMaxFlow(network, source, sink);

        List<String> matches = new ArrayList<>();
        for (Edge edge : network.edges) {
            if (edge.from >= 1 && edge.from <= uCount &&
                edge.to >= 1 + uCount && edge.to < sink && edge.flow == 1) {
                int applicantIdx = edge.from - 1;
                int jobIdx = edge.to - 1 - uCount;
                matches.add(applicants[applicantIdx] + " -> " + jobs[jobIdx]);
            }
        }

        return new MatchingResult(flowRes.maxFlow, matches);
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("       EDMONDS-KARP MAXIMUM FLOW & MIN-CUT ALGORITHM         ");
        System.out.println("=============================================================");

        // Demo 1: Classic Flow Network
        // Vertices: 0=Source (s), 1=v1, 2=v2, 3=v3, 4=v4, 5=Sink (t)
        System.out.println("\nDemo 1: Classic Network Flow (6 Vertices)");
        FlowNetwork network = new FlowNetwork(6);
        network.addEdge(0, 1, 16);
        network.addEdge(0, 2, 13);
        network.addEdge(1, 2, 10);
        network.addEdge(1, 3, 12);
        network.addEdge(2, 1, 4);
        network.addEdge(2, 4, 14);
        network.addEdge(3, 2, 9);
        network.addEdge(3, 5, 20);
        network.addEdge(4, 3, 7);
        network.addEdge(4, 5, 4);

        int source = 0;
        int sink = 5;

        MaxFlowResult result = computeMaxFlow(network, source, sink);
        System.out.println("Computed Maximum Flow : " + result.maxFlow + " (Expected: 23)");
        System.out.println("Total Cut Capacity    : " + result.totalCutCapacity);
        System.out.println("Max-Flow == Min-Cut   : " + (result.maxFlow == result.totalCutCapacity));

        System.out.println("\nEdges carrying flow:");
        for (Edge edge : network.edges) {
            if (edge.flow > 0) {
                System.out.println("  " + edge);
            }
        }

        System.out.println("\nBottleneck Min-Cut Edges (S -> T):");
        for (Edge cutEdge : result.minCutEdges) {
            System.out.printf("  (%d -> %d) with capacity %d\n", cutEdge.from, cutEdge.to, cutEdge.capacity);
        }

        System.out.print("Partition Set S (Reachable): { ");
        for (int i = 0; i < network.numVertices; i++) {
            if (result.reachableInResidual[i]) System.out.print(i + " ");
        }
        System.out.print("}\nPartition Set T (Unreachable): { ");
        for (int i = 0; i < network.numVertices; i++) {
            if (!result.reachableInResidual[i]) System.out.print(i + " ");
        }
        System.out.println("}");

        // Demo 2: Maximum Bipartite Matching Reduction
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("Demo 2: Maximum Bipartite Matching (Job Assignment)");
        System.out.println("-------------------------------------------------------------");

        String[] applicants = {"Alice", "Bob", "Charlie", "Diana"};
        String[] jobs = {"Software Dev", "Data Science", "DevOps", "Cybersecurity"};

        // Qualification matrix
        // Alice: Software Dev, Data Science
        // Bob: Software Dev
        // Charlie: DevOps, Cybersecurity
        // Diana: Data Science, DevOps
        boolean[][] qualifications = {
            {true,  true,  false, false}, // Alice
            {true,  false, false, false}, // Bob
            {false, false, true,  true }, // Charlie
            {false, true,  true,  false}  // Diana
        };

        MatchingResult matchingResult = solveBipartiteMatching(applicants, jobs, qualifications);
        System.out.println("Maximum Matches Achieved: " + matchingResult.maxMatches + " / " + applicants.length);
        System.out.println("Assignments:");
        for (String pair : matchingResult.matchedPairs) {
            System.out.println("  * " + pair);
        }

        System.out.println("\n=============================================================");
    }
}
