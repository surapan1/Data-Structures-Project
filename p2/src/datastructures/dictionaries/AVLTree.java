package datastructures.dictionaries;


import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
//import cse332.datastructures.trees.BinarySearchTree.BSTNode;

/**
 * This class started out expecting to have direct access to a nodes parent's via a field
 * inside AVLNode. After insertion, the node could travel back up the tree until it reached 
 * a point which violated the AVL conditions and perform necessary swaps. This seemed to be a 
 * better method then going top-down looking for height differences > 1. However, while reading
 * the textbook to better understand AVL trees, they provided a recursive solution with a cleaner, 
 * pointer-less way track parents -> this is essentially their implementation. 
 */

public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V>  {
    private final int IMBALANCE_LIMIT = 1;
    //used for the return value of insert -> no scoping issues *currently*
    V oldValue;
    
    public AVLTree(){
        super();
    }
    
    @SuppressWarnings("unchecked")
    private AVLNode cast(BSTNode node) {
        return (AVLTree<K, V>.AVLNode) node;
    }
    
    /*
     * The workaround for the below snippet, which was in insert appears to be operating as desired. 
     * No scope issues being caused *currently*.
     */
    //Guarantees this whole thing to be 2*log(n) operation
    //BUT caused by method specifications of returning val and work arounds being hacky
    //still Omega(log(n)) in the end, so neener-neener;
    //gets the return value here, instead of keeping track of it later
//    V returnValue = find(key);
//    if(returnValue == null) {
//        //new element
//        this.size++;
//    }
    @Override 
    public V insert(K key,V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        AVLNode curNode = cast(this.root);
        //make an Item for easy transporting
        Item<K,V> item = new Item<K,V>(key,value);
        this.root = recursiveInsert(item,curNode);     
        return this.oldValue;
   }
    
    private AVLNode recursiveInsert(Item<K,V> item, AVLNode root) {
        if(root == null) {
            //found location for insertion
            this.oldValue = null;
            this.size++;
            return new AVLNode(item);
           
        }
        int compare = root.key.compareTo(item.key);
        
        if(compare < 0) {
            root.children[1] = recursiveInsert(item,cast(root.children[1]));
        }else if(compare > 0) {
            root.children[0] = recursiveInsert(item,cast(root.children[0]));
        }else {
            //we are at a node with key = item.key, switch value
            this.oldValue = root.value;
            root.value = item.value;
        }
        return restructure(root);
    }
    /**
     * Method to maintain height requirements
     * @param curNode
     */
    private AVLNode restructure(AVLNode root) {
        if(root == null) {
            return root;
        }
        AVLNode rootLeft = cast(root.children[0]);
        AVLNode rootRight = cast(root.children[1]);
        if(height(rootLeft) - height(cast(rootRight)) > IMBALANCE_LIMIT) {
            if(height(cast(rootLeft.children[0])) >= height(cast(rootLeft.children[1]))){
                root = rotateRight(root);
            }else {
                root = rotateLeftRight(root);
            }
        }else if(height(rootRight) - height(rootLeft) > IMBALANCE_LIMIT) {
            if(height(cast(rootRight.children[1])) >= height(cast(rootRight.children[0]))) {
                root = rotateLeft(root);
            }else {
                root = rotateRightLeft(root);
            } 
        }
        //possible new root, can't use old rootLeft/Right
        root.height = Math.max(height(cast(root.children[0])), height(cast(root.children[1]))) + 1;
        return root;
    }
    
    private AVLTree<K, V>.AVLNode rotateRightLeft(AVLTree<K, V>.AVLNode k3) {
        k3.children[1] =  rotateRight(cast(k3.children[1]));
        return rotateLeft(k3);
    }

    private AVLTree<K, V>.AVLNode rotateLeftRight(AVLTree<K, V>.AVLNode k3) {
        k3.children[0] =  rotateLeft(cast(k3.children[0]));
        return rotateRight(k3);
    }

    private AVLTree<K, V>.AVLNode rotateRight(AVLTree<K, V>.AVLNode k2) {
        AVLNode kOne = cast(k2.children[0]);
        k2.children[0] = kOne.children[1];
        kOne.children[1] = k2;
        //change heights from bottom to top
        k2.height = Math.max(height(cast(k2.children[0])),height(cast(k2.children[1]))) + 1;
        kOne.height = Math.max(height(cast(kOne.children[0])), k2.height) + 1;
        return kOne;
    }

    private AVLTree<K, V>.AVLNode rotateLeft(AVLTree<K, V>.AVLNode k2) {
        AVLNode k1 = cast(k2.children[1]);
        k2.children[1] = k1.children[0];
        k1.children[0] = k2;
      //change heights from bottom to top
        k2.height = Math.max(height(cast(k2.children[0])),height(cast(k2.children[1]))) + 1;
        k1.height = Math.max(height(cast(k1.children[0])), k2.height) + 1;
        return k1;
    }

    private int height(AVLNode node) {
        return (node == null) ? -1 : node.height;
    }
    

    public class AVLNode extends BSTNode{
        
        int height;
        public AVLNode(K key, V value) {
            super(key,value);
            this.height = 0;
        }
        public AVLNode(Item<K,V> item) {
            super(item.key,item.value);
            this.height = 0;
        }
        
    }
    
    
    
    
    
}
