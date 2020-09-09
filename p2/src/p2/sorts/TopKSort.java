package p2.sorts;

import java.util.Comparator;

import cse332.datastructures.containers.Item;
import cse332.interfaces.worklists.PriorityWorkList;
import datastructures.worklists.MinFourHeap;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        int i;
        PriorityWorkList<E> heap = new MinFourHeap<E>(comparator); 
        //get the first k elements into the heap
        for(i = 0;i < k && i < array.length;i++) {
            heap.add(array[i]);
        }
        //Loop Inv - for every element, e, seen IN THIS LOOP
        // if compare(e,heap.peek) > 1:
        //the min val of the heap has been removed and e has been inserted.
        for(;i < array.length;i++) {
          E nxt = array[i];
          //if the next element is bigger than the min heap val, replace
          if(comparator.compare(nxt, heap.peek()) > 0) {
              heap.next();
              heap.add(nxt);
          }
        }
        //remove elements from heap into array
        i = 0;
        while(heap.hasWork()) {
            array[i++] = heap.next();
        }
        //now back fill the array with null
        while(i<array.length) {
            array[i++] = null;
         }
    }
//    public static <K,V> void sort(Item<K,V>[] array, int k, Comparator<Item<K,V>> comparator) {
//        
//    }
}
