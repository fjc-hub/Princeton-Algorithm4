/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// immutable
public class Solver {

    //Search Node
    private class Node {
        int moves;
        int priority;
        Board board;
        Node prev;

        public Node() {
        }

        public Node(int moves, Board board, Node prev) {
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.board = board;
            this.prev = prev;
        }
    }

    private List<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if(initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> pq1 = new MinPQ<>(new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return o1.priority - o2.priority;
            }
        });
        MinPQ<Node> pq2 = new MinPQ<>(new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return o1.priority - o2.priority;
            }
        });
        // Board starting = new Board(initial.getBoard());
        // List<Board> path1 = new ArrayList<>(); // error
        pq1.insert(new Node(0, initial, null));
        pq2.insert(new Node(0, initial.twin(), null));
        // You can't use hash table to reduce the exploration of redundant Search Node
        // Set<String> set = new HashSet<>();
        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            // proceed on pq1
            Node node1 = pq1.delMin();
            if (node1.board.isGoal()) {
                solution = new ArrayList<>();
                while (node1 != null) {
                    solution.add(node1.board);
                    node1 = node1.prev;
                }
                Collections.reverse(solution);
                pq1 = null;
                pq2 = null;
                return;
            }
            for(Board next : node1.board.neighbors()) {
                if(node1.prev != null && node1.prev.board.equals(next)) {
                    continue;
                }
                pq1.insert(new Node(node1.moves+1, next, node1));
            }
            // proceed on pq2
            Node node2 = pq2.delMin();
            if (node2.board.isGoal()) {
                pq1 = null;
                pq2 = null;
                return;
            }
            for(Board next : node2.board.neighbors()) {
                if(node2.prev != null && node2.prev.board.equals(next)) {
                    continue;
                }
                pq2.insert(new Node(node2.moves+1, next, node2));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if(isSolvable()) {
            return solution.size() - 1;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if(solution != null) {
            return new ArrayList<>(solution);
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
