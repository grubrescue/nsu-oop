package ru.nsu.fit.smolyakov.heapsort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;


class HeapTest {
    static Stream<int[]> inputArrays() {
        return Stream.of(
            new int[] {666},
            new int[] {666, 0, -666},

            new int[] {1, 2, 3, 4, 5, 6},
            new int[] {2, 1, 5, 3, 4, 6},
            new int[] {6, 5, 4, 3, 2, 1},
            new int[] {1, 6, 2, 5, 3, 4},    

            new int[] {1, 2, 3, 4, 5},
            new int[] {2, 1, 5, 3, 4},
            new int[] {5, 4, 3, 2, 1},
            new int[] {1, 2, 5, 3, 4},    

            new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, -1, 1, 0},

            new int[100500]
        );
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("inputArrays")
    void validInputTests(int[] arr) {
        var arrCopy = arr.clone();

        Arrays.sort(arr);
        Heap.sort(arrCopy);
        
        assertArrayEquals(arr, arrCopy);
    }

    @Test
    void nullInputTest() {
        assertThrows(NullPointerException.class, () -> Heap.sort(null));
    }
}
