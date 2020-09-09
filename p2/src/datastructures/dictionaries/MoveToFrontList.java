package datastructures.dictionaries;

//import java.util.Iterator;

import cse332.datastructures.containers.*;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;

/**
 * 
 * A MoveToFrontList implementation. Items are inserted into the front of the
 * list. If an item is referenced, it is moved to the front of the list. No
 * other ordering is guaranteed. Deletion is not allowed. An iterator is
 * provided, iterates over the list as it currently exists, starting at the
 * front.
 * 
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    private MTFNode front;

    public MoveToFrontList() {
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        V rval;
        if (key == null) {
            throw new IllegalArgumentException();
        }
        // rather than possibly make in two locations
        MTFNode newNode = new MTFNode(key, value, null);
        // circumvent patching issues by doing this check
        if (this.size == 0) {
            // create the root
            this.front = newNode;
            this.size++;
            return null;
        }else if(this.front.key.equals(key)) {
            rval = this.front.value;
            this.front.value = value;
            return rval;
        }
        
        // findNode should patch
        MTFNode n = findNode(key, this.front);
        if (n == null) {
            // not found
            newNode.next = this.front;
            this.front = newNode;
            this.size++;
            return null;
        }
        // we did have a match
        rval = n.value;
        n.value = value;
        return rval;
    }

    /**
     * Private method to remove a MTFNode and insert at front while maintaining
     * pointers to all nodes;
     * 
     * @param prevNode
     *            The MTFNode which will patch to curNode.next
     * @param curNode
     *            The MTFNode which will be removed from its location and moved
     *            to the front.
     * @modifies this
     * @effects post method: curNode == front and prevNode.next==(pre method
     *          curNode.next)
     */
    private void patchHole(MTFNode prevNode, MTFNode curNode) {
        prevNode.next = curNode.next;
        curNode.next = front;
        front = curNode;
    }

    /**
     * Private method to search for a MTFNode which contains the passed key.
     * Used because insert and find are quite similar.
     * 
     * @param key
     *            the key being searched for
     * @requires node != null
     * @return a MTFNode array of size 2, with [0] = (prevNode whose
     *         .next.data.key == key) or null if no match found,[1] = (curNode
     *         which == prevNode.next)
     */
    private MTFNode findNode(K key, MTFNode node) {

        MTFNode prevNode = node;
        MTFNode curNode = node.next;

        while (curNode != null) {
            if (curNode.key.equals(key)) {
                patchHole(prevNode, curNode);
                return curNode;
            }
            prevNode = curNode;
            curNode = curNode.next;
        }
        // postcondition curNode == null, i.e., key !exist in this
        return curNode;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            return null;
        }
        else if (this.front.key.equals(key)) {
            // don't need to manipulate structure
            return this.front.value;
        }
        MTFNode node = findNode(key, this.front);
        if (node == null) {
            // value wasn't found
            return null;
        }
        else {
            return node.value;
        }
    }

    // @Override
    public MTFIterator iterator() {
        return new MTFIterator();
    }

    public class MTFIterator extends SimpleIterator<Item<K, V>> {
        private MTFNode current;

        public MTFIterator() {
            if (MoveToFrontList.this.front != null) {
                this.current = MoveToFrontList.this.front;
            }
        }

        @Override
        public Item<K, V> next() {
            Item<K, V> item = this.current;
            this.current = this.current.next;
            return item;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

    }

    /**
     * This is a single-linked node.
     */
    public class MTFNode extends Item<K, V> {
        public MTFNode next;

        public MTFNode(K key, V val, MTFNode n) {
            super(key, val);
            next = n;
        }
    }

}
