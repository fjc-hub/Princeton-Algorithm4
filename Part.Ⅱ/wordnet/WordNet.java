
import edu.princeton.cs.algs4.In;

import java.util.*;

// WordNet must be a DAG
public class WordNet {

    private class Synset {
        int id;
        String[] words;

        public Synset(int id, String[] words) {
            this.id = id;
            this.words = words;
        }
    }

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
            String[] strs = in0.readLine().split(",");
            int id = Integer.valueOf(strs[0]);
            String[] words = strs[1].split(" ");
            idToSet.put(id, new Synset(id, words));
            for (String str : words) {
                if (nounToSet.get(str) == null) {
                    nounToSet.put(str, new ArrayList<>());
                }
                nounToSet.get(str).add(id);
            }
        }
        adj = new List[idToSet.size()];
        In in1 = new In(hypernyms);
        while (in1.hasNextLine()) {
            String[] ids = in1.readLine().split(",");
            Synset node = idToSet.get(Integer.valueOf(ids[0]));
            adj[node.id] = new ArrayList<>();
            for (int i = 1; i < ids.length; i++) {
                adj[node.id].add(Integer.valueOf(ids[i]));
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
        List<Integer> listA = nounToSet.get(nounA), listB = nounToSet.get(nounB);
        s1.addAll(listA);
        s2.addAll(listB);
        Set<Integer> vis1 = new HashSet<>(), vis2 = new HashSet<>();
        vis1.addAll(listA);
        vis2.addAll(listB);
        boolean sign = true;
        int ans = 0;
        while (!s1.isEmpty() && !s2.isEmpty()) {
            Set<Integer> next = new HashSet<>();
            if (sign) {
                for (int cur : s1) {
                    if (s2.contains(cur) || vis2.contains(cur)) {
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
        List<Integer> listA = nounToSet.get(nounA), listB = nounToSet.get(nounB);
        s1.addAll(listA);
        s2.addAll(listB);
        Set<Integer> vis1 = new HashSet<>(), vis2 = new HashSet<>();
        vis1.addAll(listA);
        vis2.addAll(listB);
        int[] edgeTo1 = new int[adj.length];
        Arrays.fill(edgeTo1, -1);
        int[] edgeTo2 = new int[adj.length];
        Arrays.fill(edgeTo2, -1);
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
        String str = "sd sdad dasd sd";
        String[] arr = str.split(" ");
        System.out.println(arr.length);
        for (String s : arr) {
            System.out.println(s);
        }
    }
}