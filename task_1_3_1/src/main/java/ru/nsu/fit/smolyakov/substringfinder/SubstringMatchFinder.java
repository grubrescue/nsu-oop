package ru.nsu.fit.smolyakov.substringfinder;

import java.io.IOException;
import java.util.List;

/**
 * An interface for classes whose purpose is to find a needle 
 * (specified substring) in a haystack (supplied text).
 * 
 * <p>Consists of only one method {@link find}.
 * 
 * @see AbstractSubstringFinder
 */
public interface SubstringMatchFinder {
    /**
     * Proceeds a search of a substring in a supplied text.
     * May be called only once. 
     * 
     * @return a list of indices where the substring was found
     * @throws IOException if a stream is unavailable
     */
    public List<Integer> find() throws IOException;
}