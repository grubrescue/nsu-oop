package ru.nsu.fit.smolyakov.primes;

public class Util {
    public static boolean isPrime(int number) {
        for (int i = 2; i*i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }
}
