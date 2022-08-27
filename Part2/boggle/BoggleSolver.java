import java.util.Set;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private static final int[][] offset = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};

    // 26-way Tries
    private static class TriesNode {
        int len;
        int score; // record scores
        boolean isEnd;
        TriesNode[] next;

        public TriesNode() {
            len = 0;
            score = 0;
            isEnd = false;
            next = new TriesNode[26];
        }
    }

    private TriesNode root;

    // iterative insertion
    private void insert(String word) {
        TriesNode point = root;
        int len = 0;
        for (char ch : word.toCharArray()) {
            int tmp = ch - 'A';
            len++;
            if (point.next[tmp] == null) {
                point.next[tmp] = new TriesNode();
            }
            point = point.next[tmp];
        }
        point.isEnd = true;
        point.len = len;
        point.score = getScore(len);
    }

    // recursive insertion (not tested yet)
    private void insertRecursively(String word) {
        // carefully: subTree's root node should change in some cases, but not this case inserting into Tries
        root = recur(word, 0, root, 0);
    }

    // semantics: inserting substring [idx, len(word)) of word into Tries represented by node
    private TriesNode recur(String word, int idx, TriesNode node, int len) {
        if (node == null) {
            node = new TriesNode();
        }
        if (idx == word.length() - 1) {
            node.isEnd = true;
            node.len = len;
            node.score = getScore(len);
            return node;
        }
        int tmp = word.charAt(idx) - 'A';
        node.next[tmp] = recur(word, idx+1, node.next[tmp], len+1);
        return node;
    }


    private TriesNode searchPrefix(String prefix) {
        TriesNode point = root;
        for (char ch : prefix.toCharArray()) {
            int tmp = ch - 'A';
            if (point.next[tmp] == null) {
                return null;
            }
            point = point.next[tmp];
        }
        return point;
    }

    private int getScore(int len) {
        if (len < 3) {
            return 0;
        } else if (len < 5) { // 3-4
            return 1;
        } else if (len < 6) { // 5
            return 2;
        } else if (len < 7) { // 6
            return 3;
        } else if (len <8) { // 7
            return 5;
        }
        return 11; // 8+
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new TriesNode();
        for (String word : dictionary) {
            insert(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows(), n = board.cols();
        List<String> list = new ArrayList<>();
        Set<TriesNode> marked = new HashSet<>(); //去重
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(root, i, j, board, new ArrayDeque<>(), new boolean[m][n], marked, list);
            }
        }
        return list;
    }

    // recursive semantics:
    private void dfs(TriesNode node, int x, int y, BoggleBoard board, Deque<Character> path, boolean[][] isVis, Set<TriesNode> marked, List<String> list) {
        if (x < 0 || x >= board.rows() || y < 0 || y >= board.cols() || isVis[x][y] || node == null) {
            return;
        }
        char ch = board.getLetter(x, y);
        int tmp = ch - 'A';
        node = node.next[tmp];
        if (node == null) { //pruning
            return;
        }
        isVis[x][y] = true;
        path.push(ch);
        if (ch == 'Q') {
            node = node.next['U'-'A'];
            path.push('U');
            if (node == null) { // prune again
                return;
            }
        }

        if (node.isEnd && node.len >= 3 && !marked.contains(node)) {
            list.add(pathToString(path));
            marked.add(node);
        }
        for (int[] off : offset) {
            int dx = x + off[0], dy = y + off[1];
            dfs(node, dx, dy, board, path, isVis, marked, list);
        }
        char pop = path.pop();
        if (pop == 'U' && !path.isEmpty() && path.peekFirst() == 'Q') {
            path.pop();
        }
        isVis[x][y] = false;
    }

    private String pathToString(Deque<Character> path) {
        StringBuilder sb = new StringBuilder();
        for (char ch : path) {
            sb.append(ch);
        }
        return sb.reverse().toString();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        TriesNode ret = searchPrefix(word);
        if (ret == null || !ret.isEnd) {
            return 0;
        }
        return ret.score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word+", "+solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}