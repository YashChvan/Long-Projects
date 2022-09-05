
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;
    
    // construct an empty deque
    public Deque()
    {
        // Node temp = new Node(null);
        // first = temp;
        // last = temp;
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null) throw new IllegalArgumentException();
        
        // if(first.item == null && last.item == null)
        // {
        //     Node temp = new Node(item);
        //     first = temp;
        //     last = temp;
        // }
        // else
        // {
        //     Node temp = new Node(item,first,null);
        //     first = temp;
        // }
        
        Node prevfirst = first;
        first = new Node(item);
        first.next = prevfirst;
        first.previous = null;
        if(isEmpty())
        {
            last = first;
        }
        else
        {
            prevfirst.previous = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null) throw new IllegalArgumentException();

        // if(first.item == null && last.item == null)
        // {
        //     Node temp = new Node(item);
        //     first = temp;
        //     last = temp;
        // }
        // else
        // {
        //     Node temp = new Node(item,null,last);
        //     last = temp;
        // }
        
        Node prevlast = last;
        last = new Node(item);
        last.previous = prevlast;
        last.next = null;

        if(isEmpty()) 
        {
            first = last;
        }
        else
        {
            prevlast.next = last;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if(size == 0) throw new NoSuchElementException();

        Item temp = first.item;
        
        // if(first == last)
        // {
        //     first.item = null;
        // }
        // else
        // {
        //     Node t = first;
        //     first = first.next;
        //     first.previous = null;
        //     t.next = null;
        // }
        if(size == 1)
        {
            first = null;
            last = null;
        }
        else
        {
            Node t = first;
            first = first.next;
            first.previous = null;
            t.next = null;
        }
        size--;
        return temp;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if(size == 0) throw new NoSuchElementException();
        
        Item temp = last.item;

        // if(first == last)
        // {
        //     first.item = null;
        // }

        // else
        // {
        //     Node t = last;
        //     last = last.previous;
        //     last.next = null;
        //     t.previous = null;
        // }
        if(size == 1)
        {
            first = null;
            last = null;
        }
        else
        {
            Node t = last;
            last = last.previous;
            last.next = null;
            t.previous = null;
        }
        size--;
        return temp;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    // debugging site
    public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();

        System.out.println("IS EMPTY: " + deck.isEmpty());

        for (int i = 0; i < 10; i++) {
            deck.addFirst(i);
            System.out.println("SIZE: " + deck.size());
            System.out.println("IS EMPTY: " + deck.isEmpty());
        }

        System.out.println("Elements 0-9 added. We should have seen 1 to 10 printed");

        for (Integer i : deck) {
            System.out.println(i);
        }

        System.out.println("Finished iterating over the iterator. Elements should appear from 9 to 0.");

        for (int i = 0; i < 10; i++) {
            System.out.println(deck.removeLast());
            System.out.println("IS EMPTY: " + deck.isEmpty());
            System.out.println("Deck size: " + deck.size());
        }

        System.out.println("Elements 0-9 removed. They should appear from 0 to 9.");

        for (int i = 0; i < 10; i++) {
            deck.addLast(i);
            System.out.println("IS EMPTY: " + deck.isEmpty());
            System.out.println("Deck size: " + deck.size());
        }

        System.out.println("Elements 0-9 added.");

        for (Integer i : deck) {
            System.out.println(i);
        }

        System.out.println("Finished iterating over the iterator. Elements should appear from 0 to 9");

        for (int i = 0; i < 10; i++) {
            System.out.println(deck.removeFirst());
            System.out.println("IS EMPTY: " + deck.isEmpty());
            System.out.println("Deck size: " + deck.size());
        }

        System.out.println("Elements 0-9 removed. Elements should appear from 0 to 9");

    }

    private class Node
    {
        Item item;
        Node next;
        Node previous;

        Node(Item item)
        {
            this.item = item;
            this.next = null;
            this.previous = null;
        }
        Node(Item item, Node next, Node previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }
    }

    private class DequeIterator implements Iterator<Item>
    {
        Node current = first;

        public  boolean hasNext()
        {
            return current.next != null;
        }

        public Item next()
        {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            Item temp = current.item;
            current = current.next;

            return temp;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
