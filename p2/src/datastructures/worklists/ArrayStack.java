package datastructures.worklists;

import java.util.NoSuchElementException;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
	
	//what does this class need to operate?
	
	/**Array which holds the work*/
	private E[] workArray;
	/**workArray index of the most recently added work*/
	private int LIFO;
	/**Default array/stack size*/
	private final int defaultStackSize = 10;
	

	//Everything is an object
    @SuppressWarnings("unchecked")
	public ArrayStack() {
		this.workArray = (E[]) new Object[defaultStackSize];
		//no initial work -> no initial accessing
		this.LIFO = -1;
    }

    @Override
    public void add(E work) {
    	//check size
    	if(LIFO >= workArray.length - 1) {
    		increaseStackSize();
    	}
    	//adding to the back, increment counter beforehand
		this.workArray[++LIFO] = work;
    }

    /**
     * Helper method to double the size of the stack
     * @modifies this.workArray
     * @effects pre-method: i = this.workArray.length \n
     * post-method: this.workArray.length = 2*i
     */
    @SuppressWarnings("unchecked")
	private void increaseStackSize() {
		int curSize = this.workArray.length;
		int newSize = curSize * 2;
		E[] newArray = (E[]) new Object[newSize];
		//copy old array
		for(int i = 0; i < curSize;i++) {
			newArray[i] = this.workArray[i];
		}
		this.workArray = newArray;		
	}

	@Override
    public E peek() {
		if(this.hasWork()) {
			return this.workArray[LIFO];
		}else {
			throw new NoSuchElementException();
		}
    }
	
	//testing -> make sure LIFO == -1 after pulling last element

    @Override
    public E next() {
    	if(this.hasWork()) {
    		//need to remove element from array
    		E e = this.workArray[LIFO];
    		//null out item?
    		this.workArray[LIFO] = null;
    		//decrease our stack's position -> pulling from back
    		LIFO --;
    		return e;
    	}else {
    		throw new NoSuchElementException();
    	}
    }

    @Override
    public int size() {
    	//workArray.length won't always work
    	//add one to LIFO counter
    	int size = LIFO + 1;
    	return size;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void clear() {
    	//reset LIFO and workArray
    	this.workArray = (E[]) new Object[defaultStackSize];
    	this.LIFO = -1;
    }
}
