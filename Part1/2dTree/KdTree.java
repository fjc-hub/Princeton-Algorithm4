/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {


    private class Node {
        private boolean isVerti;
        private Point2D point;
        private RectHV rectHV;
        private Node left, right;

        public Node() {
        }

        public Node(boolean isVerti, Point2D point, RectHV rectHV) {
            this.isVerti = isVerti;
            this.point = point;
            this.rectHV = rectHV;
        }
    }

    private int size;
    private Node root;

    public KdTree() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            size = 1;
            root = new Node(true, p, new RectHV(0, 0, 1, 1));
            return;
        }
        if (insert(root, p, null, true)) {
            size++;
        }
    }

    // no need to return Node, because the insert method will insert any new node at bottom of a leave;
    private boolean insert(Node subTree, Point2D p, Node pa, boolean isLeft) {
        if (subTree == null) {
            RectHV newRect = null;
            if (pa.isVerti) {
                if (isLeft) {
                    newRect = new RectHV(pa.rectHV.xmin(), pa.rectHV.ymin(), pa.point.x(),
                                         pa.rectHV.ymax());
                    pa.left = new Node(!pa.isVerti, p, newRect);
                }
                else {
                    newRect = new RectHV(pa.point.x(), pa.rectHV.ymin(), pa.rectHV.xmax(),
                                         pa.rectHV.ymax());
                    pa.right = new Node(!pa.isVerti, p, newRect);
                }
            }
            else {
                if (isLeft) {
                    newRect = new RectHV(pa.rectHV.xmin(), pa.rectHV.ymin(), pa.rectHV.xmax(),
                                         pa.point.y());
                    pa.left = new Node(!pa.isVerti, p, newRect);
                }
                else {
                    newRect = new RectHV(pa.rectHV.xmin(), pa.point.y(), pa.rectHV.xmax(),
                                         pa.rectHV.ymax());
                    pa.right = new Node(!pa.isVerti, p, newRect);
                }
            }
            return true;
        }
        else if (subTree.point.equals(p)) {
            return false;
        }
        else if (subTree.isVerti) {
            if (p.x() < subTree.point.x()) {
                return insert(subTree.left, p, subTree, true);
            }
            else {
                return insert(subTree.right, p, subTree, false);
            }
        }
        else {
            if (p.y() < subTree.point.y()) {
                return insert(subTree.left, p, subTree, true);
            }
            else {
                return insert(subTree.right, p, subTree, false);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node subTree, Point2D p) {
        if (subTree == null) {
            return false;
        }
        else if (subTree.point.equals(p)) {
            return true;
        }
        else if (subTree.isVerti) {
            if (p.x() < subTree.point.x()) {
                return contains(subTree.left, p);
            }
            else {
                return contains(subTree.right, p);
            }
        }
        else {
            if (p.y() < subTree.point.y()) {
                return contains(subTree.left, p);
            }
            else {
                return contains(subTree.right, p);
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node subTree) {
        if (subTree == null) {
            return;
        }
        if (subTree.isVerti) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(subTree.point.x(), subTree.rectHV.ymin(), subTree.point.x(),
                         subTree.rectHV.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(subTree.rectHV.xmin(), subTree.point.y(), subTree.rectHV.xmax(),
                         subTree.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(subTree.point.x(), subTree.point.y());

        draw(subTree.left);
        draw(subTree.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> list = new ArrayList<>();
        rangeSearch(root, rect, list);
        return list;
    }

    private void rangeSearch(Node subTree, RectHV rect, List<Point2D> list) {
        if (subTree == null || !rect.intersects(subTree.rectHV)) {
            return;
        }
        if (rect.contains(subTree.point)) {
            list.add(subTree.point);
        }
        // RectHV rect0 = null, rect1 = null;
        // if (subTree.isVerti) {
        //     rect0 = new RectHV(subTree.rectHV.xmin(), subTree.rectHV.ymin(), subTree.point.x(), subTree.rectHV.ymax());
        //     rect1 = new RectHV(subTree.point.x(), subTree.rectHV.ymin(), subTree.rectHV.xmax(), subTree.rectHV.ymax());
        // } else {
        //     rect0 = new RectHV(subTree.rectHV.xmin(), subTree.rectHV.ymin(), subTree.rectHV.xmax(), subTree.point.y());
        //     rect1 = new RectHV(subTree.rectHV.xmin(), subTree.point.y(), subTree.rectHV.xmax(), subTree.rectHV.ymax());
        // }
        rangeSearch(subTree.left, rect, list);
        rangeSearch(subTree.right, rect, list);
    }

    private double minDis = Double.NaN;
    private Point2D nearest;

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        minDis = Double.POSITIVE_INFINITY;
        nearest = null;
        calculateNearest(root, p);
        return nearest;
    }

    private void calculateNearest(Node subTree, Point2D target) {
        if (subTree == null || subTree.rectHV.distanceTo(target) >= minDis) {
            return;
        }
        double tmp = target.distanceTo(subTree.point);
        if (tmp < minDis) {
            minDis = tmp;
            nearest = subTree.point;
        }
        // always choose the subtree that is on the same side of the splitting line
        // as the query point as the first subtree to explore
        if (subTree.isVerti) {
            if (target.x() < subTree.point.x()) {
                calculateNearest(subTree.left, target);
                calculateNearest(subTree.right, target);
            }
            else {
                calculateNearest(subTree.right, target);
                calculateNearest(subTree.left, target);
            }
        }
        else {
            if (target.y() < subTree.point.y()) {
                calculateNearest(subTree.left, target);
                calculateNearest(subTree.right, target);
            }
            else {
                calculateNearest(subTree.right, target);
                calculateNearest(subTree.left, target);
            }
        }
    }

    /**
     * How to debug this application????
     * <p>
     * This main computes and draws the 2d-tree that results from
     * the sequence of points clicked by the user in the standard drawing window
     * <p>
     */
    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                // StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                    System.out.println(kdtree.size() + ", " + kdtree.isEmpty());
                }
            }
            StdDraw.pause(20);
        }
    }
}
