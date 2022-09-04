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

        // using a single starting index to represent suffix
        int[] startIndex = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = i;
            startIndex[i] = i;
        }

        // string radix sort (MSD algorithm)
        int[] aux = new int[len];
        msd_sort(s, startIndex, 0, 0, index.length-1, aux);
    }

    // this MSD implementation can not be capable of variable-length string
    private void msd_sort(String str, int[] startIndex, int d, int x, int y, int[] aux) {
        // assert x, y in range of index array
        if (x >= y || str.length() <= d) {
            return ;
        }
        int[] count = new int[RADIX+1];
        for (int i = x; i <= y; i++) {
            count[str.charAt((startIndex[index[i]] + d) % str.length()) + 1]++;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i-1];
        }
        for (int i = x; i <= y; i++) {
            int ch = str.charAt((startIndex[index[i]] + d) % str.length());
            aux[count[ch]++] = index[i];
        }
        for (int i = x; i <= y; i++) {
            index[i] = aux[i-x];
        }
        // recursively msd_sort
        int start = -1, end = 0;
        for (int i = 0; i < count.length; i++) {
            start = end;
            end = count[i];
            if (start < end) { // simple pruning
                msd_sort(str, startIndex, d+1, x + start, x + end - 1, aux);
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
//        String str = "ABRACADABRA!";
//        CircularSuffixArray csa = new CircularSuffixArray(str);
//        System.out.println(csa.length());
//        for (int i = 0; i < str.length(); i++) {
//            System.out.print(csa.index(i)+", ");
//        }
//        System.out.println();

        String str = "491a00341600053b3e07";
        String str2 = "00341600053b3e07491a";
        String str5 = "00053b3e07491a003416";
        String result = new String();
        char[] charArray = str.toCharArray();
        for(int i = 0; i < charArray.length; i=i+2) {
            String st = ""+charArray[i]+""+charArray[i+1];
            char ch = (char)Integer.parseInt(st, 16);
            result = result + ch;
        }
        CircularSuffixArray csa = new CircularSuffixArray(result);
        System.out.println(csa.length());
        for (int i = 0; i < result.length(); i++) {
            System.out.print(csa.index(i)+", ");
        }
        System.out.println();
    }

}