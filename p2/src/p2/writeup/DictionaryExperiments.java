package p2.writeup;

import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Supplier;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.MoveToFrontList;
import p2.wordsuggestor.WordSuggestor;

public class DictionaryExperiments {

    public static void main(String[] args) throws FileNotFoundException {
        //Compare BST, AVLTree, ChainingHashTable, and HashTrieMap on alice.txt
        BinarySearchTree<String, Integer> bst = new BinarySearchTree<>();
        AVLTree<String, Integer> avl = new AVLTree<>();
        ChainingHashTable<String, Integer> cht = new ChainingHashTable<>(() -> new MoveToFrontList<>());
        HashTrieMap<Character, AlphabeticString, String> htm = 
                new HashTrieMap<>(AlphabeticString.class);
        
        File file = new File("alice.txt"); 
        Scanner sc = new Scanner(file);

//        String [] alice = new String [26348];
        ArrayList<String> alice = new ArrayList<String>();
        int n = 0;

        while (sc.hasNext()) {
//            alice[n] = sc.next();
            alice.add(sc.next());
//            n++;
        }
        System.out.println("size: " + alice.size());
        System.out.println("BST Process: " + processTime(bst, alice));
        System.out.println("AVL Process: " + processTime(avl, alice));
        System.out.println("CHT Process: " + processTime(cht, alice));
       // System.out.println("HTM Process: " + processTime2(htm, alice));
//        for(int l = 0; l < alice.size();l++) {
//            System.out.print(alice.get(l));
//            if(l%20 == 0) {
//                System.out.println();
//            }
//        }
        sc.close();
        
        
//       try {
//           long start = System.currentTimeMillis();
//        WordSuggestor ws = new WordSuggestor("alice.txt", 26348, 26348,
//                   (() -> new ChainingHashTable<NGram,Dictionary<AlphabeticString, Integer>>(() -> new MoveToFrontList<>())),
//                   () -> new ChainingHashTable<AlphabeticString,Integer>(() -> new MoveToFrontList<>()));
//        long end = System.currentTimeMillis();
//        System.out.println("time: " + (end-start));
//        
//         BinarySearchTree<BString<String>,Dictionary<AlphabeticString, Integer>> b;
//        
//         start = System.currentTimeMillis();
//        ws = new WordSuggestor("alice.txt", 26348, 26348,
//                (() ->  new BinarySearchTree<NGram,Dictionary<AlphabeticString, Integer>>()),
//                () -> new ChainingHashTable<AlphabeticString,Integer>(() -> new MoveToFrontList<>()));
//        end = System.currentTimeMillis();
//        System.out.println("time: " + (end-start));
//        
//        
//    } catch (IOException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//    }
    }

//    private static void incCount(Dictionary<String, Integer> list, String key) {
//        Integer find = list.find(key);
//        if (find == null)
//            list.insert(key, 1);
//        else
//            list.insert(key, 1 + find);
//    }

    public static double processTime(Dictionary<String, Integer> dict, ArrayList<String> al) {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            for(int i = 0; i < al.size(); i++) {
                //increment count? 
                //incCount(dict, al[i]);
                dict.insert(al.get(i), i);
            }
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTime2(HashTrieMap<Character, AlphabeticString, String> trie, String [] al) {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            for (String word : al) {
                trie.insert(a(word), word.toUpperCase());
            }
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    /**
     * Converts a String into an AlphabeticString
     */
    private static AlphabeticString a(String s) {
        return new AlphabeticString(s);
    }
}
