/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int T;
    private int n;
    private final double nn;
    private final double[] stats;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        T = trials;
        nn = (double) n*n;
        stats = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                percolation.open(row, col);
            }
            stats[i] = percolation.numberOfOpenSites() / nn;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(stats);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(stats);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - StdStats.stddev(stats) * 1.96 / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + StdStats.stddev(stats) * 1.96 / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats obj = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("mean                    = %f\n", obj.mean());
        System.out.printf("stddev                  = %f\n", obj.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]\n", obj.confidenceLo(), obj.confidenceHi());
    }

}