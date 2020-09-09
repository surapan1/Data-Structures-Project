package p2.wordsuggestor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Supplier;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.misc.LargeValueFirstItemComparator;
import cse332.sorts.InsertionSort;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.dictionaries.MoveToFrontList.MTFNode;
import p2.sorts.TopKSort;

public class NGramToNextChoicesMap {
    private final Dictionary<NGram, Dictionary<AlphabeticString, Integer>> map;
    private final Supplier<Dictionary<AlphabeticString, Integer>> newInner;

    public NGramToNextChoicesMap(
            Supplier<Dictionary<NGram, Dictionary<AlphabeticString, Integer>>> newOuter,
            Supplier<Dictionary<AlphabeticString, Integer>> newInner) {
        this.map = newOuter.get();
        this.newInner = newInner;
    }

    /**
     * Increments the count of word after the particular NGram ngram.
     */
    public void seenWordAfterNGram(NGram ngram, String word) {
        // convert string here, check for mapping;
        AlphabeticString alphaWord = new AlphabeticString(word);
        Dictionary<AlphabeticString, Integer> curMapping = map.find(ngram);

        if (curMapping == null) {
            // need to add a completely new mapping for both inner and outer
            Dictionary<AlphabeticString, Integer> newMap = newInner.get();
            newMap.insert(alphaWord, 1);
            map.insert(ngram, newMap);
        }
        else {
            // map has this ngram, does this ngram have a mapping for word
            Integer newVal = curMapping.find(alphaWord);
            newVal = (newVal == null) ? 1 : (newVal + 1);
            curMapping.insert(alphaWord, newVal);
        }
    }

    /**
     * Returns an array of the DataCounts for this particular ngram. Order is
     * not specified.
     *
     * @param ngram
     *            the ngram we want the counts for
     * 
     * @return An array of all the Items for the requested ngram.
     */
    @SuppressWarnings("unchecked")
    public Item<String, Integer>[] getCountsAfter(NGram ngram) {
        // pull up map
        Dictionary<AlphabeticString, Integer> words = map.find(ngram);
        if (words == null) {
            return (Item<String, Integer>[]) Array.newInstance(Item.class, 0);
        }

        Item<String, Integer>[] counts = (Item<String, Integer>[]) Array
                .newInstance(Item.class, words.size());
        Iterator<Item<AlphabeticString, Integer>> IT = words.iterator();
        int arrayCounter = 0;
        // Loop Inv - for every word in words seen, word has been added to
        // counts
        // with appropriate changes to types
        while (IT.hasNext()) {
            Item<AlphabeticString, Integer> word = IT.next();
            String s = word.key.toString();
            counts[arrayCounter] = new Item<String, Integer>(s, word.value);
            arrayCounter++;
        }
        // post condition - counts contains every word/count mapped to by ngram
        // in map
        return counts;
    }

    @SuppressWarnings("unchecked")
    public String[] getWordsAfter(NGram ngram, int k) {
        Item<String, Integer>[] afterNGrams = getCountsAfter(ngram);

        Comparator<Item<String, Integer>> comp = new LargeValueFirstItemComparator<String, Integer>();
       
        if (k < 0) {
            InsertionSort.sort(afterNGrams, comp);
        }
        else {
            Comparator<Item<String, Integer>> c = new Comparator<Item<String, Integer>>() {
                @Override
                public int compare(Item<String, Integer> e1, Item<String, Integer> e2) {
                    return e1.value.compareTo(e2.value);
                }
            };
            TopKSort.sort(afterNGrams,k,c);
        }

        String[] nextWords = new String[k < 0 ? afterNGrams.length : k];
        for (int l = 0; l < afterNGrams.length && l < nextWords.length
                && afterNGrams[l] != null; l++) {
            nextWords[l] = afterNGrams[l].key;
        }
        return nextWords;
    }

    @Override
    public String toString() {
        return this.map.toString();
    }
}
