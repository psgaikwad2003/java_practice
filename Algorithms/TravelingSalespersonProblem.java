import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Traveling Salesperson Problem (TSP) Solvers
 *
 * Implements exact dynamic programming and heuristic methods:
 * 1. Held-Karp Algorithm (Bitmask DP) - Exact optimal O(n^2 * 2^n) with path reconstruction
 * 2. Nearest Neighbor Greedy Heuristic - O(n^2) fast tour construction
 * 3. 2-Opt Local Search Heuristic - Iterative tour improvement untangling edge crossings
 */
public class TravelingSalespersonProblem {

    public static final int INF = 1_000_000_000;

    public static class TSPResult {
        public final int minCost;
        public final List<Integer> tour;
        public final String method;

        public TSPResult(int minCost, List<Integer> tour, String method) {
            this.minCost = minCost;
            this.tour = tour;
            this.method = method;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(method).append(" -> Cost: ").append(minCost).append(" | Tour: ");
            for (int i = 0; i < tour.size(); i++) {
                sb.append(tour.get(i));
                if (i < tour.size() - 1) sb.append(" -> ");
            }
            return sb.toString();
        }
    }

    // -------------------------------------------------------------
    // 1. Held-Karp Dynamic Programming with Bitmask
    // Time Complexity: O(n^2 * 2^n), Space: O(n * 2^n)
    // -------------------------------------------------------------
    public static TSPResult solveHeldKarp(int[][] dist) {
        if (dist == null || dist.length == 0) return new TSPResult(0, Collections.emptyList(), "Held-Karp DP");

        int n = dist.length;
        if (n == 1) return new TSPResult(0, Arrays.asList(0, 0), "Held-Karp DP");

        int totalMasks = 1 << n;
        int[][] dp = new int[totalMasks][n];
        int[][] parent = new int[totalMasks][n];

        for (int[] row : dp) Arrays.fill(row, INF);
        for (int[] row : parent) Arrays.fill(row, -1);

        // Base case: starting at city 0
        dp[1][0] = 0;

        // Iterate through all bitmasks
        for (int mask = 1; mask < totalMasks; mask++) {
            // Must contain start city 0
            if ((mask & 1) == 0) continue;

            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] == INF) continue;

