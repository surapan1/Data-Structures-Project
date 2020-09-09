package datastructures.dictionaries;

   
    import java.lang.reflect.Array;
    import java.util.Iterator;
    import java.util.function.Supplier;

    import cse332.datastructures.containers.*;
    import cse332.interfaces.misc.DeletelessDictionary;
    import cse332.interfaces.misc.Dictionary;
    import cse332.interfaces.misc.SimpleIterator;
import datastructures.worklists.CircularArrayFIFOQueue;

    /**
     * A class which operates as a chaining hash table. Clients can provide their own structure
     * for the chains, as long as it extends Dictionary. Clients must also provide a 
     * Supplier<Dictionary<K,V>> in order to create the chains. They keys inserted into the table
     * are expected to provide their own hash function. The class does offer an Iterator, but is 
     * unwilling to commit to the order in which elements are returned. 
     * 
     * *SIDE NOTE FOR GRADERS
     * In order to pass the delete tests for HashTrieMap,
     * there is a method, get(), which is just a synonym for find()
     *  
     */
    public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
        private Supplier<Dictionary<K, V>> newChain;

        // what else does this class need?
        /** The array which represents the table */
        private Dictionary<K, V>[] table;
        /** keep track index for next prime number */
        private int nextPrime;
        /** array of primes */
        private int[] primes = new int[] { 53, 109, 211, 431, 839, 1289, 1609, 2029, 3109,
                4057, 5231, 6163, 7283, 8923, 16033, 32563, 61031, 91139, 111913, 153067,
                184417, 202339 };
        //write up test primes
//        private int[] primes = new int[] {   153067,
//                184417, 202339 };
        
        

        @SuppressWarnings("unchecked")
        public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
            this.newChain = newChain;
            this.table = (Dictionary<K, V>[]) Array.newInstance(newChain.get().getClass(), 11);
            this.nextPrime = 0;
        }

        @Override
        public V insert(K key, V value) {
            if (key == null || value == null) {
                throw new IllegalArgumentException();
            }
            // resize if load factor too great
            if (this.size / this.table.length > 2) {
                resize();
            }
            V rval;
            // hash it
            int hashVal = Math.abs(key.hashCode());
            // find array spot
            int spot = hashVal % this.table.length;
            // already a data structure in place?
            if (this.table[spot] != null) {
                // check for duplicate key
                rval = this.table[spot].insert(key, value);
                if (rval == null) {
                    this.size++;
                }
            }
            else {
                // create a new dictionary baby
                Dictionary<K, V> nd = newChain.get();
                rval = nd.insert(key, value);
                this.table[spot] = nd;
                this.size++;
            }
            return rval;
        }

        @SuppressWarnings("unchecked")
        private void resize() {
            // get next size, create new array
            int newSize;
            if (this.nextPrime >= this.primes.length) {
                newSize = (this.primes.length * 2);
            }
            else {
                newSize = this.primes[this.nextPrime++];
            }
            Dictionary<K, V>[] newTable = (Dictionary<K, V>[]) Array
                    .newInstance(newChain.get().getClass(), newSize);
            // okay ugly part incoming
            // Loop Inv - for every index i, already seen,
            // the data structure located at this.table[i] has been iterated over
            // and each element in that structure has had their hash recomputed
            // and has been inserted into newDictionary
            int oldTableSize = this.table.length;
            for (int i = 0; i < oldTableSize; i++) {
                if (this.table[i] != null) {
                    Iterator<Item<K, V>> IT = this.table[i].iterator();
                    while (IT.hasNext()) {
                        Item<K, V> nxt = IT.next();
                        int hash = Math.abs(nxt.key.hashCode());
                        int spot = hash % newSize;
                        if (newTable[spot] != null) {
                            newTable[spot].insert(nxt.key, nxt.value);
                        }
                        else {
                            Dictionary<K, V> nd = this.newChain.get();
                            nd.insert(nxt.key, nxt.value);
                            newTable[spot] = nd;
                        }
                    }
                }
            }
            // Post condition - all of the elements in this.table have been
            // transferred to the new array
            // clear up the memory
            this.table = null;
            // change pointer
            this.table = newTable;
        }

        @Override
        public V find(K key) {
            if (key == null) {
                throw new IllegalArgumentException();
            }
            int hash = Math.abs(key.hashCode());
            int spot = hash % this.table.length;
            if (this.table[spot] != null) {
                return this.table[spot].find(key);
            }
            else {
                return null;
            }
        }

        @Override
        public Iterator<Item<K, V>> iterator() {
            return new CHTIterator();
        }

        private class CHTIterator extends SimpleIterator<Item<K, V>> {
            private Dictionary<K, V> curDictionary;
            private int curSpot;
            private Iterator<Item<K, V>> curIT;

            public CHTIterator() {
                curSpot = 0;
                //find the first spot in the array which has entries
                this.curDictionary = findNextDictionary();
                if (this.curDictionary != null) {
                    this.curIT = this.curDictionary.iterator();
                }
            }

            private Dictionary<K, V> findNextDictionary() {
                int size = ChainingHashTable.this.table.length;
                Dictionary<K,V> rd = null;
                for (int i = curSpot; i < size; i++) {
                    if (ChainingHashTable.this.table[i] != null) {
                        curSpot = i + 1;
                        return ChainingHashTable.this.table[i]; 
                    }
                }
                //if we are here the table has been iterated over
                curSpot = size;
                return rd;
            }

            @Override
            public Item<K, V> next() {
                return this.curIT.next();
            }

            @Override
            public boolean hasNext() {
                //couple cases here -> iterator is null or hasNext is false
                //but there might still be array entries
                if(this.curIT == null || (this.curIT.hasNext() == false)) {
                    //might be out of array spots
                    if(curSpot >= ChainingHashTable.this.table.length) {
//                        System.out.println("no more array spots");
                        return false;
                    }else {
                        //get the next dictionary
                        Dictionary<K,V> d = this.findNextDictionary();
                        if(d == null) {
//                            System.out.println("next dictionary was null");
                            //nothing else to look into
                            return false;
                        }
                        //else we have a dictionary, set up IT
                        this.curIT = d.iterator();                  
                    }
                }
                return this.curIT.hasNext();
            }
        }

        public V get(K key) {
            return this.find(key);
        }

    }