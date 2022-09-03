public class CircularSuffixArray {

    private static final int RADIX = 256;

    private final int len;
    private final int[] index; // map index of sorted array to index of original array

    // circular suffix array of s
    // LSD radix sort algorithm to sort string
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        len = s.length();
        index = new int[len];
//        // generate all suffixes by String.substring and concatenate (too slow, 输入较大二进制文件会成为bottleneck)
//        String[] original = new String[len];
//        for (int i = 0; i < len; i++) {
//            index[i] = i;
//            original[i] = s.substring(i) + s.substring(0, i); // O(string.length)
//        }

        // using a single starting index to represent suffix
        int[] startIndex = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = i;
            startIndex[i] = i;
        }

        // string radix sort (every element is length-fixed in suffixes array)
        int[] aux = new int[len];
        for (int i = len-1; i >= 0; i--) { // loop columns
            // sort column i
            int[] count = new int[RADIX+1];
            for (int v : index) {
                count[s.charAt((i + startIndex[v]) % len)+1]++;
            }
            for (int j = 1; j < count.length; j++) {
                count[j] += count[j-1];
            }
            for (int v : index) {
                int idx = s.charAt((i + startIndex[v]) % len);
                aux[count[idx]] = v;
                count[idx]++;
            }
            for (int j = 0; j < index.length; j++) {
                index[j] = aux[j];
            }
        }
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String str = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(str);
        System.out.println(csa.length());
        for (int i = 0; i < str.length(); i++) {
            System.out.print(csa.index(i)+", ");
        }
        System.out.println();
    }

}