                // Try moving to next city v
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && dist[u][v] < INF) {
                        int nextMask = mask | (1 << v);
                        int newDist = dp[mask][u] + dist[u][v];

                        if (newDist < dp[nextMask][v]) {
                            dp[nextMask][v] = newDist;
                            parent[nextMask][v] = u;
                        }
                    }
                }
            }
        }

        // Close the tour by returning from last city to starting city 0
        int finalMask = totalMasks - 1;
        int minCost = INF;
        int lastCity = -1;

        for (int u = 1; u < n; u++) {
            if (dp[finalMask][u] < INF && dist[u][0] < INF) {
                int cost = dp[finalMask][u] + dist[u][0];
                if (cost < minCost) {
                    minCost = cost;
                    lastCity = u;
                }
            }
        }

        if (minCost >= INF) {
            return new TSPResult(-1, Collections.emptyList(), "Held-Karp DP");
        }

        // Reconstruct path backward
        List<Integer> tour = new ArrayList<>();
        tour.add(0); // Return to origin
        int currCity = lastCity;
        int currMask = finalMask;

        while (currCity != 0) {
            tour.add(currCity);
            int prevCity = parent[currMask][currCity];
            currMask ^= (1 << currCity);
            currCity = prevCity;
        }
        tour.add(0); // Origin at start

        Collections.reverse(tour);
        return new TSPResult(minCost, tour, "Held-Karp (Exact DP)");
    }

    // -------------------------------------------------------------
    // 2. Nearest Neighbor Greedy Heuristic
    // Time Complexity: O(n^2), Space: O(n)
    // -------------------------------------------------------------
    public static TSPResult solveNearestNeighbor(int[][] dist) {
        if (dist == null || dist.length == 0) return new TSPResult(0, Collections.emptyList(), "Nearest Neighbor");

        int n = dist.length;
        boolean[] visited = new boolean[n];
        List<Integer> tour = new ArrayList<>();

        int current = 0;
        tour.add(current);
        visited[current] = true;
        int totalCost = 0;

        for (int step = 1; step < n; step++) {
            int nearest = -1;
            int bestDist = INF;

            for (int next = 0; next < n; next++) {
                if (!visited[next] && dist[current][next] < bestDist) {
                    bestDist = dist[current][next];
                    nearest = next;
                }
            }

            if (nearest == -1) return new TSPResult(-1, Collections.emptyList(), "Nearest Neighbor");

            visited[nearest] = true;
            totalCost += bestDist;
            tour.add(nearest);
            current = nearest;
        }

        // Return to start
        totalCost += dist[current][0];
        tour.add(0);

        return new TSPResult(totalCost, tour, "Nearest Neighbor");
    }

    // -------------------------------------------------------------
    // 3. 2-Opt Local Search Heuristic
    // Iteratively improves tour by reversing subpaths
    // -------------------------------------------------------------
    public static TSPResult solveTwoOpt(int[][] dist, List<Integer> initialTour) {
        if (initialTour == null || initialTour.size() <= 3) {
            return new TSPResult(calculateTourCost(dist, initialTour), initialTour, "2-Opt Heuristic");
        }

        List<Integer> tour = new ArrayList<>(initialTour);
        int n = tour.size() - 1; // Exclude duplicate end city
        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 1; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int c_i_prev = tour.get(i - 1);
                    int c_i = tour.get(i);
                    int c_j = tour.get(j);
                    int c_j_next = tour.get(j + 1);

                    int currentCost = dist[c_i_prev][c_i] + dist[c_j][c_j_next];
                    int newCost = dist[c_i_prev][c_j] + dist[c_i][c_j_next];

                    if (newCost < currentCost) {
                        // Reverse segment between i and j
                        reverseSubarray(tour, i, j);
                        improved = true;
                    }
                }
            }
        }

        int finalCost = calculateTourCost(dist, tour);
        return new TSPResult(finalCost, tour, "2-Opt Improved");
    }

    private static void reverseSubarray(List<Integer> list, int i, int j) {
        while (i < j) {
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
            i++;
            j--;
        }
    }

    public static int calculateTourCost(int[][] dist, List<Integer> tour) {
        if (tour == null || tour.size() < 2) return 0;
        int cost = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            cost += dist[tour.get(i)][tour.get(i + 1)];
        }
        return cost;
    }

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("         TRAVELING SALESPERSON PROBLEM (TSP) SOLVERS         ");
        System.out.println("=============================================================");

        // Benchmark 1: Classic 4-City Asymmetric Graph
        System.out.println("\n--- Benchmark 1: 4-City Matrix ---");
        int[][] dist1 = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        TSPResult exact1 = solveHeldKarp(dist1);
        TSPResult nn1 = solveNearestNeighbor(dist1);
        TSPResult twoOpt1 = solveTwoOpt(dist1, nn1.tour);

        System.out.println(exact1);
        System.out.println(nn1);
        System.out.println(twoOpt1);
        System.out.println("Exact optimal cost verified: " + (exact1.minCost == 80));

        // Benchmark 2: 6-City Symmetric Graph with Suboptimal Greedy Choice
        System.out.println("\n--- Benchmark 2: 6-City Distance Matrix ---");
        int[][] dist2 = {
            {0, 12, 29, 22, 13, 24},
            {12, 0, 19, 11, 25, 14},
            {29, 19, 0, 17, 28, 18},
            {22, 11, 17, 0, 16, 21},
            {13, 25, 28, 16, 0, 30},
            {24, 14, 18, 21, 30, 0}
        };

        TSPResult exact2 = solveHeldKarp(dist2);
        TSPResult nn2 = solveNearestNeighbor(dist2);
        TSPResult twoOpt2 = solveTwoOpt(dist2, nn2.tour);

        System.out.println(exact2);
        System.out.println(nn2);
        System.out.println(twoOpt2);

        double savingsPercent = ((double) (nn2.minCost - twoOpt2.minCost) / nn2.minCost) * 100.0;
        System.out.printf("2-Opt Improvement over Greedy: %.2f%% cost reduction\n", savingsPercent);
        System.out.println("2-Opt reached exact optimum   : " + (twoOpt2.minCost == exact2.minCost));

        // Benchmark 3: 8-City Euclidean Coordinates
        System.out.println("\n--- Benchmark 3: 8-City Euclidean Coordinate Grid ---");
        int[][] points = {
            {0, 0}, {1, 5}, {2, 2}, {3, 9},
            {6, 8}, {7, 1}, {9, 4}, {8, 9}
        };
        int n = points.length;
        int[][] dist3 = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                dist3[i][j] = (int) Math.round(Math.hypot(dx, dy) * 10); // Scaled Euclidean distance
            }
        }

        TSPResult exact3 = solveHeldKarp(dist3);
        TSPResult nn3 = solveNearestNeighbor(dist3);
        TSPResult twoOpt3 = solveTwoOpt(dist3, nn3.tour);

        System.out.println(exact3);
        System.out.println(nn3);
        System.out.println(twoOpt3);

        System.out.println("\n=============================================================");
    }
}
