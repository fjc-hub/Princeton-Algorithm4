
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    private class Synset {
        int id;
        String[] synset;
        Synset[] hypernyms;

        public Synset(int id, String[] synset) {
            this.id = id;
            this.synset = synset;
        }
    }

    private int root;
    private List<Integer>[] adj;
    private Map<String, Synset> nounToSet; // noun => Synset
    private Map<Integer, Synset> idToSet; // id => Synset

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        nounToSet = new HashMap<>();
        List<Synset> list = new ArrayList<>();
        In in0 = new In(synsets);
        int len = 0;
        while (in0.hasNextLine()) {
            String[] strs = in0.readLine().split(",", 2);
            String[] tmp = strs[1].split(" ");
            Synset ss = new Synset(len++, tmp);
            list.add(ss);
            for (String str : tmp) {
                nounToSet.put(str, ss);
            }
        }
        adj = new List[len];
        int[] cnts = new int[len];
        In in1 = new In(hypernyms);
        while (in1.hasNextLine()) {
            String[] ids = in1.readLine().split(",");
            Synset node = list.get(Integer.valueOf(ids[0]));
            cnts[node.id] += ids.length - 1;
            adj[node.id] = new ArrayList<>();
            node.hypernyms = new Synset[ids.length-1];
            for (int i = 1; i < ids.length; i++) {
                int tmp = Integer.valueOf(ids[i]);
                node.hypernyms[i-1] = list.get(tmp);
                adj[node.id].add(tmp);
            }
        }
        // choose root
        root = -1;
        for (int i = 0; i < cnts.length; i++) {
            if (cnts[i] == 0 && root == -1) {
                root = i;
            } else if (cnts[i] == 0) {
                throw new IllegalArgumentException();
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

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> s1 = new HashSet<>(), s2 = new HashSet<>();
        int a = nounToSet.get(nounA).id, b = nounToSet.get(nounB).id;
        s1.add(a);
        s2.add(b);
        Set<Integer> vis1 = new HashSet<>();
        Set<Integer> vis2 = new HashSet<>();
        boolean sign = true;
        int ans = 0;
        while (!s1.isEmpty() && !s2.isEmpty()) {
            Set<Integer> next = new HashSet<>();
            if (sign) {
                for (int cur : s1) {
                    if (s2.contains(cur)) {
                        return ans;
                    }
                    for (int nx : adj[cur]) {
                        if (!vis1.contains(nx)) {
                            next.add(nx);
                            vis1.add(nx);
                        }
                    }
                }
                s1 = next;
            } else {
                for (int cur : s2) {
                    if (s1.contains(cur)) {
                        return ans;
                    }
                    for (int nx : adj[cur]) {
                        if (!vis2.contains(nx)) {
                            next.add(nx);
                            vis2.add(nx);
                        }
                    }
                }
                s2 = next;
            }
            ans++;
            sign = !sign;
        }
        throw new RuntimeException("00???????????????00");
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> s1 = new HashSet<>(), s2 = new HashSet<>();
        int a = nounToSet.get(nounA).id, b = nounToSet.get(nounB).id;
        s1.add(a);
        s2.add(b);
        int[] edgeTo1 = new int[adj.length];
        Arrays.fill(edgeTo1, -1);
        int[] edgeTo2 = new int[adj.length];
        Arrays.fill(edgeTo2, -1);
        Set<Integer> vis1 = new HashSet<>();
        Set<Integer> vis2 = new HashSet<>();
        boolean sign = true;
        while (!s1.isEmpty() && !s2.isEmpty()) {
            Set<Integer> next = new HashSet<>();
            if (sign) {
                for (int cur : s1) {
                    if (s2.contains(cur)) {
                        String ans = Integer.toString(cur);
                        for (int l = edgeTo1[cur]; l != -1; l = edgeTo1[l]) {
                            ans = l + "-" + ans;
                        }
                        for (int r = edgeTo2[cur]; r != -1; r = edgeTo2[r]) {
                            ans = ans + "-" + r;
                        }
                        return ans;
                    }
                    for (int nx : adj[cur]) {
                        if (!vis1.contains(nx)) {
                            next.add(nx);
                            vis1.add(nx);
                        }
                        if (edgeTo1[nx] == -1) {
                            edgeTo1[nx] = cur;
                        }
                    }
                }
                s1 = next;
            } else {
                for (int cur : s2) {
                    if (s1.contains(cur)) {
                        String ans = Integer.toString(cur);
                        for (int l = edgeTo1[cur]; l != -1; l = edgeTo1[l]) {
                            ans = l + "-" + ans;
                        }
                        for (int r = edgeTo2[cur]; r != -1; r = edgeTo2[r]) {
                            ans = ans + "-" + r;
                        }
                        return ans;
                    }
                    for (int nx : adj[cur]) {
                        if (!vis2.contains(nx)) {
                            next.add(nx);
                            vis2.add(nx);
                        }
                        if (edgeTo2[nx] == -1) {
                            edgeTo2[nx] = cur;
                        }
                    }
                }
                s2 = next;
            }
            sign = !sign;
        }
        throw new RuntimeException("11???????????????11");
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}