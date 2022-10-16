package ru.nsu.fit.smolyakov.substringfinder.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

import ru.nsu.fit.smolyakov.substringfinder.AbstractSubstringFinder;
import ru.nsu.fit.smolyakov.substringfinder.CharSlicer;

/**
* A class which purpose is to find a needle (specified substring) in a haystack (supplied text).
* 
* As well as Knuth-Morris-Pratt algorithm, two-way search is effective when finding 
* a pattern which is quite long, besides it does preprocessing in the
* same {@code O(n)} time, where {@code n} is a length of a needle, however, only 
* {@code O(1)} additional memory is used.
* 
* Reuses {@code needle} and {@code haystack} initialized by the default constructor of 
* {@link AbstractSubstringFinder}.
*/
public class TwoWaySubstringFinder extends AbstractSubstringFinder {
    /**
     * Constructs a new instance of {@code TwoWaySubstringFinder}. 
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
    public TwoWaySubstringFinder(InputStream source, String needleString) throws IOException {
        super(source, needleString);
    }

    private record Factorization(int idx, int period) {}

    // protected char[] needle is used here 
    private Factorization findMaxSuffix(Comparator<Character> comparator) {
        int period = 1; // a local period of a factorization
        int k = 1;
        int j = 0;
        int idx = -1; // an index at which we split a needle

        while (j + k < needle.length) {
            var comparisonResult = comparator.compare(needle[j + k], needle[idx + k]);
            if (comparisonResult < 0) {
                j += k;
                k = 1;
                period = j - idx;
            } else if (comparisonResult == 0) {
                if (k == period) {
                    j += period;
                    k = 1;
                } else {
                    k++;
                }
            } else {
                idx = j;
                j++;
                period = 1;
                k = 1;
            }
        }

        return new Factorization(idx, period);
    }

    private Factorization criticalFactorization() {
        Factorization naturalOrder = findMaxSuffix(Comparator.naturalOrder());
        Factorization reverseOrder = findMaxSuffix(Comparator.reverseOrder());

        return naturalOrder.idx() > reverseOrder.idx() ? naturalOrder : reverseOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> find() throws IOException {
        List<Integer> listOfOccurences = new ArrayList<>();

        var factorization = criticalFactorization();

        int idx = factorization.idx();
        int period = factorization.period();

        // period of second part of needle is equal to global period
        if (Arrays.equals(needle, 0, idx + 1, needle, period, period + idx + 1)) {
            int mem = -1;

            while (haystack != null) {
                int i = Integer.max(idx, mem) + 1;

                while (i < needle.length && needle[i] == haystack.get(i)) {
                    i++;
                }

                if (i >= needle.length) {
                    i = idx;

                    while (i > mem && needle[i] == haystack.get(i)) {
                        i--;
                    }

                    if (i <= mem) {
                        listOfOccurences.add(haystack.getAbsolutePosition());
                    }

                    haystack = haystack.shift(period);
                    mem = needle.length - period - 1;
                } else {
                    haystack = haystack.shift(i - idx);
                    mem = -1;
                }
            }
        } else {
            period = Integer.max(idx + 1, needle.length - idx - 1) + 1;

            while (haystack != null) {
                int i = idx + 1;

                while (i < needle.length && needle[i] == haystack.get(i)) {
                    i++;
                }

                if (i >= needle.length) {
                    i = idx;

                    while (i >= 0 && needle[i] == haystack.get(i)) {
                        i--;
                    }

                    if (i < 0) {
                        listOfOccurences.add(haystack.getAbsolutePosition());
                    }

                    haystack = haystack.shift(period);
                } else {
                    haystack = haystack.shift(i - idx);
                }
            }
        }

        return listOfOccurences;
    }
}
