package ru.nsu.fit.smolyakov.tree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An iterator over a tree, which is ordered by depth-first search.
 * Allows to remove elements during iteration.
 * 
 * @see java.util.Iterator
 */
class DfsIterator<T> implements Iterator<T> {
    private Long iteratorModified = System.currentTimeMillis();
    private AtomicLong lastModified;

    private Stack<Integer> stack = new Stack<>();
    private Node<T> currentNode;
    private int currentId = 0;

    private boolean currRemoved = true;
    
    // May be instantiated only by Tree<T>.iterator()
    DfsIterator(Node<T> root, AtomicLong lastModified) {
        this.lastModified = lastModified;
        this.currentNode = root;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        if (currentNode.getValue() == null 
            && currentId >= currentNode.getChildCount()) {
            return false;
        } else {
            return true;
        }
    } 

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     * @throws ConcurrentModificationException if some modifications are made to the tree
     *                                         while iteration
     */
    @Override
    public T next() throws NoSuchElementException {
        if (lastModified.get() > iteratorModified.longValue()) {
            throw new ConcurrentModificationException("modification during iteration is prohibited");
        }
        if (!hasNext()) {
            throw new NoSuchElementException("No more items");
        }

        currRemoved = false;

        if (currentId >= currentNode.getChildCount()) {
            currentNode = currentNode.getParent();
            currentId = stack.pop();
        } else {
            while (currentNode.getChild(currentId).getChildCount() > 0) {
                currentNode = currentNode.getChild(currentId);
                stack.push(currentId);
                currentId = 0;
            }
        }
        
        return currentNode.getChild(currentId++).getValue();
    }

    /**
     * Removes from this tree the last element returned
     * by this iterator (optional operation). This method can be called
     * only once per call to {@link #next}.
     * 
     * <p>This is fail-fast iterator. If this tree is modified while the iteration 
     * is in progress in any way other than by calling this method, 
     * ConcurrentModificationException is thrown.
     */
    @Override
    public void remove() throws IllegalStateException {
        if (currRemoved) {
            throw new IllegalStateException("amount of calls of next() is not equal to 1");
        } else {
            currentNode.removeChild(--currentId);
            currRemoved = true;

            iteratorModified = System.currentTimeMillis();
            lastModified.set(iteratorModified);
        }
    }
}
