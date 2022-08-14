import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Deque;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.List;

public class SAP {

    private int curLen;
    private int comAncestor;
    private boolean[] mark0;
    private boolean[] mark1;
    private int[] distTo0;
    private int[] distTo1;
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        ancestor(v, w);
        return curLen;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> V, Iterable<Integer> W) {
        // validation
        validateVertices(V);
        validateVertices(W);
        // init
        curLen = -1;
        comAncestor = -1;
        mark0 = new boolean[digraph.V()];
        mark1 = new boolean[digraph.V()];
        distTo0 = new int[digraph.V()];
        distTo1 = new int[digraph.V()];
        Deque<Integer> q0 = new ArrayDeque<>(), q1 = new ArrayDeque<>();
        for (int v : V) {
            mark0[v] = true;
            q0.offer(v);
        }
        for (int w : W) {
            mark1[w] = true;
            q1.offer(w);
        }
        // Bidirectional search (you can use one/two queue as you like)

        while (!q0.isEmpty() || !q1.isEmpty()) {
            if (!q0.isEmpty()) {
                int v = q0.poll();
                if (mark1[v]) {
                    if (distTo0[v] + distTo1[v] < curLen || curLen == -1) {
                        curLen = distTo0[v] + distTo1[v];
                        comAncestor = v;
                    }
                }
                // simple pruning
                if (distTo0[v] >= curLen && curLen != -1) {
                    continue;
                }
                for (int next : digraph.adj(v)) {
                    if (!mark0[next]) {
                        mark0[next] = true;
                        distTo0[next] = distTo0[v] + 1;
                        q0.offer(next);
                    }
                }
            }
            if (!q1.isEmpty()) {
                int w = q1.poll();
                if (mark0[w]) {
                    if (distTo0[w] + distTo1[w] < curLen || curLen == -1) {
                        curLen = distTo0[w] + distTo1[w];
                        comAncestor = w;
                    }
                }
                // simple pruning
                if (distTo1[w] >= curLen && curLen != -1) {
                    continue;
                }
                for (int next : digraph.adj(w)) {
                    if (!mark1[next]) {
                        mark1[next] = true;
                        distTo1[next] = distTo1[w] + 1;
                        q1.offer(next);
                    }
                }
            }
        }
        return comAncestor;
    }


    private void validateVertex(Integer v) {
        if (v == null) {
            throw new IllegalArgumentException("java generic bullshit");
        }
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        for (Integer v : vertices) {
            validateVertex(v);
        }
    }


    // do unit testing of this class
    public static void main(String[] args) {
    }
}

