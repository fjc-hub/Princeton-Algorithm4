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
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> list;

    public FastCollinearPoints(Point[] points) {
        if(points == null) {
            throw new IllegalArgumentException();
        }
        int n = points.length;
        points = Arrays.copyOf(points, n);
        for(int i = 0; i < n; i++) {
            if(points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        Arrays.sort(points);
        for(int i = 1; i < n; i++) {
            if(points[i].slopeTo(points[i-1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException();
            }
        }
        Point[] aux = new Point[n];
        list = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            Point[] tmp = new Point[n];
            for(int t = 0; t < n; t++) {
                tmp[t] = points[t];
            }
            BottomUpMergeSort(tmp, aux, points[i].slopeOrder());
            int cnt = 1;
            // maintain collinear line segment' two endpoint, while avoiding select duplicated collinear line segment
            Point min = tmp[0];
            Point max = tmp[0];
            double slope = points[i].slopeTo(tmp[0]);
            for(int j = 1; j < n; j++) {
                double sp = points[i].slopeTo(tmp[j]);
                if(slope == sp) {
                    cnt++;
                } else {
                    if(cnt >= 4 && min.compareTo(points[i]) >= 0) {
                        list.add(new LineSegment(points[i], max));
                    }
                    cnt = 2;
                    slope = sp;
                    min = tmp[0];
                    max = tmp[0];
                }
                if(min != null && min.compareTo(tmp[j]) > 0) {
                    min = tmp[j];
                }
                if(max != null && max.compareTo(tmp[j]) < 0) {
                    max = tmp[j];
                }
            }
            if(cnt >= 4 && min.compareTo(points[i]) >= 0) {
                list.add(new LineSegment(points[i], tmp[n-1]));
            }
        }
    }

    //*** Stable Bottom-Up Merge-sort
    private void BottomUpMergeSort(Point[] arr, Point[] aux, Comparator<Point> comp) {
        int n = arr.length;
        for(int sz = 2; sz < 2 * n; sz = sz+sz) {
            for(int i = 0; i < n; i += sz) {
                int j = Math.min(i+sz, n) - 1;
                merge(arr, aux, i, Math.min(i+sz/2, n)-1, j, comp);
            }
        }
    }

    private void merge(Point[] a, Point[] aux, int x, int m, int y, Comparator<Point> comp) {
        int l = x; //[x, m]
        int r = m+1; //[m+1, y]
        for(int i = x; i <= y; i++) {
            if(l > m) {
                aux[i] = a[r++];
            } else if (r > y) {
                aux[i] = a[l++];
            } else if (comp.compare(a[l], a[r]) <= 0) { // the key of stability
                aux[i] = a[l++];
            } else {
                aux[i] = a[r++];
            }
        }
        for(int i = x; i <= y; i++) {
            a[i] = aux[i];
        }
    }

    public int numberOfSegments() {
        return list.size();
    }

    public LineSegment[] segments() {
        return list.toArray(new LineSegment[list.size()]);
    }

    public static void main(String[] args) {

        // Point[] points = new Point[]{
        //          new Point(0, 0),
        //          new Point(1, 1),
        //          new Point(3, 3),
        //          new Point(4, 4),
        // };
        // Point p0 = new Point(0, 0);
        // Point p1 = new Point(1, 1);
        // Point p3 = new Point(3, 3);
        // System.out.println(p0.slopeOrder().compare(p1, p3)+ "   " +p0.slopeTo(p1)+ "   " + p0.slopeTo(p3));
        //
        // FastCollinearPoints t = new FastCollinearPoints(points);
        // // System.out.println(t.numberOfSegments());
        // for(LineSegment ls : t.segments()) {
        //     System.out.println(ls.toString());
        // }

        // read the n points from a file
        In in = new In(args[0]);
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