package p2.sorts;

import java.util.Comparator;

import cse332.exceptions.NotYetImplementedException;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        pivot(array, 0, array.length, comparator);
    }

    /**
     * This method splits an array by two based on a pivot, then recursively
     * calls itself on each half until reaching arrays of length <= 2.
     * 
     * @param arr
     *            the array being sorted
     * @param begI
     *            the location of the first element
     * @param len
     *            len-1 is the location of the last element
     * @param comparator
     *            a way to sort
     */
    private static <E> void pivot(E[] arr, int begI, int len, Comparator<E> comparator) {
        // stop when there is only one element
        if (len - begI <= 1) {
            return;
        }
        int pivotLocal, i, j;
        E pivot;
        // chose one of three pivots
        pivotLocal = getPivot(arr, begI, len, comparator);

        // swap the pivot
        pivot = arr[pivotLocal];
        arr[pivotLocal] = arr[begI];
        arr[begI] = pivot;
        pivot = arr[begI];
        //set indices
        i = begI + 1;
        j = len - 1;
        // until these two meet
        while (i < j) {
            // move j left
            while ((comparator.compare(arr[j], pivot) > 0) && j > i) {
                j--;
            }
            // move i right
            while ((comparator.compare(arr[i], pivot) < 0) && i < j) {
                i++;
            }
            // swap
            E e = arr[i];
            arr[i] = arr[j];
            arr[j] = e;
        }

        // get pivot to correct location
        if (comparator.compare(arr[i], pivot) > 0) {
            //decrementing i here for edge case
            //of recursive call
            arr[begI] = arr[--i];
            arr[i] = pivot;
        }
        else {
            arr[begI] = arr[i];
            arr[i] = pivot;

        }
        // recursively call on the two subArrays
        pivot(arr, begI, (i), comparator);
        pivot(arr, (i + 1), len, comparator);

    }

    private static <E> int getPivot(E[] arr, int begI, int len,
            Comparator<E> comparator) {
        // chose one of three pivots
        E begE = arr[begI];
        E midE = arr[(len - 1) / 2];
        E lstE = arr[len - 1];
        if (comparator.compare(begE, lstE) > 0) {
            if (comparator.compare(begE, midE) < 0) {
                // lstE< begE < midE
                return begI;
            }
            else if (comparator.compare(midE, lstE) < 0) {
                // midE < lstE < bigE
                return len - 1;
            }
            else {
                // lstE <= (midE, bigE)
                return (len - 1) / 2;
            }
        }
        else if (comparator.compare(begE, midE) > 0) {
            // midE < bigE < lstE
            return begI;
        }
        else {
            // default is the middle
            return (len - 1) / 2;
        }
    }
}
