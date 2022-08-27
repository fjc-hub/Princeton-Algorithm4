/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        while(!StdIn.isEmpty()) {
            deque.enqueue(StdIn.readString());
        }
        for(int i = 0; i < k; i++) {
            System.out.println(deque.dequeue());
        }
    }
}
