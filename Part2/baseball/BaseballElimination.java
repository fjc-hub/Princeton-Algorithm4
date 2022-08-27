import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

public class BaseballElimination {

    private final Map<String, Integer> map;
    private final String[] Names;
    private final int[] W;
    private final int[] L;
    private final int[] R;
    private final int[][] G;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        W = new int[n];
        L = new int[n];
        R = new int[n];
        G = new int[n][n];
        Names = new String[n];
        map = new HashMap<>(n);
        int idx = 0;
        for (int t = 0; t < n; t++) {
            Names[t] = in.readString();
            map.put(Names[t], idx);
            W[idx] = in.readInt();
            L[idx] = in.readInt();
            R[idx] = in.readInt();
            for (int i = 0; i < n; i++) {
                int tmp = in.readInt();
                G[idx][i] = tmp;
                G[i][idx] = tmp;
            }
            idx++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return map.size();
    }

    // all teams
    public Iterable<String> teams() {
        return map.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!map.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return W[map.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!map.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return L[map.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!map.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return R[map.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!map.containsKey(team1) || !map.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return G[map.get(team1)][map.get(team2)];
    }

    // auxiliary variable of method buildNetwork
    private int OutSourceCapacity;

    private FordFulkerson buildFordFulkerson(int x, Set<String> subSet) {
        OutSourceCapacity = 0;
        Map<Integer, String[]> IdToName = new HashMap<>();
        int n = this.numberOfTeams();
        int size = n+1 + (n-1)*(n-2)/2;
        FlowNetwork flowNetwork = new FlowNetwork(size);
        int s = size-2, t = size-1;
        for (int i = 0; i < n; i++) {
            if (i == x) {continue;}
            flowNetwork.addEdge(new FlowEdge(mapToIdx(i, x), t, W[x] + R[x] - W[i]));
        }
        for (int i = 0; i < n; i++) {
            if (i == x) {continue;}
            for (int j = i+1; j < n; j++) {
                if (j == x) {continue;}
                OutSourceCapacity += G[i][j];
                int idx = mapToIdx(i, j, x, n);
                flowNetwork.addEdge(new FlowEdge(s, idx, G[i][j]));
                flowNetwork.addEdge(new FlowEdge(idx, mapToIdx(i, x), Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(idx, mapToIdx(j, x), Double.POSITIVE_INFINITY));
                if (subSet != null) {
                    IdToName.put(idx, new String[]{Names[i], Names[j]});
                }
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
        if (subSet != null) {
            for (Map.Entry<Integer, String[]> entry : IdToName.entrySet()) {
                if (fordFulkerson.inCut(entry.getKey())) {
                    String[] strs = entry.getValue();
                    subSet.add(strs[0]);
                    subSet.add(strs[1]);
                }
            }
        }
        return fordFulkerson;
    }

    private int mapToIdx(int v, int x) {
        if (v > x) {
            return v-1;
        }
        return v;
    }

    private int mapToIdx(int v, int w, int x, int n) {
        // assert v < w
        n--;
        v = mapToIdx(v, x);
        w = mapToIdx(w, x);
        if (v == 0) {
            return n - 1 + w - v;
        }
        return (n-1) * (v+1) + w - v*(v+1)/2;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!map.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        if (trivialCheck(team) != null) {
            return true;
        }
        FordFulkerson fordFulkerson = buildFordFulkerson(map.get(team), null);
        // compare max flow
        // If some edges pointing from source are not full,
        // then there is no scenario in which team x can win the division
        return fordFulkerson.value() < OutSourceCapacity;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!map.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        String tmp = trivialCheck(team);
        if (tmp != null) {
//            return Arrays.asList(tmp);
            return Collections.singletonList(tmp);
        }
        Set<String> set = new HashSet<>();
        FordFulkerson fordFulkerson = buildFordFulkerson(map.get(team), set);
        if (fordFulkerson.value() >= OutSourceCapacity) {
            return null;
        }
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            if (entry.getKey().equals(team)) {
//                continue;
//            }
//            if (fordFulkerson.inCut()) {
//                set.add(entry.getKey());
//            }
//        }
        return set;
    }

    private String trivialCheck(String team) {
        int id = map.get(team);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(team)) {
                continue;
            }
            if (W[entry.getValue()] > W[id] + R[id]) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
