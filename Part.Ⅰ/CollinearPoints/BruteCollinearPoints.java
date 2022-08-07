/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> list;

    public BruteCollinearPoints(Point[] points) {
        if(points == null) {
            throw new IllegalArgumentException();
        }
        int n = points.length;
        Point[] exchange = new Point[n];
        // points = Arrays.copyOf(points, n);
        for(int i = 0; i < n; i++) {
            if(points[i] == null) {
                throw new IllegalArgumentException();
            }
            exchange[i] = points[i];
        }
        points = exchange;
        Arrays.sort(points);
        for(int i = 1; i < n; i++) {
            if(points[i].slopeTo(points[i-1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException();
            }
        }
        // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
        list = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = i+1; j < n; j++) {
                for(int k = j+1; k < n; k++) {
                    if(points[k] == null) {
                        throw new IllegalArgumentException();
                    }
                    double t1 = points[i].slopeTo(points[j]);
                    double t2 = points[i].slopeTo(points[k]);
                    if(t1 != t2) {
                        continue;
                    }
                    for(int t = k+1; t < n; t++) {
                        if(t1 == points[i].slopeTo(points[t])) {
                            list.add(new LineSegment(points[i], points[t]));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return list.size();
    }

    public LineSegment[] segments() {
        return list.toArray(new LineSegment[list.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("input8.txt");
        // In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}