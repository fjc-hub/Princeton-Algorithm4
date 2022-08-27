/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(x, y, that.x, that.y);
    }

    public String toString() {
        return "("+x+","+y+")";
    }

    public int compareTo(Point that) {
        if(x == that.x && y == that.y) {
            return 0;
        }
        if(y < that.y || (y == that.y && x < that.x)) {
            return -1;
        }
        return 1;
    }

    public double slopeTo(Point that) {
        if(x == that.x && y == that.y) {
            return Double.NEGATIVE_INFINITY;
        } else if(x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if(y == that.y) {
            return +0.0;
        } else {
            return ((double) that.y - y) / ((double) that.x - x);
        }
    }

    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                double d1 = slopeTo(o1);
                double d2 = slopeTo(o2);
                if(d1 < d2) {
                    return -1;
                } else if (d1 == d2) {
                    return 0;
                }
                return 1;
            }
        };
    }

}