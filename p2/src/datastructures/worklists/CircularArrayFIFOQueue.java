package datastructures.worklists;

import java.util.Iterator;
import java.util.NoSuchElementException;

import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */


public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
		
	//what does this class need?
	/**keeps track of front and back of queue*/
	private int front, back;
	/**keep track of how much work is contained*/
	private int workCount;
	/**circular array*/
	private E [] workArray;
	 
	
    @SuppressWarnings("unchecked")
	public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        /**front and back start out at same location -> 0*/
        /**front is location OF FIFO, unless workCount = 0*/
        /**back is location FOR LILO*/
        this.front = 0;
        this.back = 0;
        this.workCount = 0;
        this.workArray = (E[]) new Comparable[capacity];
    }

    @Override
    public void add(E work) {
    	if(this.isFull()) {
    		throw new IllegalStateException();
    	}
    	/**Add to back*/
    	this.workCount ++;
    	this.workArray[back] = work;
    	/**watch out for this one*/
    	this.back = (this.back + 1) % this.capacity();
    }

    @Override
    public E peek() {
    	/**looking at FIFO element*/
    	if(this.hasWork()) {
    		return this.workArray[front];
    	}else {
    		throw new NoSuchElementException();
    	}
    }
    
    @Override
    public E peek(int i) {
    	if(!this.hasWork()) {
    		throw new NoSuchElementException();
    	}else if(i < 0 || i >= this.size() ) {
    		throw new IndexOutOfBoundsException();
    	}else {
    		//find that element
    		int location = (this.front + i) % this.capacity();
    		return this.workArray[location];
    	}
    }
    
    @Override
    public E next() {
    	if(!this.hasWork()) {
    		throw new NoSuchElementException();
    	}
    	/**Pull from front*/
    	E e = this.workArray[front];
    	this.front = (this.front + 1) % this.capacity();
    	this.workCount --;
    	return e;
    }
    
    @Override
    public void update(int i, E value) {
    	if(!this.hasWork()) {
    		throw new NoSuchElementException();
    	}else if(i < 0 || i >= this.size() ) {
    		throw new IndexOutOfBoundsException();
    	}else {
    		//find that element
    		int location = (this.front + i) % this.capacity();
    		this.workArray[location] = value;
    	}
    }
    
    @Override
    public int size() {
        return this.workCount;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void clear() {
       this.front = 0;
       this.back = 0;
       this.workCount = 0;
       this.workArray = (E[]) new Object[this.capacity()];
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.

        
            Iterator<E> otherIT = other.iterator();
            Iterator<E> thisIT = this.iterator();
            //Loop Inv - for every seen E in this.str, E.compare(other.E) == 0
            while(otherIT.hasNext() && thisIT.hasNext()) {
                int compared = thisIT.next().compareTo(otherIT.next());
                if(compared != 0) {
                    return Integer.signum(compared);
                }
            } 
            //do size check here
            if(this.size()<other.size()) {
                return -1;
            }else if(this.size()>other.size()) {
                return 1;
            }
//        }
        //Post condition - this.compare(other) = 0
        return 0;
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        }
        else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            //short-circuit 
            if(other.size() != this.size()) {
                return false;
            }
            //check every element
            Iterator<E> otherIT = other.iterator();
            Iterator<E> thisIT = this.iterator();
            //Loop Inv - for every E in in this.workArray seen, other has an equal element in
            //the same index position
            while(otherIT.hasNext()) {
                if(!otherIT.next().equals(thisIT.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i=this.size() -1; i>=0;i--){
            hash = 31*hash + this.workArray[i].hashCode();
        }
        return hash;
    }
    
    /*@Override
    public int hashCode() {
        int hash = 5381;

        for(int i=this.size() -1; i>=0;i--){
            hash = ((hash << 5) + hash) + this.workArray[i].hashCode();
        }
        return hash;
    }*/
    
    
}
