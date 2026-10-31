import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

/**
 * Trapping Rain Water Algorithms
 *
 * Implements multiple approaches for 1D and 2D terrain:
 * 1. Two Pointers - O(n) time, O(1) space
 * 2. Dynamic Programming (Prefix & Suffix Max) - O(n) time, O(n) space
 * 3. Monotonic Decreasing Stack - O(n) time, O(n) space
 * 4. Trapping Rain Water II (3D Elevation Grid) - O(M*N log(M*N)) using Min-Heap
 */
public class TrappingRainWater {

    // -------------------------------------------------------------
    // Approach 1: Two Pointers (Optimal 1D)
    // Time Complexity: O(n), Space Complexity: O(1)
    // -------------------------------------------------------------
    public static int trapTwoPointers(int[] height) {
        if (height == null || height.length < 3) return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    totalWater += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    totalWater += rightMax - height[right];
                }
                right--;
            }
        }

        return totalWater;
    }

    // -------------------------------------------------------------
    // Approach 2: Dynamic Programming (Prefix and Suffix Max)
    // Time Complexity: O(n), Space Complexity: O(n)
    // -------------------------------------------------------------
    public static int trapDP(int[] height) {
        if (height == null || height.length < 3) return 0;

        int n = height.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(leftMax[i], rightMax[i]);
            if (waterLevel > height[i]) {
                totalWater += waterLevel - height[i];
            }
        }

        return totalWater;
    }

    // -------------------------------------------------------------
    // Approach 3: Monotonic Stack (Computes bounded water horizontally)
    // Time Complexity: O(n), Space Complexity: O(n)
    // -------------------------------------------------------------
    public static int trapStack(int[] height) {
        if (height == null || height.length < 3) return 0;

        int totalWater = 0;
        Deque<Integer> stack = new ArrayDeque<>();

        for (int current = 0; current < height.length; current++) {
            while (!stack.isEmpty() && height[current] > height[stack.peek()]) {
                int bottomIndex = stack.pop();
                if (stack.isEmpty()) break;

                int leftIndex = stack.peek();
                int distance = current - leftIndex - 1;
                int boundedHeight = Math.min(height[current], height[leftIndex]) - height[bottomIndex];

                totalWater += distance * boundedHeight;
            }
            stack.push(current);
        }

        return totalWater;
    }

    // -------------------------------------------------------------
    // Trapping Rain Water II (3D Elevation Grid)
    // Time Complexity: O(M*N log(M*N)), Space: O(M*N)
    // -------------------------------------------------------------
    private static class Cell implements Comparable<Cell> {
        int row, col, height;

        Cell(int row, int col, int height) {
            this.row = row;
            this.col = col;
            this.height = height;
        }

        @Override
        public int compareTo(Cell other) {
            return Integer.compare(this.height, other.height);
        }
    }

    public static int trapRainWater2D(int[][] heightMap) {
        if (heightMap == null || heightMap.length < 3 || heightMap[0].length < 3) return 0;

        int m = heightMap.length;
        int n = heightMap[0].length;
        boolean[][] visited = new boolean[m][n];
        PriorityQueue<Cell> minHeap = new PriorityQueue<>();

        // Add all outer boundary cells to the min-heap
        for (int r = 0; r < m; r++) {
            minHeap.offer(new Cell(r, 0, heightMap[r][0]));
            minHeap.offer(new Cell(r, n - 1, heightMap[r][n - 1]));
            visited[r][0] = true;
            visited[r][n - 1] = true;
        }

        for (int c = 1; c < n - 1; c++) {
            minHeap.offer(new Cell(0, c, heightMap[0][c]));
            minHeap.offer(new Cell(m - 1, c, heightMap[m - 1][c]));
            visited[0][c] = true;
            visited[m - 1][c] = true;
        }

        int totalWater = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!minHeap.isEmpty()) {
            Cell current = minHeap.poll();

            for (int[] dir : directions) {
                int nr = current.row + dir[0];
                int nc = current.col + dir[1];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    // If neighbor is lower than current boundary height, water is trapped
                    if (heightMap[nr][nc] < current.height) {
                        totalWater += current.height - heightMap[nr][nc];
                    }
                    // Push cell with effective water-level height to maintain containment boundary
                    minHeap.offer(new Cell(nr, nc, Math.max(heightMap[nr][nc], current.height)));
                }
            }
        }

        return totalWater;
    }

    // Helper to print ASCII visualization of 1D elevation and trapped water
    public static void printAsciiTerrain(int[] height) {
        if (height == null || height.length == 0) return;
        int n = height.length;
        int maxHeight = 0;
        for (int h : height) maxHeight = Math.max(maxHeight, h);

        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) rightMax[i] = Math.max(rightMax[i + 1], height[i]);

        System.out.println("ASCII Terrain (# = Bar, ~ = Water):");
        for (int level = maxHeight; level >= 1; level--) {
            System.out.printf("%2d | ", level);
            for (int i = 0; i < n; i++) {
                int waterLine = Math.min(leftMax[i], rightMax[i]);
                if (height[i] >= level) {
                    System.out.print("# ");
                } else if (waterLine >= level) {
                    System.out.print("~ ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.print("     ");
        for (int i = 0; i < n; i++) System.out.print("--");
        System.out.println();
        System.out.print("Idx: ");
        for (int i = 0; i < n; i++) System.out.printf("%d ", i % 10);
        System.out.println("\n");
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("        TRAPPING RAIN WATER ALGORITHMS           ");
        System.out.println("=================================================");

        // Test Case 1: Standard LeetCode elevation profile
        int[] terrain1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println("Test Case 1:");
        System.out.println("Heights: " + Arrays.toString(terrain1));
        printAsciiTerrain(terrain1);

        int ansTwoPointers = trapTwoPointers(terrain1);
        int ansDP = trapDP(terrain1);
        int ansStack = trapStack(terrain1);

        System.out.println("Two Pointers Result : " + ansTwoPointers + " units");
        System.out.println("Dynamic Prog Result  : " + ansDP + " units");
        System.out.println("Monotonic Stack Result: " + ansStack + " units");
        System.out.println("All methods match   : " + (ansTwoPointers == ansDP && ansDP == ansStack));

        // Test Case 2: Deep basin
        int[] terrain2 = {4, 2, 0, 3, 2, 5};
        System.out.println("\nTest Case 2: Deep Basin");
        System.out.println("Heights: " + Arrays.toString(terrain2));
        printAsciiTerrain(terrain2);
        System.out.println("Trapped Water: " + trapTwoPointers(terrain2) + " units (Expected: 9)");

        // Test Case 3: Edge cases (Flat, strictly decreasing, insufficient points)
        System.out.println("\nEdge Cases:");
        int[] flat = {3, 3, 3, 3};
        int[] descending = {5, 4, 3, 2, 1};
        int[] shortArr = {2, 1};
        System.out.println("Flat terrain [3,3,3,3]          : " + trapTwoPointers(flat) + " units");
        System.out.println("Descending terrain [5,4,3,2,1]  : " + trapTwoPointers(descending) + " units");
        System.out.println("Short terrain [2,1]             : " + trapTwoPointers(shortArr) + " units");

        // Test Case 4: Trapping Rain Water II (2D grid / 3D elevation)
        System.out.println("\n-------------------------------------------------");
        System.out.println("  Trapping Rain Water II (3D Elevation Matrix)   ");
        System.out.println("-------------------------------------------------");
        int[][] heightMap = {
            {1, 4, 3, 1, 3, 2},
            {3, 2, 1, 3, 2, 4},
            {2, 3, 3, 2, 3, 1}
        };

        System.out.println("Elevation Grid (3x6):");
        for (int[] row : heightMap) {
            System.out.println("  " + Arrays.toString(row));
        }
        int water2D = trapRainWater2D(heightMap);
        System.out.println("3D Trapped Water: " + water2D + " units (Expected: 4)");
        System.out.println("=================================================");
    }
}
