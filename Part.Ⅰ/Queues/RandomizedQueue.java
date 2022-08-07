/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] arr; // resizing array

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        arr = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if(item == null) {
            throw new IllegalArgumentException();
        }
        if (size == arr.length) {
            // resize
            Item[] tmp = (Item[]) new Object[arr.length << 1];
            for(int i = 0; i < arr.length; i++) {
                tmp[i] = arr[i];
            }
            arr = tmp;
        }
        arr[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if(isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int rd = StdRandom.uniform(0, size--);
        Item ret = arr[rd];
        arr[rd] = arr[size];
        arr[size] = null;
        if (size <= (arr.length >> 2)) {
            //shrink
            int len = arr.length >> 1;
            if (len == 0) {
                len = 1;
            }
            Item[] tmp = (Item[]) new Object[len];
            for(int i = 0; i < tmp.length; i++) {
                tmp[i] = arr[i];
            }
            arr = tmp;
        }
        return ret;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if(isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int rd = StdRandom.uniform(0, size);
        return arr[rd];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<Item> {

        private int len;

        private Item[] inArr;

        public Iter() {
            len = size;
            inArr = (Item[]) new Object[len];
            for(int i=0; i < len; i++) {
                inArr[i] = arr[i];
            }
        }

        public boolean hasNext() {
            return len != 0;
        }

        public Item next() {
            if(!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            int rd = StdRandom.uniform(0, len--);
            Item ret = inArr[rd];
            inArr[rd] = inArr[len];
            inArr[len] = ret;
            return ret;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        deque.enqueue("a");
        deque.enqueue("b");
        System.out.println(deque.size());
        System.out.println(deque.dequeue());
        System.out.println(deque.sample());
        System.out.println(deque.dequeue());
        System.out.println(deque.isEmpty());
    }

}
