package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the N-Queens problem using classic Backtracking and Bitmask Optimization.
 * 
 * Features:
 * 1. Backtracking with Boolean Arrays (O(N!) time, O(N) space): Finds all valid chessboard layouts.
 * 2. Bitmask Backtracking: Ultra-fast state representation using bitwise flags.
 * 3. Total solution counting: Standard benchmark for N = 1 to 10.
 * 4. Beautiful visual board printing.
 */
public class NQueensBacktrackingDemo {

    /**
     * Solves the N-Queens puzzle and returns all distinct board configurations.
     *
     * @param n board dimension (N x N) and number of queens
     * @return List of solutions, where each solution is a list of N strings representing rows
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        if (n <= 0) return solutions;

        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }

        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n - 1]; // row - col + (n - 1)
        boolean[] diag2 = new boolean[2 * n - 1]; // row + col

        backtrack(0, n, board, cols, diag1, diag2, solutions);
        return solutions;
    }

    private static void backtrack(int row, int n, char[][] board,
                                  boolean[] cols, boolean[] diag1, boolean[] diag2,
                                  List<List<String>> solutions) {
        if (row == n) {
            List<String> validBoard = new ArrayList<>();
            for (char[] r : board) {
                validBoard.add(new String(r));
            }
            solutions.add(validBoard);
            return;
        }

        for (int col = 0; col < n; col++) {
            int d1 = row - col + (n - 1);
            int d2 = row + col;

            if (!cols[col] && !diag1[d1] && !diag2[d2]) {
                board[row][col] = 'Q';
                cols[col] = true;
                diag1[d1] = true;
                diag2[d2] = true;

                backtrack(row + 1, n, board, cols, diag1, diag2, solutions);

                // Backtrack
                board[row][col] = '.';
                cols[col] = false;
                diag1[d1] = false;
                diag2[d2] = false;
            }
        }
    }

    /**
     * Bitmask solver: counts total valid solutions with minimal memory and maximum speed.
     *
     * @param n board size
     * @return total number of distinct solutions
     */
    public static int totalNQueensBitmask(int n) {
        if (n <= 0 || n > 32) return 0;
        int limit = (1 << n) - 1;
        return bitmaskHelper(0, 0, 0, limit);
    }

    private static int bitmaskHelper(int cols, int diag1, int diag2, int limit) {
        if (cols == limit) {
            return 1;
        }

        int count = 0;
        // Available positions in the current row
        int available = limit & ~(cols | diag1 | diag2);

        while (available != 0) {
            // Extract the lowest set bit (position for queen)
            int bit = available & -available;
            available -= bit;

            count += bitmaskHelper(
                cols | bit,
                (diag1 | bit) << 1,
                (diag2 | bit) >> 1,
                limit
            );
        }
        return count;
    }

    /**
     * Prints a formatted chessboard configuration.
     *
     * @param board list of strings representing rows
     */
    public static void printBoard(List<String> board) {
        int n = board.size();
        System.out.println("+" + "---".repeat(n) + "+");
        for (String row : board) {
            System.out.print("|");
            for (char c : row.toCharArray()) {
                System.out.print(c == 'Q' ? " Q " : " . ");
            }
            System.out.println("|");
        }
        System.out.println("+" + "---".repeat(n) + "+");
    }

    public static void main(String[] args) {
        System.out.println("=== N-Queens Backtracking & Bitmask Demo ===\n");

        // Solution count benchmark for N = 1 to 8
        System.out.println("Total solutions count per N:");
        for (int n = 1; n <= 8; n++) {
            int total = totalNQueensBitmask(n);
            System.out.printf("  N = %d -> %d distinct solutions%n", n, total);
        }

        // Detailed board layout for N = 4
        int demoN = 4;
        System.out.printf("%nDetailed Solutions for N = %d:%n", demoN);
        List<List<String>> solutionsN4 = solveNQueens(demoN);
        for (int i = 0; i < solutionsN4.size(); i++) {
            System.out.printf("Solution #%d:%n", i + 1);
            printBoard(solutionsN4.get(i));
            System.out.println();
        }
    }
}
