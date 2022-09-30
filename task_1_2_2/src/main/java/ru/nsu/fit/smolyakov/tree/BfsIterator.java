package ru.nsu.fit.smolyakov.tree;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An iterator over a tree, which is ordered by breadth-first search.
 * Removing elements during iteration is not allowed!
 * 
 * @see java.util.Iterator
 */
class BfsIterator<T> implements Iterator<T> {
    private Long iteratorModified = System.currentTimeMillis();
    private AtomicLong lastModified;

    private Deque<Node<T>> queue = new ArrayDeque<>();

    boolean currRemoved = true;
    Node<T> current = null;

    // May be instantiated only by Tree<T>.iterator()
    BfsIterator(Node<T> root, AtomicLong lastModified) {
        this.lastModified = lastModified;
        queue.addAll(root.getChildren());
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
        return !queue.isEmpty();
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
            throw new ConcurrentModificationException("modification outside iterator is prohibited");
        }
        if (!hasNext()) {
            throw new NoSuchElementException("No more items");
        }

        currRemoved = false;

        current = queue.poll();
        queue.addAll(current.getChildren());
        return current.getValue();
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
            for (int i = 0; i < current.getChildCount(); i++) {
                queue.removeLast();
            }

            current.getParent().removeChild(current);
            current = null;
            currRemoved = true;

            iteratorModified = System.currentTimeMillis();
            lastModified.set(iteratorModified);
        }
    }
}
