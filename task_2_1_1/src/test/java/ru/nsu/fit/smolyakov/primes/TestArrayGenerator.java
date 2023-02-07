package ru.nsu.fit.smolyakov.primes;

import ru.nsu.fit.smolyakov.primes.Util;

class TestArrayGenerator {
    final static int ARRAY_SIZE = 7460222;
    final static int MAX_NUMBER = 8000000;

    private static final int[] arr = new int[ARRAY_SIZE];

    static {
        int cnt = 0;
        for (int i = 4; i < MAX_NUMBER; i++) {
            if (!Util.isPrime(i)) {
                arr[cnt++] = i;
            }
        }
        arr[cnt] = 31;
//        System.out.println("Actual size of array is " + cnt);
    }

    public static int[] generate() {
        return arr;
    }
}
