/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private int cnt;
    //两个虚拟节点
    private final int top; // all top rows common parent node
    private final int bottom; // all bottom row common parent node
    private boolean[] isOpen;
    private final WeightedQuickUnionUF weightedQuickUnionUF; //ESSENCE
    private final WeightedQuickUnionUF avoidBackWash; //ESSENCE

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        top = n*n;
        bottom = n*n+1;
        isOpen = new boolean[n*n+2];
        isOpen[top] = true;
        isOpen[bottom] = true;
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
        avoidBackWash = new WeightedQuickUnionUF(n*n+1); //
        // for (int i = 0; i < n; i++) {
        //     weightedQuickUnionUF.union(top, i);
        //     weightedQuickUnionUF.union(bottom, n*(n-1)+i);
        // }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!(1 <= row && row <= n && 1 <= col && col <= n)) {
            throw new IllegalArgumentException();
        }
        int x = row - 1;
        int y = col - 1;
        if (isOpen[x*n+y]) {
            return;
        }
        cnt++;
        isOpen[x*n+y] = true;
        final int[][] offset = new int[][]{{0,1}, {1,0}, {-1,0}, {0,-1}};
        for (int[] off : offset) {
            int dx = x + off[0];
            int dy = y + off[1];
            if (!(0 <= dx && dx < n && 0 <= dy && dy < n && isOpen[dx*n+dy])) {
                continue;
            }
            weightedQuickUnionUF.union(dx*n+dy, x*n+y);
            avoidBackWash.union(dx*n+dy, x*n+y);

        }
        //千万不要在open之前就与top或者bottom这两个虚拟节点union
        if(row == 1) {
            weightedQuickUnionUF.union(top, x*n+y);
            avoidBackWash.union(top,x*n+y);
        }
        if(row == n) {
            weightedQuickUnionUF.union(bottom, x*n+y);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!(1 <= row && row <= n && 1 <= col && col <= n)) {
            throw new IllegalArgumentException();
        }
        return isOpen[(row-1)*n+col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!(1 <= row && row <= n && 1 <= col && col <= n)) {
            throw new IllegalArgumentException();
        }
        return avoidBackWash.find(top) == avoidBackWash.find((row-1)*n+col-1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.find(top) == weightedQuickUnionUF.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
