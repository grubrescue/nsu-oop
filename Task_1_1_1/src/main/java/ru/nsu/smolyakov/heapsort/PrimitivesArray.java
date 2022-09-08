package ru.nsu.smolyakov.heapsort;

class PrimitivesArray {
    static void swap(int[] arr, int i, int j) {
        final int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    static void reverse(int[] arr) {
        for (int i = 0; i < arr.length/2; i++) {
            swap(arr, i, arr.length-i-1);
        }
    }
}