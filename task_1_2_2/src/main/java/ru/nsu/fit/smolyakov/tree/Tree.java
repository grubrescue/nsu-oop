package ru.nsu.fit.smolyakov.tree;

import static java.lang.reflect.Array.newInstance;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * An implementation of classic tree collection.
 * 
 * <p>While initializing, an auxiliary root node without value 
 * is created, so all nodes we add will be successors of 
 * this node or of successors of this node.
 * 
 * <p>Null-values are not allowed! {@link #add} and {@link #addNode}
 * methods will throw {@link IllegalArgumentException}, and 
 * {@link #remove} and {@link #contains} methods will do nothing 
 * and return false.
 * 
 * @see java.util.Collection
 */
public class Tree<T> implements Collection<T> {
    // With the purpose of improving perfomance,
    // we use polynomial hash
    private int hash = 0;
    private int size = 0;
    
    // AtomicLong is used as we need to
    // create a reference to this entity
    // from DfsIterator class
    private AtomicLong lastModified = new AtomicLong(System.currentTimeMillis()); 

    private Node<T> root = new Node<T>(this, null, null);

    /**
     * Constructs a new tree with an empty root node.
     */
    public Tree() {}

    /**
     * Constructs a new tree with an empty root node
     * and element with specified {@code value}.
     * 
     * @param  value  a value
     * 
     * @throws IllegalArgumentException  if value is null
     */
    public Tree(T value) throws IllegalArgumentException {
        addNode(value);
    }

    /** 
     * Constructs a tree with an empty root node and 
     * elements contained in the specified {@code collection} 
     * as its successors. 
     * 
     * @param  collection  a collection of values
     * @throws IllegalArgumentException  if any item of collection
     *                                   is null
     */
    public Tree(Collection<? extends T> collection) throws IllegalArgumentException {
        addAll(collection);
    }

    /**
     * Adds an element with specified {@code value} to this tree 
     * as a successor of root.
     * 
     * @return true 
     * @throws IllegalArgumentException  if value is null
     */
    @Override
    public boolean add(T value) throws IllegalArgumentException {
        addNode(value);
        return true;
    }

    /**
     * The same as {@link #add} method, but return value for this 
     * is a reference to new node.
     * 
     * @param  value  a value to add
     * @return a reference to new node
     * @throws IllegalArgumentException  if value is null
     */
    public Node<T> addNode(T value) throws IllegalArgumentException {
        return addNode(root, value);
    }

    /**
     * Adds an element with specified {@code value} to this tree 
     * as a successor of specified node.
     * 
     * @param  where  a parent node 
     * @param  value  a value to add
     * @return a reference to new node
     * @throws IllegalArgumentException  if value is null
     */
    public Node<T> addNode(Node<T> where, T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Nulls are not allowed");
        } else if (where.getProprietor() != this) {
            throw new IllegalArgumentException("This node doesn't belong the tree");
        } else {
            lastModified.set(System.currentTimeMillis());

            hash += value.hashCode();
            size++;

            var newNode = new Node<T>(where, value);
            where.addChild(newNode);
            return newNode;
        }
    }

    /** 
     * Adds all elements contained in the specified {@code collection}
     * as a successors of a root node.
     * 
     * @param  collection  a collection of values
     * @return true 
     * @throws IllegalArgumentException  if collection is null
     *                                   or one of collection
     *                                   elements is null
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) throws IllegalArgumentException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection is null");
        }

        collection.stream() 
                  .forEachOrdered(this::add);

        return true;
    }

    /**
     * Removes all nodes, except for auxiliary root node.
     */
    @Override
    public void clear() {
        lastModified.set(System.currentTimeMillis());

        root.clearChildren();
        size = 0; 
        hash = 0;
    }

    /**
     * Checks if {@code obj} is present in this tree.
     * 
     * @param  obj  an object to check presence of
     * @return true if this tree contains obj,
     *         false otherwise
     */
    @Override
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }

        return this.stream()
                   .anyMatch((val) -> val.equals(obj));
    }

    /**
     * Checks if all elements contained in the specified {@code collection}
     * is present in this tree.
     * 
     * @param  collection  a collection of elements to 
     *                     check presence of
     * @return true if this tree contains all objects
     *         of collection, false otherwise
     * @throws IllegalArgumentException  if one of collection
     *                                   elements is null
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection is null");
        }

        return collection.stream()
                         .map(this::contains)
                         .noneMatch((res) -> (res == false));
    }

    
    /**
     * Checks if this tree has only empty root node.
     * 
     * @return true if this tree has only empty root node,
     *         false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator over the elements of 
     * this tree in breadth-first search order.
     * Empty root node is not evolved.
     * 
     * <p>This method is a wrapper for {@link bfsIterator} method.
     * 
     * @return an iterator over the elements in BFS order
     */
    @Override
    public Iterator<T> iterator() {
        return new BfsIterator<T>(root, lastModified);
    }

    /**
     * Returns an iterator over the elements of 
     * this tree in depth-first search order.
     * Empty root node is not evolved.
     * 
     * @return an iterator over the elements in DFS order
     */
    public Iterator<T> dfsIterator() {
        return new DfsIterator<T>(root, lastModified);
    }

    /**
     * Returns an iterator over the elements of 
     * this tree in breadth-first search order.
     * Empty root node is not evolved.
     * 
     * @return an iterator over the elements in BFS order
     */
    public Iterator<T> bfsIterator() {
        return new BfsIterator<T>(root, lastModified);
    }



 
    /**
     * Removes all elements of this tree equal to {@code obj}.
     * 
     * @param  obj  an object to remove
     * @return true if at least one element of this tree
     *         is removed, false otherwise
     */
    @Override
    public boolean remove(Object obj) throws IllegalArgumentException {
        if (obj == null) {
            return false;
        }

        int srcSize = this.size;

        var iterator = this.iterator();
        while (iterator.hasNext()) {
            var val = iterator.next();
            if (val.equals(obj)) {
                iterator.remove();

                size--;
                hash -= obj.hashCode();
            }
        }

        if (this.size < srcSize) {
            lastModified.set(System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all elements of this tree equal to 
     * elements contained in the specified {@code collection}.
     * All successors of removed elements are also removed.
     * 
     * @param  collection  a collection of objects to remove
     * @return true 
     * @throws IllegalArgumentException  if collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) throws IllegalArgumentException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection is null");
        }

        collection.stream()
                  .forEachOrdered(this::remove);

        return true;
    }

    /**
     * Retains all elements of this tree equal to 
     * elements contained in the specified {@code collection}.
     * All successors of unretained elements are also removed.
     * 
     * @param  collection  a collection of objects to retain
     * @return true
     * @throws IllegalArgumentException  if collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection is null");
        }

        var iter = iterator();
        while (iter.hasNext()) {
            T obj = iter.next();
            
            if (collection.stream()
                .allMatch((o) -> !obj.equals(o))) {
                    iter.remove();
                    
                }
        }

        return true;
    }

    /**
     * Returns amount of elements in a tree, exclusive of auxiliary root node.
     * 
     * @return amount of elements in a tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Creates new array consisting of elements of this tree
     * sorted in a DFS order.
     * 
     * <p>If this tree was changed during execution of this method,
     * the behaviour is undefined.
     * 
     * @return  array of this tree elements in a DFS order
     * @see #iterator
     */
    // @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray() {
        return toArray(new Object[this.size]);
    }

    /**
     * Puts elements of this tree sorted in a DFS order to
     * {@code arr}, if it has enough length, otherwise creates a new one.
     * 
     * <p>If this tree was changed during execution of this method,
     * the behaviour is undefined.
     * 
     * @param  arr  an array to put elements to
     * @return amount of elements in a tree
     * @see #iterator
     * 
     * @throws NullPointerException  if specified array is null
     * @throws ArrayStoreException  if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     */
    @SuppressWarnings("unchecked")
    @Override
    public <A> A[] toArray(A[] arr) {
        if (this.size > arr.length) {
            arr = (A[]) newInstance(
                    arr.getClass().getComponentType(), this.size);
        }

        var iterator = this.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            arr[i++] = (A) iterator.next();
        }

        if (this.size < arr.length) {
            arr[size] = null;
        }

        return arr;
    }

    /**
     * Returns a polynomial hash for all elements of 
     * this tree.
     * 
     * @return  hash of all elements of this tree
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Compares the specified object with this tree for equality. 
     * Returns true if the specified object is also a tree, 
     * the two trees have the same size, and every member of the 
     * specified tree is contained in this tree, considering 
     * their location.
     * 
     * @param  obj  object to be compared for equality with this tree
     * @return true if the specified object is equal to this tree,
     *         false otherwise
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        Tree<T> other = (Tree<T>) obj;

        if (size != other.size) {
            return false;
        } else if (hash != other.hash) {
            return false;
        } 

        return this.root.equalRecursive(other.root);
    }
}
