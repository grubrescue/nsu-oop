package ru.nsu.fit.smolyakov.primes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class TestSources {
    private static int[] scanArray(File file) {
        try (Scanner scanner = new Scanner(file)){
            int arraySize = scanner.nextInt();
            int[] arr = new int[arraySize];

            for (int i = 0; i < arraySize; i++) {
                arr[i] = scanner.nextInt();
            }

            return arr;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int[] smallestArray = scanArray(new File("smallerdata.txt"));
    public static final int[] smallArray = scanArray(new File("smalldata.txt"));
    public static final int[] largeArray = scanArray(new File("largedata.txt"));

    public static final NonPrimeFinder sequentialStreamNonPrimeFinder
        = new SequentialStreamNonPrimeFinder();
    public static final NonPrimeFinder parallelStreamNonPrimeFinder
        = new ParallelStreamNonPrimeFinder();
    public static final NonPrimeFinder parallelThreadsNonPrimeFinder
        = new ParallelThreadsNonPrimeFinder(Runtime.getRuntime().availableProcessors());
}
