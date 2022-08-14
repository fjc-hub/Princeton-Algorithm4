
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;


// WordNet must be a DAG
public class WordNet {

    private class Synset {
        int id;
        String words;

        public Synset(int id, String words) {
            this.id = id;
            this.words = words;
        }
    }

    private final SAP sap;
    private final Digraph digraph;
    private final List<Integer>[] adj;
    private final Map<String, List<Integer>> nounToSet; // noun => Synset id
    private Map<Integer, Synset> idToSet; // id => Synset

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        nounToSet = new HashMap<>();
        idToSet = new HashMap<>();
        In in0 = new In(synsets);
        while (in0.hasNextLine()) {
            String[] fields = in0.readLine().split(",");
            int id = Integer.valueOf(fields[0]);
            idToSet.put(id, new Synset(id, fields[1]));
            String[] words = fields[1].split(" ");
            for (String str : words) {
                if (nounToSet.get(str) == null) {
                    nounToSet.put(str, new ArrayList<>());
                }
                nounToSet.get(str).add(id);
            }
        }
        digraph = new Digraph(idToSet.size());
        adj = new List[idToSet.size()];
        In in1 = new In(hypernyms);
        while (in1.hasNextLine()) {
            String[] ids = in1.readLine().split(",");
            Synset node = idToSet.get(Integer.valueOf(ids[0]));
            adj[node.id] = new ArrayList<>();
            for (int i = 1; i < ids.length; i++) {
                int w = Integer.valueOf(ids[i]);
                adj[node.id].add(w);
                digraph.addEdge(node.id, w);
            }
        }
        // The calling order of these two functions cannot be changed
        ifCyclic();
        ifNoRoot();
        sap = new SAP(digraph);
    }

    private void ifCyclic() {
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("word net should not have a cycle in it");
        }
    }

    // pre-required digraph is DAG, then
    private void ifNoRoot() {
        int cnt = 0;
        for (int i=0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                cnt++;
                if (cnt >= 2) {
                    throw new IllegalArgumentException("word net should not have more than one root");
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSet.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToSet.containsKey(word);
    }

    // distance between nounA and nounB
    // can't use two-direction BFS on Digraph to query shortest path like on undirected graph
    // https://oi-wiki.org/search/bidirectional/
//    public int distance(String nounA, String nounB) {
//        if (!isNoun(nounA) || !isNoun(nounB)) {
//            throw new IllegalArgumentException();
//        }
//        List<Integer> listA = nounToSet.get(nounA), listB = nounToSet.get(nounB);
//        int[][] dist = new int[2][idToSet.size()]; // mark node from an end
//        Arrays.fill(dist[0], -1);
//        Arrays.fill(dist[1], -1);
//        for (int id : listA) {
//            dist[0][id] = 0; // mark this node as 0
//        }
//        for (int id : listB) {
//            dist[1][id] = 0; // mark this node as 1
//        }
//        bfsDistTo(listA, dist[0]);
//        bfsDistTo(listB, dist[1]);
//        int ans = Integer.MAX_VALUE;
//        for (int i = 0; i < dist[0].length; i++) {
//            if (dist[0][i] != -1 && dist[1][i] != -1 && dist[0][i] + dist[1][i] < ans) {
//                ans = dist[0][i] + dist[1][i];
//            }
//        }
//        return ans;
//    }
//
//    // distTo has already init
//    private void bfsDistTo(List<Integer> start, int[] distTo) {
//        Deque<Integer> deque = new ArrayDeque<>();
//        deque.addAll(start);
//        while (!deque.isEmpty()) {
//            int cur = deque.poll();
//            for (int next : adj[cur]) {
//                if (distTo[next] != -1) {
//                    continue;
//                }
//                distTo[next] = distTo[cur] + 1;
//                deque.offer(next);
//            }
//        }
//    }

        public int distance(String nounA, String nounB) {
            if (!isNoun(nounA) || !isNoun(nounB)) {
                throw new IllegalArgumentException();
            }
            return sap.length(nounToSet.get(nounA), nounToSet.get(nounB));
        }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return idToSet.get(sap.ancestor(nounToSet.get(nounA), nounToSet.get(nounB))).words;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wn.distance("Turkic_language", "glyceric_acid"));
//        WordNet wn = new WordNet("synsets8.txt", "hypernyms8WrongBFS.txt");
//        System.out.println(wn.distance("a", "e"));
    }
}