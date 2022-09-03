import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class MoveToFront {

    private static final int RADIX = 256;

    private static final class LinkedNode {
        char val;
        LinkedNode next;

        public LinkedNode() {
        }

        public LinkedNode(char val) {
            this.val = val;
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        try{
            LinkedNode head = new LinkedNode();
            LinkedNode pre = head;
            for (int i = 0; i < RADIX; i++) {
                char ch = (char) i;
                LinkedNode node = new LinkedNode(ch);
                pre.next = node;
                pre = node;
            }
            while (!BinaryStdIn.isEmpty()) {
                char ch = BinaryStdIn.readChar(8);
                LinkedNode point = head.next;
                LinkedNode p = head;
                int idx = 0;
                while (point != null && point.val != ch) {
                    p = point;
                    idx++;
                    point = point.next;
                }
                // assert p != null and point != null
                BinaryStdOut.write(idx, 8);
                p.next = point.next;
                point.next = head.next;
                head.next = point;
            }
        } finally {
            BinaryStdIn.close();
            BinaryStdOut.close();
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        try{
            LinkedNode head = new LinkedNode();
            LinkedNode pre = head;
            for (int i = 0; i < RADIX; i++) {
                char ch = (char) i;
                LinkedNode node = new LinkedNode(ch);
                pre.next = node;
                pre = node;
            }
            while (!BinaryStdIn.isEmpty()) {
                int idx = BinaryStdIn.readChar(8);
                LinkedNode point = head.next;
                LinkedNode p = head;
                while (idx-- > 0) {
                    p = point;
                    point = point.next;
                }
                // assert p != null and point != null
                BinaryStdOut.write(point.val, 8);
                p.next = point.next;
                point.next = head.next;
                head.next = point;
            }
        } finally {
            BinaryStdIn.close();
            BinaryStdOut.close();
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            encode();
        } else if ("+".equals(args[0])) {
            decode();
        }
    }

}