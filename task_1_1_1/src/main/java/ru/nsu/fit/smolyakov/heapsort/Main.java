package ru.nsu.fit.smolyakov.heapsort;

import java.util.Arrays;
import java.util.stream.*;

/**
 * Contains only main() method.
 */
public class Main {
    /**
     * Reads command line arguments, 
     * parses them as Integer objects (if they are),
     * then heap-sorts the sequence
     * and prints it to stdout.
     * 
     * @param  args  the command line arguments
     * @throws NumberFormatException  when one of args is not an Integer number
     **/
    public static void main(String[] args) throws NumberFormatException {
        Stream.of(Arrays.stream(args)
              .mapToInt(Integer::parseInt))
              .map(IntStream::toArray)
              .map(Heap::sort)
              .map(Arrays::toString)
              .forEach(System.out::println);
    }
}
