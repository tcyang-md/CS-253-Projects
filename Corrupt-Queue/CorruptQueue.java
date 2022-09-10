/* 
THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
A TUTOR OR CODE WRITTEN BY OTHER STUDENTS
- TIANEN CHRISTOPHER YANG
*/

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class CorruptQue<Item> implements Iterable<Item>
{
    // Helpful Linked List for storing the queue
    private class Node {
        public Node next, prev;
        public Item item;

        public Node(Item it) {
            this.prev = null;
            this.next = null;
            this.item = it;
        }

        // Instantiate a node while setting both its prev and next pointers
        public Node(Item it, Node prev, Node next) {
            this.prev = prev;
            this.next = next;
            this.item = it;
        }
    }

    private int N; // Number of items in the queue
    private Node head, tail; // Back and front of the corrupt queue, respectively

    public CorruptQue() {
        this.N = 0;
        this.head = null;
        this.tail = null;
    }

    // return the number of items
    public int size() {
        return N;
    }

    // true if empty, false otherwise
    public boolean isEmpty() {
        return size() == 0;
    }

    // add Item x to the back of this queue
    public void enqueue(Item x) {
        // FILL ME IN
        // "this" is the corrupt queue object
        // queue this at the index 0 in terms of space. head is last in terms of space
        
        // create a floating node with the value of the item
        Node curr = new Node(x, null, null);
        
        // if the queue is empty, make the item the head and tail of this linked list the only node we have
        if (this.isEmpty() == true)
        {
            this.head = curr;
            this.tail = curr;
        }
        // if the queue is not empty, add on to the end of the queue (tail)
        else 
        {
           // head <-> node <-> tail
           this.tail.next = curr;
           curr.prev = this.tail;
           this.tail = curr;
        }
        
        this.N++;
    }

    // barge into the line, adding Item x to the second place from the front (or the front if they're alone)
    public void cut(Item x) {
        // FILL ME IN
        Node curr = new Node(x);
        
        // empty case or one item case
        if (this.isEmpty() == true || this.N == 1) 
        {
            // enqueue the item
            this.enqueue(x);
        }
        else
        {
            // assign curr's previous and next
            curr.prev = this.head;
            curr.next = this.head.next;
            // second node's previous is no longer head
            this.head.next.prev = curr;
            // head's next node (new second node) is now current
            this.head.next = curr;
            this.N++;
        }
    }

    // return item removed from the front (end) of the queue
    public Item dequeue() throws NoSuchElementException {
        if (isEmpty() == true)
            throw new NoSuchElementException();

        // FILL ME IN
        
        if (N == 1) 
        {
           Node temp = this.head;
           this.head = null;
           this.tail = null;
           this.N--;
           return temp.item;
        }
        else
        {
            // copy of old head node 
            Node temp = this.head;
            // old head = second node
            this.head = this.head.next;
            // new head's previous is now null because it's the head 
            this.head.prev = null;
            this.N--;
            return temp.item;
        }
    }

    // internal iterator implementation
    public class Iter implements Iterator<Item> {
        private Node where;

        public Iter() {
            where = tail; // Assumes tail has the front of the queue. You can turn this around if you desire.
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item it = where.item;
            where = where.prev;
            return it;
        }

        public boolean hasNext() {
            return (where != null);
        }

    }

    // teturn Iterator as required by Iterable (from front to back).
    public Iterator<Item> iterator() {
        return new Iter();
    }

    // print contents of queue from front to back
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item it : this) {
            s.append (it.toString() + " ");
        }
        s.append ("\n"); // newline
        return s.toString();
    }

    // this method is used by HackerRank to read in operations
    public void process(char op, Item val) {
        if (op == 'e') // enqueue
           enqueue(val);
        else if (op == 'c') // cut
           cut(val);
        else if (op == 'd') // dequeue
           System.out.println (dequeue()); // ignore val
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        CorruptQue<Integer> cq = new CorruptQue<>();

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, n).forEach(nItr -> {
            try {
                char o = (char)bufferedReader.read();
                int k = 0;
                if (o != 'd') { // the enqueue operations 'e' and 'c' both take an argument
                    bufferedReader.skip(1); // eat the space
                    k = Integer.parseInt(bufferedReader.readLine().trim());
                } else {
                    bufferedReader.readLine();
                }
                cq.process(o, k);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}
