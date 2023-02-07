import ru.nsu.fit.smolyakov.primes.Util;

public class TestArrayGenerator {
    final static int ARRAY_SIZE=7500000;
    final static int MAX_NUMBER=8000000;

    private static int[] arr = new int[ARRAY_SIZE];
    static {
        int cnt = 0;
        for (int i = 4; i < MAX_NUMBER; i++) {
            if (Util.isPrime(i)) {
                arr[cnt++] = i;
            }
        }
        arr[cnt++] = Integer.MAX_VALUE-1;
        System.out.println(cnt);
    }

    public static int[] generate() {
        return arr;
    }
}
