package p2.writeup;

import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Supplier;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.MoveToFrontList;
import p2.clients.NGramTester;
import p2.wordsuggestor.WordSuggestor;

public class NewDictionaryExperiment {

    public static void main(String[] args) throws IOException {
        //Compare BST, AVLTree, ChainingHashTable, and HashTrieMap on alice.txt
        String file = "alice.txt";
        int n = 3;
        int k = -1;
        
        System.out.println("BST Process: " + processTimeBST(file, n, k));
        System.out.println("AVL Process: " + processTimeAVL(file, n, k));
        System.out.println("CHTm Process: " + processTimeCHTmtf(file, n, k));
        System.out.println("CHTa Process: " + processTimeCHTavl(file,n,k));
        System.out.println("CHTc Process: " + processTimeCHTcombo(file,n,k));
        System.out.println("HTM Process: " + processTimeHTM(file, n, k));
    }

    public static double processTimeBST(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;
        
        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k,
                    NGramTester.binarySearchTreeConstructor(),
                    NGramTester.binarySearchTreeConstructor());
            //System.out.println(suggestions);
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTimeAVL(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k,
                    NGramTester.avlTreeConstructor(),
                    NGramTester.avlTreeConstructor());
            //System.out.println(suggestions);
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTimeCHTmtf(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;
        

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k,
                    NGramTester.hashtableConstructor(() -> (new ChainingHashTable<>(() -> new MoveToFrontList<>()))),
                    NGramTester.hashtableConstructor(() -> new MoveToFrontList<>()));
            //System.out.println(suggestions);
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }
        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTimeCHTavl(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;
        

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k, NGramTester.hashtableConstructor(() -> 
                    (new ChainingHashTable<NGram,Dictionary<AlphabeticString,Integer>>(NGramTester.avlTreeConstructor()))),
                    NGramTester.hashtableConstructor(() -> new AVLTree()));
            //System.out.println(suggestions);
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTimeCHTcombo(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;
        

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k,
                    NGramTester.hashtableConstructor(() -> 
                    (new ChainingHashTable<NGram,Dictionary<AlphabeticString,Integer>>(NGramTester.avlTreeConstructor()))),
                    NGramTester.hashtableConstructor(() -> new MoveToFrontList<>()));           
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
    
    public static double processTimeHTM(String file, int n, int k) throws FileNotFoundException, IOException {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            WordSuggestor suggestions = new WordSuggestor(file, n, k,
                    NGramTester.trieConstructor(NGram.class),
                    NGramTester.trieConstructor(AlphabeticString.class));
            //System.out.println(suggestions);
            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }

        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
}
