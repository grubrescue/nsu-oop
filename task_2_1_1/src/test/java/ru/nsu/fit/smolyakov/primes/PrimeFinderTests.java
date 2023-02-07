package ru.nsu.fit.smolyakov.primes;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.primes.*;

public class PrimeFinderTests {
//    private static final int REPEATION_TIMES = 4;
    private static final TestUtil<int[]> testUtil = new TestUtil<>();
    private static final int[] arr = TestArrayGenerator.generate();
    private static PrimeFinder sequentialStreamPrimeFinder
        = new SequentialStreamPrimeFinder();
    private static PrimeFinder parallelStreamPrimeFinder
        = new ParallelStreamPrimeFinder();
    private static PrimeFinder parallelThreadsPrimeFinder
        = new ParallelThreadsPrimeFinder(Runtime.getRuntime().availableProcessors());


    @Test
    void timeMeasureTest() {
        System.out.println("sequential is " +
            testUtil.measureExecutionTimeNanoseconds(sequentialStreamPrimeFinder::find, arr)
        );

        System.out.println("parallel stream is " +
            testUtil.measureExecutionTimeNanoseconds(parallelStreamPrimeFinder::find, arr)
        );

        System.out.println("parallel threads is " +
            testUtil.measureExecutionTimeNanoseconds(parallelThreadsPrimeFinder::find, arr)
        );
    }
}
