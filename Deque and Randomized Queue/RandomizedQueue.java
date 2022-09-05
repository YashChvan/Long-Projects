import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private int size, current;
    private Item RQ[];
    // construct an empty randomized queue
    public RandomizedQueue()
    {
        size =4;
        current =0;
        // numberofnull = 2;
        RQ = (Item[]) new Object[size];
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return current == 0;
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return current;
    }

    // add the item
    public void enqueue(Item item)
    {
        if(item == null) throw new IllegalArgumentException();

        RQ[current] = item;
        current++;
        if(current == size)
        {
            RQ = increasecap(RQ);
        }
        
    }

    // remove and return a random item
    public Item dequeue()
    {
        if(size() == 0) throw new NoSuchElementException();

        int index = StdRandom.uniform(1, current+1) -1;
        int lastindex = current-1;
        Item temp = RQ[index];
        RQ[index] = RQ[lastindex];
        current--;
        if(current <= size/4 && size>4)
        {
            RQ = decreasecap(RQ);
        } 
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        int index = StdRandom.uniform(1, current+1) -1;
        return RQ[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    private Item[] increasecap(Item[] a)
    {
        Item[] newRQ = (Item[]) new Object[size*2];
        for(int i=0 ; i<size ; i++)
        {
            newRQ[i] = RQ[i];
        }
        size *= 2;
        return newRQ;
    }

    private Item[] decreasecap(Item[] a)
    {
        Item[] newRQ = (Item[]) new Object[size/4];
        for(int i=0 ; i<size/4 ; i++)
        {
            newRQ[i] = RQ[i];
        }
        size = size/4;
        return newRQ;
    }

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        RandomizedQueue<Item> copy;
        RandomizedQueueIterator()
        {
            copy = new RandomizedQueue<>();
            for(int i=0 ; i<current ; i++)
            {
                copy.enqueue(RQ[i]);
            }
        }

        public boolean hasNext()
        {
            return copy.size() > 0;
        }

        public Item next()
        {
            if(! hasNext()) throw new NoSuchElementException();

            return copy.dequeue();
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for(int i=0 ; i<10 ; i++)
        {
            rq.enqueue(i);
        }
        for(int i=0 ; i<10 ; i++)
        {
            System.out.println(rq.dequeue());
        }
    }
}
