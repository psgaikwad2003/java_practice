import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FloydWarshallAlgorithm {

    public static final int INF = 1_000_000_000;

    public static class AllPairsResult {
        public final int[][] distances;
        public final int[][] next;
        public final boolean hasNegativeCycle;

        public AllPairsResult(int[][] distances, int[][] next, boolean hasNegativeCycle) {
            this.distances = distances;
            this.next = next;
            this.hasNegativeCycle = hasNegativeCycle;
        }

        public List<Integer> reconstructPath(int u, int v) {
            if (hasNegativeCycle || distances[u][v] >= INF) {
                return Collections.emptyList();
            }

            List<Integer> path = new ArrayList<>();
            path.add(u);
            int curr = u;
            while (curr != v) {
                curr = next[curr][v];
                if (curr == -1) return Collections.emptyList();
                path.add(curr);
            }
            return path;
        }
    }

    public static AllPairsResult computeAllPairsShortestPaths(int vertices, int[][] graph) {
        int[][] dist = new int[vertices][vertices];
        int[][] next = new int[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
                if (i != j && graph[i][j] < INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] < INF && dist[k][j] < INF) {
                        int newDist = dist[i][k] + dist[k][j];
                        if (newDist < dist[i][j]) {
                            dist[i][j] = newDist;
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }

        boolean hasNegativeCycle = false;
        for (int i = 0; i < vertices; i++) {
            if (dist[i][i] < 0) {
                hasNegativeCycle = true;
                break;
            }
        }

        return new AllPairsResult(dist, next, hasNegativeCycle);
    }

    public static void main(String[] args) {
        int V = 4;
        int[][] matrix = {
            {0, 5, INF, 10},
            {INF, 0, 3, INF},
            {INF, INF, 0, 1},
            {INF, INF, INF, 0}
        };

        AllPairsResult result = computeAllPairsShortestPaths(V, matrix);

        System.out.println("Has Negative Cycle: " + result.hasNegativeCycle);
        System.out.println("\nAll-Pairs Shortest Distances Matrix:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (result.distances[i][j] >= INF) {
                    System.out.printf("%6s", "INF");
                } else {
                    System.out.printf("%6d", result.distances[i][j]);
                }
            }
            System.out.println();
        }

        System.out.println("\nReconstructed Paths:");
        System.out.println("Path from 0 to 3: " + result.reconstructPath(0, 3) + " (Cost: " + result.distances[0][3] + ")");
        System.out.println("Path from 0 to 2: " + result.reconstructPath(0, 2) + " (Cost: " + result.distances[0][2] + ")");
        System.out.println("Path from 1 to 3: " + result.reconstructPath(1, 3) + " (Cost: " + result.distances[1][3] + ")");
    }
}
