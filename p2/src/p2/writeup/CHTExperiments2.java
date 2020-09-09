package p2.writeup;


import datastructures.dictionaries.ChainingHashTable;

import java.util.Iterator;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.MoveToFrontList;

public class CHTExperiments2 {

    public static void main(String[] args) {
        
        //hard code array of values here
        AlphabeticString [] array = new AlphabeticString[1000000];
        for(int i = 0; i < 1000000;i++) {
            int k = i * 37* 33 % 1000000;
            String str = String.format("%05d", k);
            array[i] = new AlphabeticString(str);
        }
        int n = 100;
        
        System.out.println("MTF Process(n=100): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
        
        n = 1000;
        
        System.out.println("MTF Process(n=1000): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
        
        n = 10000;
        
        System.out.println("MTF Process(n=10000): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
        
        n = 25000;
        
        System.out.println("MTF Process(n=25000): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
        
        n = 100000;
        
        System.out.println("MTF Process(n=100000): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
        
        n = 250000;
        
        System.out.println("MTF Process(n=250000): " + processTime(new ChainingHashTable<BString<Character>,Integer>(() -> new MoveToFrontList<>()),n,array));
    }
    
    private static void incCount(Dictionary<BString<Character>, Integer> list, AlphabeticString key) {
        Integer find = list.find(key);
        if (find == null)
            list.insert(key, 1);
        else
            list.insert(key, 1 + find);
    }
    
    public static double processTime(ChainingHashTable<BString<Character>, Integer> cht, int n,AlphabeticString[] array) {

        int NUM_TESTS = 15;
        int NUM_WARMUP = 10;
        double totalTime = 0;
        
        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                AlphabeticString str = array[i];
                for (int j = 0; j < 10; j ++) {
                    incCount(cht, str);
                }
            }
            long endTime = System.currentTimeMillis();
          if (NUM_WARMUP <= h) {
              totalTime += (endTime - startTime);
          }
            
        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
}
