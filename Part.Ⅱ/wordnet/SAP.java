import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Iterator;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfp0 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(digraph, w);
        int ca = universal(bfp0, bfp1);
        return ca == -1 ? -1 : bfp0.distTo(ca) + bfp1.distTo(ca);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return universal(new BreadthFirstDirectedPaths(digraph, v), new BreadthFirstDirectedPaths(digraph, w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("variable can not be null");
        }
        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }
        BreadthFirstDirectedPaths bfp0 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfp1 = new BreadthFirstDirectedPaths(digraph, w);
        int ca = universal(bfp0, bfp1);
        return ca == -1 ? -1 : bfp0.distTo(ca) + bfp1.distTo(ca);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("variable can not be null");
        }
        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }
        return universal(new BreadthFirstDirectedPaths(digraph, v), new BreadthFirstDirectedPaths(digraph, w));
    }

    // return common ancestor that participates in shortest ancestral path;
    private int universal(BreadthFirstDirectedPaths bfp0, BreadthFirstDirectedPaths bfp1) {
        int min = Integer.MAX_VALUE;
        int ca = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfp0.hasPathTo(i) && bfp1.hasPathTo(i)) {
                int tmp = bfp0.distTo(i) + bfp1.distTo(i);
                if (tmp < min) {
                    min = tmp;
                    ca = i;
                }
            }
        }
        return ca;
    }

    private boolean hasValue(Iterable<Integer> a) {
        Iterator<Integer> it = a.iterator();
        return it.hasNext();
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
