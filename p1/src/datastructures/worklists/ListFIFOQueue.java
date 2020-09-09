package datastructures.worklists;

import java.util.NoSuchElementException;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
	
    
    private int theSize;
    private Node<E> first;
    private Node<E> last;
    
    public ListFIFOQueue() {
    	//constructor creates the first and last node of LL
    	//and clears out the data and resets the pointers
        clear();
    }

    @Override
    public void add(E work) {
    	//if adding to an empty LL (edge case)
    	//add data to the first node, and point to the last node
    	if(first.data == null) {
    		first = new Node<E>(work, null, null);
        	first.next = last;
    	}
    	//if adding to a LL that only has 1 non-null node (another edge case)
    	//add data to the last node
    	else if(last.data == null){
    		last.data = work;
    	}
    	//otherwise, add a new node at the end, and change the previous
    	//last nodes pointer to new last node
    	else {
    		Node<E> tempNode = new Node<E>(work,last,null);
    		last.next = tempNode;
    		last = tempNode;
    	}
    	
    	theSize++;
    	
    }

    @Override
    public E peek() {
    	if(first.data == null)
    		throw new NoSuchElementException();
    	else {
    		return first.data;
    	}
    }

    @Override
    public E next() {
    	if(first.data == null || theSize == 0)
    		throw new NoSuchElementException();

    	//create a temp generic to help return next
    	//default to store first node's data
		E tempE = first.data;

		//if there is only 1 valid node, return data
		if(theSize == 1) {
			clear();
			return tempE;
		}
		
		//otherwise change the only second node to be new first node
		//remove pointer to next node from the original first node
		//change size of LL
		first = first.next;
		first.prev = null;
		theSize--;
		
		return tempE;

    }

    @Override
    public int size() {
    	return theSize;
    }

    @Override
    public void clear() {
    	//create two empty nodes for first and last node of a LL
    	//and correctly points first to last node and vice-vera
    	first = new Node<E>( null, null, null );
        last = new Node<E>( null, first, null );
        first.next = last;

        theSize = 0;
    }
    
    /**
     * This is the doubly-linked list node.
     */
    private static class Node<E>
    {
        public E data;
        public Node<E>   prev;
        public Node<E>   next;
        
        public Node(E d, Node<E> p, Node<E> n)
        {
            data = d;
            prev = p;
            next = n;
        }
    }
}
