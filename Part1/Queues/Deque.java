/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private class Node<G> {
        G val;
        Node<G> prev;
        Node<G> next;
    }

    private int size;
    private Node<Item> head;
    private Node<Item> tail;

    // construct an empty deque
    public Deque() {
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head.next == tail;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size++;
        Node<Item> v = new Node<>();
        v.val = item;
        Node<Item> tmp = head.next;
        head.next = v;
        v.prev = head;
        v.next = tmp;
        tmp.prev = v;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size++;
        Node<Item> v = new Node<>();
        v.val = item;
        Node<Item> tmp = tail.prev;
        tail.prev = v;
        v.prev = tmp;
        v.next = tail;
        tmp.next = v;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        size--;
        Node<Item> ret = head.next;
        Node<Item> tmp = head.next.next;
        head.next = tmp;
        tmp.prev = head;
        ret.next = null;
        ret.prev = null;
        return ret.val;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        size--;
        Node<Item> ret = tail.prev;
        Node<Item> tmp = tail.prev.prev;
        tail.prev = tmp;
        tmp.next = tail;
        ret.next = null;
        ret.prev = null;
        return ret.val;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<Item> {

        Node<Item> cursor;

        public Iter() {
            this.cursor = head;
        }

        public boolean hasNext() {
            return cursor.next != tail;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Node<Item> ret = cursor.next;
            cursor = cursor.next;
            return ret.val;
        }

        public void remove() {
            throw new  UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("a");
        deque.addLast("b");
        deque.addLast("c");
        System.out.println(deque.size);
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.isEmpty());
        for(Iterator<String> iter = deque.iterator(); iter.hasNext();) {
            System.out.println(iter.next());
        }
    }
}
