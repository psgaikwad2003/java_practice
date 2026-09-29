package Algorithms;

import java.util.*;

/**
 * Demonstrates the A* (A-Star) Pathfinding Algorithm on 2D Grids.
 *
 * A* combines the strengths of Dijkstra's algorithm (guaranteeing the shortest path)
 * and Greedy Best-First Search (using heuristics to guide search towards the goal).
 *
 * Evaluation Function:
 *   f(n) = g(n) + h(n)
 * where:
 *   - g(n) is the exact cost from the start node to current node n.
 *   - h(n) is the estimated (heuristic) cost from n to the goal.
 *   - f(n) is the estimated total path cost through node n.
 *
 * Supported Heuristics:
 * 1. Manhattan Distance  : Optimal for 4-directional grid movement.
 * 2. Euclidean Distance  : Direct straight-line distance.
 * 3. Octile / Diagonal   : Optimal for 8-directional grid movement.
 */
public class AStarPathfindingDemo {

    public enum HeuristicType {
        MANHATTAN,
        EUCLIDEAN,
        OCTILE
    }

    public static class Point {
        public final int r;
        public final int c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return r == p.r && c == p.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }

        @Override
        public String toString() {
            return "(" + r + ", " + c + ")";
        }
    }

    public static class Node implements Comparable<Node> {
        public final int r;
        public final int c;
        public double gCost; // Cost from start
        public double hCost; // Heuristic estimate to target
        public double fCost; // g + h
        public Node parent;

        public Node(int r, int c, double gCost, double hCost, Node parent) {
            this.r = r;
            this.c = c;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            int cmp = Double.compare(this.fCost, other.fCost);
            if (cmp == 0) {
                // Tie-breaker: pick node closer to goal (lower hCost)
                return Double.compare(this.hCost, other.hCost);
            }
            return cmp;
        }
    }

    public static class PathResult {
        public final List<Point> path;
        public final double pathCost;
        public final int nodesExplored;
        public final boolean found;
        public final boolean[][] closedSet;

        public PathResult(List<Point> path, double pathCost, int nodesExplored, boolean found, boolean[][] closedSet) {
            this.path = path;
            this.pathCost = pathCost;
            this.nodesExplored = nodesExplored;
            this.found = found;
            this.closedSet = closedSet;
        }
    }

    public static class AStarSolver {
        private final int[][] grid; // 0 = walkable, 1 = obstacle
        private final int rows;
        private final int cols;

        // 4-directional offsets (orthogonals)
        private static final int[][] DIRS_4 = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        // 8-directional offsets (orthogonals + diagonals)
        private static final int[][] DIRS_8 = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        public AStarSolver(int[][] grid) {
            this.grid = grid;
            this.rows = grid.length;
            this.cols = grid[0].length;
        }

        public PathResult findPath(Point start, Point target, HeuristicType heuristicType, boolean allowDiagonals) {
            if (!isValid(start.r, start.c) || !isValid(target.r, target.c)) {
                return new PathResult(Collections.emptyList(), 0, 0, false, new boolean[rows][cols]);
            }

            PriorityQueue<Node> openSet = new PriorityQueue<>();
            boolean[][] closedSet = new boolean[rows][cols];
            double[][] bestG = new double[rows][cols];

            for (int r = 0; r < rows; r++) {
                Arrays.fill(bestG[r], Double.POSITIVE_INFINITY);
            }

            double initialH = calculateHeuristic(start.r, start.c, target.r, target.c, heuristicType);
            Node startNode = new Node(start.r, start.c, 0.0, initialH, null);
            openSet.add(startNode);
            bestG[start.r][start.c] = 0.0;

            int nodesExplored = 0;
            Node targetNode = null;
            int[][] directions = allowDiagonals ? DIRS_8 : DIRS_4;

            while (!openSet.isEmpty()) {
                Node current = openSet.poll();

                if (closedSet[current.r][current.c]) {
                    continue;
                }
                closedSet[current.r][current.c] = true;
                nodesExplored++;

                // Target reached?
                if (current.r == target.r && current.c == target.c) {
                    targetNode = current;
                    break;
                }

                // Explore neighbors
                for (int[] dir : directions) {
                    int nr = current.r + dir[0];
                    int nc = current.c + dir[1];

                    if (!isValid(nr, nc) || closedSet[nr][nc]) {
                        continue;
                    }

                    // Cost: 1.0 for orthogonal step, sqrt(2) ~ 1.414 for diagonal step
                    boolean isDiagonal = (dir[0] != 0 && dir[1] != 0);
                    double stepCost = isDiagonal ? 1.4142 : 1.0;
                    double tentativeG = current.gCost + stepCost;

                    if (tentativeG < bestG[nr][nc]) {
                        bestG[nr][nc] = tentativeG;
                        double h = calculateHeuristic(nr, nc, target.r, target.c, heuristicType);
                        openSet.add(new Node(nr, nc, tentativeG, h, current));
                    }
                }
            }

            if (targetNode == null) {
                return new PathResult(Collections.emptyList(), 0, nodesExplored, false, closedSet);
            }

            // Reconstruct path backwards from target
            List<Point> path = new ArrayList<>();
            Node curr = targetNode;
            while (curr != null) {
                path.add(new Point(curr.r, curr.c));
                curr = curr.parent;
            }
            Collections.reverse(path);

            return new PathResult(path, targetNode.gCost, nodesExplored, true, closedSet);
        }

        private boolean isValid(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == 0;
        }

        private double calculateHeuristic(int r1, int c1, int r2, int c2, HeuristicType type) {
            int dr = Math.abs(r1 - r2);
            int dc = Math.abs(c1 - c2);

            switch (type) {
                case MANHATTAN:
                    // D * (dx + dy)
                    return dr + dc;
                case EUCLIDEAN:
                    // sqrt(dx^2 + dy^2)
                    return Math.hypot(dr, dc);
                case OCTILE:
                    // D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
                    // D = 1.0, D2 = 1.4142
                    return (dr + dc) + (1.4142 - 2.0) * Math.min(dr, dc);
                default:
                    return dr + dc;
            }
        }

        /**
         * Renders the grid with start (S), goal (G), walls (#), path (*), and explored nodes (.).
         */
        public void renderGrid(Point start, Point target, PathResult result) {
            Set<Point> pathSet = new HashSet<>(result.path);

            System.out.println("+" + "--".repeat(cols) + "-+");
            for (int r = 0; r < rows; r++) {
                StringBuilder rowStr = new StringBuilder("| ");
                for (int c = 0; c < cols; c++) {
                    Point pt = new Point(r, c);
                    if (pt.equals(start)) {
                        rowStr.append("S ");
                    } else if (pt.equals(target)) {
                        rowStr.append("G ");
                    } else if (pathSet.contains(pt)) {
                        rowStr.append("* ");
                    } else if (grid[r][c] == 1) {
                        rowStr.append("# ");
                    } else if (result.closedSet != null && result.closedSet[r][c]) {
                        rowStr.append(". ");
                    } else {
                        rowStr.append("  ");
                    }
                }
                rowStr.append("|");
                System.out.println(rowStr);
            }
            System.out.println("+" + "--".repeat(cols) + "-+");
            System.out.println("Legend: S=Start, G=Goal, #=Obstacle, *=Path, .=Explored, ' '=Unvisited\n");
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("      A* (A-Star) Pathfinding Algorithm Demonstration    ");
        System.out.println("==========================================================\n");

        // 0 = Free cell, 1 = Wall / Obstacle
        int[][] mazeGrid = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0}
        };

        AStarSolver solver = new AStarSolver(mazeGrid);
        Point start = new Point(0, 0);
        Point goal = new Point(8, 11);

        // Scenario 1: 4-Directional Movement with Manhattan Heuristic
        System.out.println("Scenario 1: 4-Directional Movement (Manhattan Heuristic)");
        System.out.println("Start: " + start + " -> Goal: " + goal);
        PathResult res4 = solver.findPath(start, goal, HeuristicType.MANHATTAN, false);
        System.out.printf("Path Found: %b | Total Cost: %.2f | Path Steps: %d | Nodes Explored: %d%n",
                res4.found, res4.pathCost, res4.path.size(), res4.nodesExplored);
        solver.renderGrid(start, goal, res4);

        // Scenario 2: 8-Directional Movement with Octile Heuristic
        System.out.println("Scenario 2: 8-Directional Movement (Octile Heuristic)");
        PathResult res8 = solver.findPath(start, goal, HeuristicType.OCTILE, true);
        System.out.printf("Path Found: %b | Total Cost: %.2f | Path Steps: %d | Nodes Explored: %d%n",
                res8.found, res8.pathCost, res8.path.size(), res8.nodesExplored);
        solver.renderGrid(start, goal, res8);

        // Scenario 3: Trapped / Unreachable Goal
        System.out.println("Scenario 3: Unreachable Goal (Surrounded by Obstacles)");
        int[][] trappedGrid = {
            {0, 0, 0, 0},
            {0, 1, 1, 1},
            {0, 1, 0, 1},
            {0, 1, 1, 1}
        };
        AStarSolver trappedSolver = new AStarSolver(trappedGrid);
        Point trappedGoal = new Point(2, 2);
        PathResult resTrapped = trappedSolver.findPath(start, trappedGoal, HeuristicType.MANHATTAN, false);
        System.out.printf("Path Found: %b | Nodes Explored: %d%n", resTrapped.found, resTrapped.nodesExplored);
        trappedSolver.renderGrid(start, trappedGoal, resTrapped);
    }
}
