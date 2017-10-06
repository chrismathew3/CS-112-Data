package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {
    
    /**
     * Inner class - to build the partial tree circular linked list 
     * 
     */
    public static class Node {
        /**
         * Partial tree
         */
        public PartialTree tree;
        
        /**
         * Next node in linked list
         */
        public Node next;
        
        /**
         * Initializes this node by setting the tree part to the given tree,
         * and setting next part to null
         * 
         * @param tree Partial tree
         */
        public Node(PartialTree tree) {
            this.tree = tree;
            next = null;
        }
    }

    /**
     * Pointer to last node of the circular linked list
     */
    private Node rear;
    
    /**
     * Number of nodes in the CLL
     */
    private int size;
    
    /**
     * Initializes this list to empty
     */
    public PartialTreeList() {
        rear = null;
        size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
        Node ptr = new Node(tree);
        if (rear == null) {
            ptr.next = ptr;
        } else {
            ptr.next = rear.next;
            rear.next = ptr;
        }
        rear = ptr;
        size++;
    }

    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
        //case where there no trees in the list
        if(size < 1){
            throw new NoSuchElementException("Empty List");
        //case where there is one tree  
        }else if(size == 1){
            Node end = rear;                                             
            rear = null;                                                      
            size = 0;
            end.next = null;      
            return end.tree;
        //case where there is more than one tree
        }else {
             Node front = rear.next;                                             
             rear.next = front.next;                                             
             size = size - 1;
             front.next = null;
             return front.tree;
        }
            
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
        PartialTree removedTree = null;
        if (rear == null) {
            throw new NoSuchElementException("Empty List");
        }    
        Node temp = rear;
         
        do {
            PartialTree tree = temp.tree;
            boolean isTrue = vCheck(tree, vertex);
            
            if (isTrue) {
                removedTree = tree;
                nodeRemoval(temp);
                break;
            }
            temp = temp.next;

            
        } 
        while (temp != rear);
         
        if (removedTree == null) {
            return null;
        }
        else {
            return removedTree;
        }
    }
  
    private boolean vCheck (PartialTree t, Vertex v) {
        Vertex pT = v;
         
        while (pT.parent != pT) {         
            pT = pT.parent;
        }
        return pT == t.getRoot();
    }
    
    private void nodeRemoval (Node n) {
        Node beforeNode;               
        beforeNode = n;
        
        while (!(beforeNode.next == n)) {
            beforeNode = beforeNode.next;
        } 
        Node afterNode = n.next;
        if (afterNode == n && beforeNode == n) {
            rear = null;
            size = size - 1;
        }
        else if (afterNode == beforeNode) {
            if (n == rear) {                      
                 rear = rear.next;
            }
            (n.next).next = n.next;            
            size = size - 1;
        }
         
        else {
            if (n == rear) {
                rear = beforeNode;
            }
            beforeNode.next = afterNode;
            size = size - 1;
        }
        n.next = null;
    }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
        return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
        
        private PartialTreeList.Node ptr;
        private int rest;
        
        public PartialTreeListIterator(PartialTreeList target) {
            rest = target.size;
            ptr = rest > 0 ? target.rear.next : null;
        }
        
        public PartialTree next() 
        throws NoSuchElementException {
            if (rest <= 0) {
                throw new NoSuchElementException();
            }
            PartialTree ret = ptr.tree;
            ptr = ptr.next;
            rest--;
            return ret;
        }
        
        public boolean hasNext() {
            return rest != 0;
        }
        
        public void remove() 
        throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
        
    }
}

