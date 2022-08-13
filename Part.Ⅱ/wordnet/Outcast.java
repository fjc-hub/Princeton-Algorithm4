public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] sum = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                if (j == i) {
                    continue;
                }
                if (i == 1 && j == 2) {
                    System.out.println(i);
                }
                sum[i] += wordNet.distance(nouns[i], nouns[j]);
            }
        }
        int min = 0;
        for (int i = 0; i < nouns.length; i++) {
            if (sum[i] < sum[min]) {
                min = i;
            }
        }
        return nouns[min];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}