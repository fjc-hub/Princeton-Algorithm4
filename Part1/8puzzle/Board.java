/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable
public class Board {

    private static final int[][] OFFSET = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private int hamming;
    private int mahanttan;
    private final int zeroX;
    private final int zeroY;
    private final int[][] board;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        board = new int[n][n];
        int tmpx = -1;
        int tmpy = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    tmpx = i;
                    tmpy = j;
                }
            }
        }
        zeroX = tmpx;
        zeroY = tmpy;
        hamming = -1;
        mahanttan = -1;
    }

    // string representation of this board
    public String toString() {
        int n = board.length;
        StringBuilder sb = new StringBuilder();
        sb.append(n+"\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(board[i][j]+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1) {
            return hamming;
        }
        int cnt = 0;
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == i*n+j+1 || board[i][j] == 0) {
                    continue;
                }
                cnt++;
            }
        }
        hamming = cnt;
        return cnt;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (mahanttan != -1) {
            return mahanttan;
        }
        int cnt = 0;
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == i*n+j+1 || board[i][j] == 0) {
                    continue;
                }
                int x = (board[i][j] - 1) / n;
                int y = (board[i][j] - 1) % n;
                cnt = cnt + Math.abs(x - i) + Math.abs(y - j);
            }
        }
        mahanttan = cnt;
        return cnt;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == i*n+j+1 || (board[i][j] == 0 && i == n-1 && j == n-1)) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        Board b = (Board) y;
        if (this.board.length != b.board.length) {
            return false;
        }
        for (int i = 0; i < this.board.length; i++) {
            if (!Arrays.equals(this.board[i], b.board[i])) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = board.length;
        List<Board> list = new ArrayList<>();
        for (int[] off : OFFSET) {
            int x = zeroX + off[0];
            int y = zeroY + off[1];
            if (x < 0 || n <= x || y < 0 || n <= y) {
                continue;
            }
            int[][] newArr = copy(board);
            int tmp = board[x][y];
            newArr[x][y] = 0;
            newArr[zeroX][zeroY] = tmp;
            Board b = new Board(newArr);
            list.add(b);
        }
        return list;
    }

    private int[][] copy(int[][] arr) {
        int n = arr.length;
        int[][] newArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newArr[i][j] = arr[i][j];
            }
        }
        return newArr;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = board.length;
        int[][] newArr = copy(board);
        int t1 = (zeroX * n + zeroY - 1 + n * n) % (n * n);
        int t2 = (zeroX * n + zeroY + 1 + n * n) % (n * n);
        int[] tile1 = new int[]{t1 / n, t1 % n};
        int[] tile2 = new int[]{t2 / n, t2 % n};
        int tmp = newArr[tile1[0]][tile1[1]];
        newArr[tile1[0]][tile1[1]] = newArr[tile2[0]][tile2[1]];
        newArr[tile2[0]][tile2[1]] = tmp;
        return new Board(newArr);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] arr = new int[][]{{7, 3, 4}, {6, 2, 5}, {0, 8, 1}};
        Board b0 = new Board(arr);
        Board b1 = new Board(arr);
        System.out.println(b0.toString());
        System.out.println(b0.dimension());
        System.out.println(b0.equals(b1));
        System.out.println(b0.hamming());
        System.out.println(b1.manhattan());
        System.out.println(b0.isGoal());
        for (Board b : b0.neighbors()) {
            System.out.println(b.toString());
        }
        System.out.println(b1.twin());
    }

}
