package datastructures.dictionaries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.BString;
import cse332.interfaces.trie.TrieMap;
import cse332.types.AlphabeticString;
import datastructures.worklists.ArrayStack;

/**
 * See cse332/interfaces/trie/TrieMap.java and
 * cse332/interfaces/misc/Dictionary.java for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
	@SuppressWarnings("unchecked")
	private final A emptyString = (A) "";
	public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
		public HashTrieNode() {
			this(null);
		}

		public HashTrieNode(V value) {
			this.pointers = new HashMap<A, HashTrieNode>();
			this.value = value;
		}

		@Override
		public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
			return pointers.entrySet().iterator();
		}
	}

	public HashTrieMap(Class<K> KClass) {
		super(KClass);
		this.root = new HashTrieNode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V insert(K key, V value) {
		// check null
		if (key == null || value == null) {
			throw new IllegalArgumentException();
		}
		/** Current 'work' from key.str. Start with empty string for ease */
		A curKey = emptyString;
		/** Iterates over key.str -> FixedSizeFIFOWorkList */
		Iterator<A> keyIT = key.iterator();
		/** start with root as current node */
		HashTrieNode curNode = (HashTrieNode) this.root;
		// Continue until all work in key is done
			while (keyIT.hasNext()) {
				curKey = keyIT.next();
				// kind of ugly, but scope is causing issues
				HashTrieNode temp = getNextNode(curKey, curNode);
				if (temp == null) {
					// lets create a new pointer entry in current node
					HashTrieNode newNode = new HashTrieNode(null);
					// map it
					curNode.pointers.put(curKey, newNode);
					// move forward one node
					curNode = newNode;
				} else {
					curNode = temp;
				}
			}
		// get old value, insert new
		V rVal = curNode.value;
		curNode.value = value;
		// watch out for size
		this.size++;
		return rVal;
	}

	/**
	 * 
	 * @param curKey  the key which curNode.pointers might contain
	 * @param curNode the current HashTrieMap node being searched over
	 * @return HashTrieNode which curKey points to, null otherwise
	 */
	@SuppressWarnings("unchecked")
	private HashTrieNode getNextNode(A curKey, HashTrieNode curNode) {
		/** Loop Inv - for every entry e seen, e.key != curA */
		for (Entry<A, ?> e : curNode) {
//			if (curKey.equals(e.getKey())) {
			if(curKey==e.getKey() || curKey.equals(e.getKey())) {
				// we have a match, update curNode
				return (HashTrieNode) e.getValue();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V find(K key) {
		/** start with root as current node */
		HashTrieNode curNode = (HashTrieNode) this.root;
		if (key == null) {
			throw new IllegalArgumentException();
		}
		/** Current 'work' from key.str. Start with empty string for ease */
		A curKey = emptyString;
		/** Iterates over key.str -> FixedSizeFIFOWorkList */
		Iterator<A> keyIT = key.iterator();
		// Loop Inv - for all keys seen so far, curNode.pointers.contains(key)
			while (keyIT.hasNext()) {
				curKey = keyIT.next();
				HashTrieNode temp = getNextNode(curKey, curNode);
				if (temp == null) {
					// value wasn't found
					return null;
				} else {
					// make progress
					curNode = temp;
				}
			}
		// post condition - this contains key
		return curNode.value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean findPrefix(K key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		/** start with root as current node */
		HashTrieNode curNode = (HashTrieNode) this.root;
		/** Iterates over key.str -> FixedSizeFIFOWorkList */
		Iterator<A> keyIT = key.iterator();
		/** Current 'work' from key.str. Start with empty string for ease */
		A curKey = emptyString;
			// Loop Inv - for every key seen so far, curNode.pointers.contains(key)
			while (keyIT.hasNext()) {
				curKey = keyIT.next();
				HashTrieNode temp = getNextNode(curKey, curNode);
				if (temp == null) {
					// value wasn't found
					return false;
				} else {
					// make progress
					curNode = temp;
				}
			}
		// Post condition - prefix contained
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(K key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		/** Make shift map */
		ArrayStack<A> keyMap = new ArrayStack<>();
		ArrayStack<HashTrieNode> nodeMap = new ArrayStack<>();
		/** start with root as current node */
		HashTrieNode curNode = (HashTrieNode) this.root;
		/** Iterates over key.str -> FixedSizeFIFOWorkList */
		Iterator<A> keyIT = key.iterator();
		/** Current 'work'*/
		A curKey;
		/** keep track of whether key was empty string */
		boolean keyIsEmptyString = (key.size() == 0 ? true : false);
		// Loop Inv - for all keys seen so far, curNode.pointers.contains(key)
		// and keyMap.contains(key) and nodeMap.contains(curNode)
		if (!keyIsEmptyString) { // empty string check)
			while (keyIT.hasNext()) {
				curKey = keyIT.next();
				keyMap.add(curKey);
				nodeMap.add(curNode);
				HashTrieNode temp = getNextNode(curKey, curNode);
				if (temp == null) {
					// value wasn't found -> don't perform any action
					return;
				} else {
					// make progress
					curNode = temp;
				}
			}
			// Post Condition - curNode is last node in key
			// watch out for size
			if (curNode.value != null) {
				this.size--;
			}
			HashTrieNode prevNode = curNode;
			// only manipulate structure if there aren't values which descend
			if (curNode.pointers.isEmpty()) {
				curNode = null;
				int size = keyMap.size();
				int i = 0;
				// Loop Inv - for every key and node seen, node.pointers.remove(key)
				for (; i < size; i++) {
					curNode = nodeMap.next();
					curKey = keyMap.next();
					// always removing key
					removeNode(curKey, curNode, prevNode);
					// if curNode has value, or points to values -> stop
					if (!curNode.pointers.isEmpty() || (curNode.value != null)) {
						break;
					}
					prevNode = curNode;
				}
			} else {
				// just remove nodes value
				curNode.value = null;
			}
		}else {
			//empty string case
			if(curNode.value!=null){
				curNode.value = null;
				this.size--;
			}
		}	
	}

	/**
	 * A helper method to clear out a node, and remove the pointer to it.
	 * 
	 * @param key     the key which pNode.pointers.contains
	 * @param nWithPointer   the node which node.pointers.contains(key)
	 * @param nToDelete the node we are actually deleting, which pNode points to
	 */
	private void removeNode(A key, HashTrieNode nWithPointer, HashTrieNode nToDelete) {
		nWithPointer.pointers.remove(key);
		nToDelete.value = null;
		nToDelete.pointers = null;
		nToDelete = null;
	}

	@Override
	public void clear() {
		// clear all nodes
		for (Item<K, V> i : this) {
			i = null;
		}
		//clear size
		this.size = 0;
		// reset root
		this.root = new HashTrieNode();
	}
}
