package ru.nsu.fit.smolyakov.substringfinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import ru.nsu.fit.smolyakov.substringfinder.implementation.KnuthMorrisPrattSubstringFinder;

/**
 * A class containing main method.
 */
public class Main {
    /**
     * Reads a filename and a substring to search from {@code stdin}
     * and proceeds a search using 
     * {@link ru.nsu.fit.smolyakov.substringfinder.implementation.KnuthMorrisPrattSubstringFinder}.
     * 
     * @param  args  unused
     * @throws IOException if a file is unavailable
      */
    public static void main(String[] args) throws IOException {
        String filename;
        String needle;

        try (Scanner scanner = new Scanner(System.in)) {
            filename = scanner.next();
            needle = scanner.next();
        } 

        SubstringMatchFinder finder = 
            new KnuthMorrisPrattSubstringFinder(new FileInputStream(filename), needle);
        System.out.println(finder.find());
    }
}
