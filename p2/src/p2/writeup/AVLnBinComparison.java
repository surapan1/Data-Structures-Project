package p2.writeup;

import java.util.Random;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;

public class AVLnBinComparison {

    public static void main(String[] args) {

        BinarySearchTree<Integer, Integer> BST = new BinarySearchTree<Integer, Integer>();
        AVLTree<Integer, Integer> AVL = new AVLTree<Integer, Integer>();
        long[] bstTimes = new long[20];
        long[] avlTimes = new long[20];
        long bstAvg = 0;
        long avlAvg = 0;
        Random rand = new Random();

        // the increasing N insertion tests
        for (int j = 0; j < 40000; j+=2000) {
            long bstStart = System.currentTimeMillis();
            for (Integer i = 0; i < j; i++) {
                BST.insert(i, i);
            }
            long bstEnd = System.currentTimeMillis();
            long bstTotal = (bstEnd - bstStart);
            // System.out.println("total time: " + (bstTotal/1000) + "
            // seconds");

            long avlStart = System.currentTimeMillis();
            for (Integer i = 0; i < j; i++) {
                AVL.insert(i, i);
            }
            long avlEnd = System.currentTimeMillis();
            long avlTotal = (avlEnd - avlStart);
            // System.out.println("Total: " + (avlTotal/1000) + " seconds");
            bstTimes[j/2000] = bstTotal;
            avlTimes[j/2000] = avlTotal;
            bstAvg += bstTotal;
            avlAvg += avlTotal;
            
        }


        for (int k = 0; k < 20; k++) {
            System.out.print(bstTimes[k] + " ");
        }
        System.out.println();
        for (int k = 0; k < 20; k++) {
            System.out.print(avlTimes[k] + " ");
        }

        System.out.println();
        System.out.println("bstAverage in seconds: " + bstAvg / 20 + "   and avlAvg: "
                + avlAvg / 20);
        
        
        //the n = 40,000 tests
//        for (int j = 0; j < 20; j++) {
//            long bstStart = System.currentTimeMillis();
//            for (Integer i = 0; i < 40000; i++) {
//                BST.insert(i, i);
//            }
//            long bstEnd = System.currentTimeMillis();
//            long bstTotal = (bstEnd - bstStart);
//            // System.out.println("total time: " + (bstTotal/1000) + "
//            // seconds");
//
//            long avlStart = System.currentTimeMillis();
//            for (Integer i = 0; i < 40000; i++) {
//                AVL.insert(i, i);
//            }
//            long avlEnd = System.currentTimeMillis();
//            long avlTotal = (avlEnd - avlStart);
//            // System.out.println("Total: " + (avlTotal/1000) + " seconds");
//            bstTimes[j] = bstTotal;
//            avlTimes[j] = avlTotal;
//            bstAvg += bstTotal;
//            avlAvg += avlTotal;
//            
//        }
//        for (int k = 0; k < 20; k++) {
//            System.out.print(bstTimes[k] + " ");
//        }
//        System.out.println();
//        for (int k = 0; k < 20; k++) {
//            System.out.print(avlTimes[k] + " ");
//        }
//
//        System.out.println();
//        System.out.println("bstAverage in seconds: " + bstAvg / 20 + "   and avlAvg: "
//                + avlAvg / 20);

        // //the find tests

        // for (int j = 0; j < 10; j++) {
        // long bstStart = System.currentTimeMillis();
        // for (Integer i = 0; i < 40000; i++) {
        // BST.find(rand.nextInt(40000));
        // }
        // long bstEnd = System.currentTimeMillis();
        // long bstTotal = (bstEnd - bstStart);
        // // System.out.println("total time: " + (bstTotal/1000) + "
        // // seconds");
        //
        // long avlStart = System.currentTimeMillis();
        // for (Integer i = 0; i < 40000; i++) {
        // AVL.find(rand.nextInt(40000));
        // }
        // long avlEnd = System.currentTimeMillis();
        // long avlTotal = (avlEnd - avlStart);
        // // System.out.println("Total: " + (avlTotal/1000) + " seconds");
        // bstTimes[j] = bstTotal;
        // avlTimes[j] = avlTotal;
        // bstAvg += bstTotal;
        // avlAvg += avlTotal;
        // }
        //
        // for (int j = 0; j < 10; j++) {
        // System.out.print(bstTimes[j] + " ");
        // }
        // System.out.println();
        // for (int j = 0; j < 10; j++) {
        // System.out.print(avlTimes[j] + " ");
        // }
        //
        // System.out.println("bstAverage in seconds: " + bstAvg / 10 + " and
        // avlAvg: "
        // + avlAvg / 10);

    }
}
