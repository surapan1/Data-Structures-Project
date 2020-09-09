package p2.writeup;

import datastructures.dictionaries.ChainingHashTable;

import java.util.Iterator;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.Dictionary;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.MoveToFrontList;

public class CHTExperiments {

    public static void main(String[] args) {
        String[] strings = new String[1000000];
        for (int i = 0; i < 1000000; i++) {
            int k = (i * 37) % 31111;
            String str = String.format("%05d", k);
            strings[i] = str;
        }

        int n = 100;

        System.out.println("BST Process(n=100): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=100): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=100): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));

        n = 1000;

        System.out.println("BST Process(n=1000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=1000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=1000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));

        n = 10000;

        System.out.println("BST Process(n=10000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=10000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=10000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));

        n = 25000;

        System.out.println("BST Process(n=25000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=25000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=25000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));

        n = 100000;

        System.out.println("BST Process(n=100000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=100000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=100000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));

        n = 250000;

        System.out.println("BST Process(n=250000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=250000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=250000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));
           
        n = 1000000;
        System.out.println("BST Process(n=1000000): " + processTime(
                new ChainingHashTable<>(() -> new BinarySearchTree<String, Integer>()),
                n,strings));
        System.out.println("AVL Process(n=1000000): " + processTime(
                new ChainingHashTable<>(() -> new AVLTree<String, Integer>()), n,strings));
        System.out.println("MTF Process(n=1000000): "
                + processTime(new ChainingHashTable<>(() -> new MoveToFrontList<>()), n,strings));
    }

    public static double processTime(ChainingHashTable<String, Integer> cht, int n,
            String[] strings) {

        int NUM_TESTS = 5;
        int NUM_WARMUP = 3;
        double totalTime = 0;

        for (int h = 0; h < NUM_TESTS; h++) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                cht.insert(strings[i], i);
            }

            long endTime = System.currentTimeMillis();
            if (NUM_WARMUP <= h) {
                totalTime += (endTime - startTime);
            }
        }
        return totalTime / (NUM_TESTS - NUM_WARMUP);
    }
}
