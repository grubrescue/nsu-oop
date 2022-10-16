package ru.nsu.fit.smolyakov.substringfinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.List;

/**
 * A template for a class implementing {@link SubstringMatchFinder} interface.
 * 
 * <p>Provides protected {@code needle} and {@code haystack} fields, initialized by
 * {@link AbstractSubstringFinder#AbstractSubstringFinder(InputStream, String)}
 * constructor. First one is an array of chars to find in text, being converted from a {@link String}.
 * Second one is an instance of {@link CharSlicer} class, which gives a programmer
 * functionality of a sliding window over the supplied text.
 */
public abstract class AbstractSubstringFinder implements SubstringMatchFinder {
    /**
     * A needle to find in a haystack.
     * Initialized by {@link AbstractSubstringFinder#AbstractSubstringFinder(InputStream, String)}
     * consttructor.
     */
    protected char[] needle;

    /**
     * A sliding window over the haystack where the needle have to be found.
     * Initialized by {@link AbstractSubstringFinder#AbstractSubstringFinder(InputStream, String)}
     * consttructor.
     */
    protected CharSlicer haystack;

    /**
     * Constructs a new instance of {@code AbstractSubstringFinder}. 
     * 
     * @param source  a stream containing UTF-8-encoded text
     * @param needleString  a string to find in a text provided by {@code source}.
     * 
     * @throws IOException  if {@code source} is unavailable
     * @throws InputMismatchException  if a {@code needleString} is longer than a text
     *                                 supplied by a {@code source}
     * 
     * @see CharSlicer
     */
    public AbstractSubstringFinder(InputStream source, String needleString) throws IOException {
        if (needleString == null 
            || needleString.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        needle = needleString.toCharArray();

        try {
            haystack = new CharSlicer(new InputStreamReader(source, "UTF-8"), needle.length);
        } catch (Exception e) {
            haystack = null;
        } 
    }

    /**
     * {@inheritDoc}
     */
    public abstract List<Integer> find() throws IOException;
}

