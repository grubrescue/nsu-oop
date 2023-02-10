package ru.nsu.fit.smolyakov.primes;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PrimeFinderTests {
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

    private static final int[] smallestArray = scanArray(new File("smallerdata.txt"));
    private static final int[] smallArray = scanArray(new File("smalldata.txt"));
    private static final int[] largeArray = scanArray(new File("largedata.txt"));

    private static final NonPrimeFinder sequentialStreamNonPrimeFinder
        = new SequentialStreamNonPrimeFinder();
    private static final NonPrimeFinder parallelStreamNonPrimeFinder
        = new ParallelStreamNonPrimeFinder();
    private static final NonPrimeFinder parallelThreadsNonPrimeFinder
        = new ParallelThreadsNonPrimeFinder(Runtime.getRuntime().availableProcessors());

    @Test
    void sequentialStreamNonPrimeFinderTest() {
        assertThat(sequentialStreamNonPrimeFinder.find(smallestArray)).isTrue();
        assertThat(sequentialStreamNonPrimeFinder.find(smallArray)).isFalse();
        assertThat(sequentialStreamNonPrimeFinder.find(largeArray)).isFalse();
    }

    @Test
    void parallelStreamNonPrimeFinderTest() {
        assertThat(parallelStreamNonPrimeFinder.find(smallestArray)).isTrue();
        assertThat(parallelStreamNonPrimeFinder.find(smallArray)).isFalse();
        assertThat(parallelStreamNonPrimeFinder.find(largeArray)).isFalse();
    }

    @Benchmark
    void sequentialStreamNonPrimeFinderBench() {
        assertThat(sequentialStreamNonPrimeFinder.find(smallestArray)).isTrue();
        assertThat(sequentialStreamNonPrimeFinder.find(smallArray)).isFalse();
        assertThat(sequentialStreamNonPrimeFinder.find(largeArray)).isFalse();
    }

    @Test
    void parallelStreamNonPrimeFinderBench() {
        assertThat(parallelStreamNonPrimeFinder.find(smallestArray)).isTrue();
        assertThat(parallelStreamNonPrimeFinder.find(smallArray)).isFalse();
        assertThat(parallelStreamNonPrimeFinder.find(largeArray)).isFalse();
    }
}
