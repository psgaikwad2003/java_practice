public class SudokuSolver {

    public static boolean solveSudoku(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) return false;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    for (char ch = '1'; ch <= '9'; ch++) {
                        if (isValid(board, r, c, ch)) {
                            board[r][c] = ch;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[r][c] = '.';
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValid(char[][] board, int row, int col, char ch) {
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == ch) return false;
            if (board[i][col] == ch) return false;
            if (board[boxRowStart + i / 3][boxColStart + i % 3] == ch) return false;
        }
        return true;
    }

    public static boolean isValidSudoku(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) return false;

        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] boxes = new int[9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char ch = board[r][c];
                if (ch == '.') continue;
                if (ch < '1' || ch > '9') return false;

                int bit = 1 << (ch - '0');
                int boxIdx = (r / 3) * 3 + (c / 3);

                if ((rows[r] & bit) != 0 || (cols[c] & bit) != 0 || (boxes[boxIdx] & bit) != 0) {
                    return false;
                }

                rows[r] |= bit;
                cols[c] |= bit;
                boxes[boxIdx] |= bit;
            }
        }
        return true;
    }

    public static void printBoard(char[][] board) {
        for (int r = 0; r < 9; r++) {
            if (r % 3 == 0 && r != 0) {
                System.out.println("------+-------+------");
            }
            for (int c = 0; c < 9; c++) {
                if (c % 3 == 0 && c != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] board = {
            {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
            {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
            {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
            {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
            {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
            {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
            {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
            {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
            {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        System.out.println("Initial Board valid? " + isValidSudoku(board));
        System.out.println("Input Puzzle:");
        printBoard(board);

        if (solveSudoku(board)) {
            System.out.println("\nSolved Board:");
            printBoard(board);
            System.out.println("Final Board valid? " + isValidSudoku(board));
        } else {
            System.out.println("\nNo solution exists.");
        }
    }
}
