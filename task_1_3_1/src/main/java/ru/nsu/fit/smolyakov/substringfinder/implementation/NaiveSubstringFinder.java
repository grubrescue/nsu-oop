package ru.nsu.fit.smolyakov.substringfinder.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import ru.nsu.fit.smolyakov.substringfinder.AbstractSubstringFinder;
import ru.nsu.fit.smolyakov.substringfinder.CharSlicer;

/**
 * A class which purpose is to find a needle (specified substring) in a haystack (supplied text).
 * 
 * <p>Implements brute-force substring search algorithm, it's not effective one, but 
 * enough for testing and finding small patterns. Neither use any additional memory nor
 * does any preprocessing.
 * 
 * <p>Reuses {@code needle} and {@code haystack} initialized by the default constructor of 
 * {@link AbstractSubstringFinder}.
 */
public class NaiveSubstringFinder extends AbstractSubstringFinder {
    /**
     * Constructs a new instance of {@code NaiveSubstringFinder}. 
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
    public NaiveSubstringFinder(InputStream source, String needleString) throws IOException {
        super(source, needleString);
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> find() throws IOException {
        List<Integer> listOfOccurences = new ArrayList<>();

        while (haystack != null) {
            if (Arrays.equals(haystack.toCharArray(), needle)) {
                listOfOccurences.add(haystack.getAbsolutePosition());
            }
            
            haystack = haystack.shift();
        }

        return listOfOccurences;
    }
}
