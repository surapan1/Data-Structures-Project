package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java for method
 * specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /*
     * Do not change the name of this field; the tests rely on it to work
     * correctly.
     */
    private E[] data;
    int theSize;
    private Comparator<E> comp;

    public MinFourHeap(Comparator<E> comp) {
        this.comp = comp;
        clear();
    }

    @Override
    public boolean hasWork() {
        return (theSize > 0);
    }

    @Override
    public void add(E work) {
        // if the size of the array is not big enough to add to, double the size
        // then add
        if (data.length == theSize) {
            data = doubleArray(data);
        }

        // increment the counter, add to the array, and then percolate up
        theSize++;
        data[theSize - 1] = work;
        percolateUp(theSize - 1);
    }

    @Override
    public E peek() {
        if (!this.hasWork()) {
            throw new NoSuchElementException();
        }
        return data[0];
    }

    @Override
    public E next() {
        if (!this.hasWork()) {
            throw new NoSuchElementException();
        }
        // remove the lowest value, then move the top nodes leaf to the node
        // then percolate down and decrement the counter
        E min = data[0];
        data[0] = data[theSize - 1];

        // addition 7/26
//        theSize--;
//         data[theSize-1] = null;

        percolateDown(0);
        theSize--;
        
        
        
        return min;
    }

    @Override
    public int size() {
        return theSize;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        data = (E[]) new Object[10];
        theSize = 0;
    }

    /**
     * An internal method to double the size of the array
     */
    @SuppressWarnings("unchecked")
    private E[] doubleArray(E[] original) {
        E[] newArray = (E[]) new Comparable[original.length * 2];
        for (int i = 0; i < original.length; i++) {
            newArray[i] = original[i];
        }
        return newArray;
    }

    /**
     * An internal method that 'percolates a value up' if it is less than its
     * parent
     */

    private void percolateUp(int index) {
        int parent;
        int tIndex = index;
        while (tIndex > 0 && (comp.compare(data[tIndex],
                data[(parent = (int) Math.floor((tIndex - 1) / 4))]) < 0)) {
            // swap
            E e = data[tIndex];
            data[tIndex] = data[parent];
            data[parent] = e;
            tIndex = parent;
        }

        // boolean check = false;
        // while (!check && index > 0) {
        // int parent = (int) Math.floor((index - 1) / 4);
        // // if(comp.compare(data[parent], data[index]) > 0) {
        // if (comp.compare(data[index], data[parent]) < 0) {
        // E temp = data[index];
        // data[index] = data[parent];
        // data[parent] = temp;
        // index = parent;
        // }
        // else {
        // check = true;
        // }
        // }
    }

    /**
     * An internal method that 'percolates a value down' if it is greater than
     * its child
     */
    private void percolateDown(int index) {

        boolean done = false;
        while (!done) {
            int i = (index * 4) + 1;
            int min = index;
            while (i <= theSize - 1 && i <= (index * 4) + 4) {
                // if(comp.compare(data[min], data[i]) > 0){
                if (comp.compare(data[i], data[min]) < 0) {
                    min = i;
                }
                i++;
            }
            // if(comp.compare(data[min], data[index]) < 0) {
            if (comp.compare(data[index], data[min]) > 0) {
                E minimumValue = data[min];
                data[min] = data[index];
                data[index] = minimumValue;
                index = min;
            }
            else {
                done = true;
            }
        }
    }

}
