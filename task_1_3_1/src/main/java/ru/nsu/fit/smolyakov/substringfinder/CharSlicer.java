package ru.nsu.fit.smolyakov.substringfinder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@code CharSlicer} class provides a fixed-size buffered sliding window
 * over a supplied text. 
 * 
 * Creation of a slice of a length greater than the whole text length 
 * will be interrupted by an {@link InputMismatchException}. 
 * 
 * The user is supposed to destructively assign a return value of a {@link shift} 
 * method to the current class instance, as it indicates if the stream is over.
 * If the user tries to use {@link get}, {@link iterator} or {@link toCharArray} 
 * from a slice doesn't containing a specified amount of characters, an
 * {@link IllegalStateException} will be thrown.
 */
public class CharSlicer implements Iterable<Character> {
    private class CharSlicerIterator implements Iterator<Character> {
        private int cur = 0;

        /**
         * Returns {@code true} if this slice has more characters.
         * (In other words, returns {@code true} if {@link #next} would
         * return a character rather than throwing an exception.)
         *
         * @return {@code true} if the slice has more elements
         */
        @Override
        public boolean hasNext() {
            return cur < sliceLength;
        }

        /**
         * Returns the next character of this slice iteration.
         *
         * @return the next character in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Character next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            return buf[pos + (cur++)];
        }

    }

    private final int MIN_BUF_SIZE = 4096;

    private char[] buf;

    private int pos = 0;
    private int size;
    private int bufCap;
    private int absolutePos = 0;

    private InputStreamReader source;

    private int sliceLength;

    private boolean readToBuf() throws IOException {
        System.arraycopy(buf, Integer.max(0, size-sliceLength), buf, 0, sliceLength);
        int bytesCnt = source.read(buf, sliceLength, bufCap - sliceLength);

        if (bytesCnt > 0) {
            pos = pos - size + sliceLength;
            size = bytesCnt + sliceLength;
            return true;
        } else {
            size = 0;
            return false;
        }
    }

    /**
     * Constructs a new {@link CharSlicer} instance with a specified {@link InputStreamReader}
     * and {@code sliceLength}.
     * 
     * @param source  an input stream reader
     * @param sliceLength  a length of this slice
     * 
     * @throws IOException  if {@code source} is unavailable
     * @throws InputMismatchException  if {@code source} contains less than {@code sliceLength} 
     *                                 characters
     * @throws IllegalArgumentException  if sliceLength is non-positive
     */
    public CharSlicer(InputStreamReader source, int sliceLength) throws IOException,
                                                                InputMismatchException {
        if (sliceLength <= 0) {
            throw new IllegalArgumentException();
        }

        this.source = source;
        this.sliceLength = sliceLength;

        bufCap = Integer.max(MIN_BUF_SIZE, sliceLength * 8);
        buf = new char[bufCap];

        size = this.source.read(buf, 0, bufCap);
        if (size < sliceLength) {
            throw new InputMismatchException();
        }
    }

    /**
     * Shifts the slice right by a specified {@code amount} of characters.
     *  
     * @param  amount  an amount of characters to shift the slice by
     * @return this slice if there is enough characters in the input stream,
     *         {@code null} otherwise
     * 
     * @throws IOException  if there is no enough characters in the input stream
     */
    public CharSlicer shift(int amount) throws IOException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }

        pos += amount;
        absolutePos += amount;

        while (pos + sliceLength > size) {
            if (readToBuf() == false) {
                return null;
            }
        }

        return this;
    }

    /**
     * Shifts the slice right by one character.
     *  
     * @return this slice if there is enough characters in the input stream,
     *         {@code null} otherwise
     * 
     * @throws IOException  if there is no enough characters in the input stream
     */
    public CharSlicer shift() throws IOException {
        return shift(1);
    }

    /**
     * Returns a slice character at a specified position.
     * 
     * @param  i  an index of a character
     * @return a character at a specified position
     * 
     * @throws IllegalStateException  if an amount of characters in this slice is less 
     *                                than specified due to end of stream
     * @throws ArrayIndexOutOfBoundsException if {@code i} is not between 0 and a length 
     *                                        of this slice (exclusive)
     *                                        
     */
    public char get(int i) {
        if (size - pos < sliceLength) {
            throw new IllegalStateException();
        }

        if (i < 0 || i > sliceLength) {
            throw new ArrayIndexOutOfBoundsException();
        } 
        return buf[pos+i];
    }

    /**
     * Returns an iterator over this slice characters.
     *
     * @return an Iterator
     * 
     * @throws IllegalStateException  if an amount of characters in this slice is less 
     *                                than specified due to end of stream
     */
    @Override
    public Iterator<Character> iterator() {
        if (size - pos < sliceLength) {
            throw new IllegalStateException();
        }

        return new CharSlicerIterator();
    }

    /**
     * Converts this {@code CharSlicer} to a new character array.
     *
     * @return  a newly allocated character array whose length is the length
     *          of this slice and whose contents are initialized to contain
     *          the character sequence represented by this {@code CharSlicer}
     *          instance
     * 
     * @throws IllegalStateException  if an amount of characters in this slice is less 
     *                                than specified due to end of stream
     * 
     * @see Arrays
     */
    public char[] toCharArray() {
        if (size - pos < sliceLength) {
            throw new IllegalStateException();
        }

        return Arrays.copyOfRange(buf, pos, pos+sliceLength);
    }

    /**
     * Returns an absolute position of this slice in the text.
     * 
     * @return an absolute position of this slice in the text
     */
    public int getAbsolutePosition() {
        return absolutePos;
    }
}
