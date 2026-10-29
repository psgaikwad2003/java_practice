import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordSearch {

    public static boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || word == null || word.isEmpty()) return false;
        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == word.charAt(0) && dfs(board, word, 0, r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(char[][] board, String word, int index, int r, int c) {
        if (index == word.length()) return true;

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c] != word.charAt(index)) {
            return false;
        }

        char original = board[r][c];
        board[r][c] = '#';

        boolean found = dfs(board, word, index + 1, r + 1, c) ||
                        dfs(board, word, index + 1, r - 1, c) ||
                        dfs(board, word, index + 1, r, c + 1) ||
                        dfs(board, word, index + 1, r, c - 1);

        board[r][c] = original;
        return found;
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word = null;
    }

    public static List<String> findWords(char[][] board, String[] words) {
        Set<String> result = new HashSet<>();
        if (board == null || board.length == 0 || words == null || words.length == 0) {
            return new ArrayList<>();
        }

        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode curr = root;
            for (char ch : w.toCharArray()) {
                curr = curr.children.computeIfAbsent(ch, k -> new TrieNode());
            }
            curr.word = w;
        }

        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (root.children.containsKey(board[r][c])) {
                    dfsTrie(board, r, c, root, result);
                }
            }
        }

        return new ArrayList<>(result);
    }

    private static void dfsTrie(char[][] board, int r, int c, TrieNode parent, Set<String> result) {
        char ch = board[r][c];
        TrieNode curr = parent.children.get(ch);
        if (curr == null) return;

        if (curr.word != null) {
            result.add(curr.word);
        }

        board[r][c] = '#';
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (nr >= 0 && nr < board.length && nc >= 0 && nc < board[0].length && board[nr][nc] != '#') {
                if (curr.children.containsKey(board[nr][nc])) {
                    dfsTrie(board, nr, nc, curr, result);
                }
            }
        }

        board[r][c] = ch;

        if (curr.children.isEmpty()) {
            parent.children.remove(ch);
        }
    }

    public static void main(String[] args) {
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };

        System.out.println("Word Search I (Single Word):");
        System.out.println("Contains 'ABCCED': " + exist(board, "ABCCED"));
        System.out.println("Contains 'SEE':    " + exist(board, "SEE"));
        System.out.println("Contains 'ABCB':   " + exist(board, "ABCB"));

        char[][] multiBoard = {
            {'o', 'a', 'a', 'n'},
            {'e', 't', 'a', 'e'},
            {'i', 'h', 'k', 'r'},
            {'i', 'f', 'l', 'v'}
        };
        String[] dictionary = {"oath", "pea", "eat", "rain", "hklf"};
        System.out.println("\nWord Search II (Multi-Word Dictionary):");
        System.out.println("Words found: " + findWords(multiBoard, dictionary));
    }
}
