package ru.nsu.fit.smolyakov.stack;

import java.util.Arrays;

/**
 * An implementation of a stack data structure.
 * дописать потом
 */
public class Stack<T> implements Cloneable {
    private class Parameters {
        final static int INITIAL_CAPACITY = 8;
        final static int RESIZE_FACTOR = 2;
    }

    private T[] arr;
    private int size = 0;

    private void resize() {
        arr = Arrays.copyOf(arr, arr.length * Parameters.RESIZE_FACTOR);
    }

    /**
     * Constructs an empty stack with the spectified initial capacity.
     * 
     * @param  capacity  the initial capacity of the stack
     * @throws IllegalArgumentException  if capacity is a negative number
     */
    public Stack(int capacity) throws IllegalArgumentException {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity can't be a negative number");
        }

        arr = (T[]) new Object[capacity];
    }

    /**
     * Constructs an empty stack with the initial capacity of 8.
     */
    public Stack() {
        this(Parameters.INITIAL_CAPACITY);
    }

    /**
     * Constructs a stack consisting of elements of initialArr.
     * The first element of the array will be the last 
     * element extracted from the stack.
     * 
     * @param  initialArr  an array whose elements are used 
     *                     to construct a stack
     * @throws NullPointerException  if initialArr is null
     */
    public Stack(T[] initialArr) throws NullPointerException {
        if (initialArr == null) {
            throw new NullPointerException("Input array has to exist");
        }

        this.arr = Arrays.copyOf(arr, arr.length);
        this.size = arr.length;
    }
    
    /**
     * Returns an amount of elements in a stack.
     * 
     * @return  an amount of elements in a stack
     */
    public int count() {
        return size;
    }

    /**
     * Returns true if a stack is empty.
     * 
     * @return  true if a stack contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Appends a single element to the top of the stack.
     * 
     * @param  elem  an element to push
     */
    public void push(T elem) {
        if (size >= arr.length) {
            resize();
        }
        arr[size++] = elem;
    }     

    /**
     * Appends all elements of anotherStack to the top.
     * An order of elements keeps unchanged.
     * 
     * @param  anotherStack  a stack to push
     * @throws NullPointerException  if anotherStack is null
     */
    public void pushStack(Stack<T> anotherStack) throws NullPointerException {
        if (anotherStack == null) {
            throw new NullPointerException("anotherStack at least should be a stack");
        }

        for (T elem : anotherStack.arr) {
            push(elem);
        }
    }
    
    /**
     * Returns a value of a top element of a stack.
     * 
     * @return  a value of a head of a stack
     */
    public T peek() {
        if (size > 0) {
            return arr[size-1];
        } else {
            return null;
        }
    }

    /**
     * Extracts a top element of a stack.
     * 
     * @return  the top element of the stack
     */
    public T pop() {
        if (size > 0) {
            return arr[--size];
        } else {
            return null; //maybe exception/optional?
        }
    }

    /**
     * Extracts elemAmount top elements from a stack and 
     * returns a stack consisting of them.
     * <p>
     * If the size of the stack is less than elemAmount, 
     * the method is applied to the whole stack.
     * 
     * @param  elemAmount  an amount of elements to extract
     * @return             a stack composed of extracted elements
     */
    public Stack<T> popStack(int elemAmount) {
        elemAmount = Math.min(size, elemAmount);

        var selectedRangeArr = Arrays.copyOfRange(arr, size-elemAmount, size);
        size -= elemAmount; 

        return new Stack<T>(selectedRangeArr);
    }


    /**
     * {@inheritDoc}
     * Compares the specified object with this stack for equality. 
     * Returns true if and only if the specified object is also a stack, 
     * both stacks have the same size, and all corresponding pairs 
     * of elements in the two lists are equal. 
     * In other words, two stacks are defined to be equal 
     * if they contain the same elements in the same order.
     * 
     * @param  obj  an object to compare with this
     * @return      true if the specified object is equal to this stack
     */
    @Override 
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Stack<?>)) {
            return false;
        } else {
            var stack = (Stack<?>) obj;

            if (this.size != stack.size) {
                return false;
            }

            for (int i = 0; i < stack.size; i++) {
                if (!(this.arr[i] == null && stack.arr[i] == null) &&
                    !(this.arr[i] != null && this.arr[i].equals(stack.arr[i]))) {
                    return false;
                }
            }

            return true;
        }
    }
    
    /**
     * {@inheritDoc}
     * Returns a deep copy of this Stack instance.
     * @return  a deep copy of a stack
     * 
     * @see  Cloneable
     */
    public Object clone() {
        var cloned = new Stack<T>(this.size);
        System.arraycopy(this.arr, 0, cloned.arr, 0, this.size);
        cloned.size = this.size;

        return cloned;
    }
}