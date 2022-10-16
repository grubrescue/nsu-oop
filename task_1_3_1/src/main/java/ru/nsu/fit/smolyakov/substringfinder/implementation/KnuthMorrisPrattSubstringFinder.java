package ru.nsu.fit.smolyakov.substringfinder.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import ru.nsu.fit.smolyakov.substringfinder.AbstractSubstringFinder;
import ru.nsu.fit.smolyakov.substringfinder.CharSlicer;

/**
* A class which purpose is to find a needle (specified substring) in a haystack (supplied text).
* 
* <p>Implements Knute-Morris-Pratt substring search algorithm, which is effective while finding
* large patterns with a small alphabet. Does preprocessing in a linear time: it costs {@code O(n)}, 
* where {@code n} is a needle length, {@code O(n)} additional memory is used as well.
* 
* <p>Reuses {@code needle} and {@code haystack} initialized by the default constructor of 
* {@link AbstractSubstringFinder}.
*/
public class KnuthMorrisPrattSubstringFinder extends AbstractSubstringFinder {
    /**
     * Constructs a new instance of {@code KnuthMorrisPrattSubstringFinder}. 
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
    public KnuthMorrisPrattSubstringFinder(InputStream source, String needleString) 
                                                                throws IOException {
        super(source, needleString);
    }

    private int[] calculatePrefixArray() {
        int[] prefixArray = new int[needle.length];

        prefixArray[0] = 0;

        for (int i = 1; i < needle.length; i++) {
            var cur = prefixArray[i - 1];

            while (cur > 0  
                   && prefixArray[i] != prefixArray[cur]) {
                cur = prefixArray[cur - 1];
            }

            if (needle[i] == needle[cur]) {
                cur++;
            }

            prefixArray[i] = cur;
        }
        return prefixArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> find() throws IOException {
        List<Integer> listOfOccurences = new ArrayList<>();

        var prefix = calculatePrefixArray();
        
        int i = 0;
        while (haystack != null) {
            if (i < needle.length && needle[i] == haystack.get(i)) {
                i++;
            } else if (i > 0) {
                if (i == needle.length) {
                    listOfOccurences.add(haystack.getAbsolutePosition());
                }

                var idPrev = i;
                i = prefix[i - 1];

                haystack = haystack.shift(idPrev - i);
            } else if (i == 0) {
                haystack = haystack.shift();
            }
        }

        return listOfOccurences;
    }
    
}
