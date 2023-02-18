package ru.nsu.fit.smolyakov.primes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class JmhSources {
    public static final int[] smallestArrayTrue = scanArray(new File("src/test/resources/smallerdata_true.txt"));
    public static final int[] smallArrayFalse = scanArray(new File("src/test/resources/smalldata_false.txt"));
    public static final int[] largeArrayTrue = scanArray(new File("src/test/resources/largedata_true.txt"));

    private static int[] scanArray(File file) {
        try (Scanner scanner = new Scanner(file)) {
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
}
