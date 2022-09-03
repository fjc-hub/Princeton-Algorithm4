import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int RADIX = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        try {
            int first = -1;
            String str = BinaryStdIn.readString();
            int len = str.length();
            char[] t = new char[len];
            CircularSuffixArray csa = new CircularSuffixArray(str);
            for (int i = 0; i < str.length(); i++) {
                int idx = csa.index(i);
                t[i] = str.charAt((len - 1 + idx) % len);
                if (idx == 0) {
                    first = i;
                }
            }
            BinaryStdOut.write(first);
            for (char ch : t) {
                BinaryStdOut.write(ch, 8);
            }
        } finally {
            BinaryStdIn.close();
            BinaryStdOut.close();
        }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        try {
            int first = BinaryStdIn.readInt();
            char[] t = BinaryStdIn.readString().toCharArray();
            int[] next = new int[t.length];
            // RADIX SORT for char[] t
            int[] count = new int[RADIX+1];
//            char[] firstCol = new char[t.length];
            for (char ch : t) {
                count[ch + 1]++;
            }
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i-1];
            }
            for (int i = 0; i < t.length; i++) {
                char ch = t[i];
//                firstCol[count[ch]++] = ch;
                // quick solution to calculate the next array
                next[count[ch]++] = i;
            }
            // calculate the next array
            // (better solution refer to https://github.com/yixuaz/algorithm4-princeton-cos226/blob/996c842ed69f350a47cafe816614b0eb1bd43e51/princetonSolution/src/part2/week5/project/BurrowsWheeler.java#L38)
//          // slow solution
//            Map<Integer, Queue<Integer>> map = new HashMap<>(t.length);
//            for (int i = 0; i < firstCol.length; i++) {
//                Queue<Integer> queue = map.getOrDefault((int) firstCol[i], new ArrayDeque<>());
//                queue.offer(i); // ascending
//                map.put((int) firstCol[i], queue);
//            }
//            for (int i = 0; i < t.length; i++) {
//                Queue<Integer> queue = map.get((int) t[i]);
//                int idx = queue.poll();
//                next[idx] = i;
//            }
            // get result
            first = next[first];
            for (int i = 0; i < t.length; i++) {
                BinaryStdOut.write(t[first], 8);
                first = next[first];
            }
        } finally {
            BinaryStdIn.close();
            BinaryStdOut.close();
        }
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else if ("+".equals(args[0])) {
            inverseTransform();
        }
    }

}