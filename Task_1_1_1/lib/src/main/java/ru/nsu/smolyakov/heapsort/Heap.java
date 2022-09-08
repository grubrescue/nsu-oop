package ru.nsu.smolyakov.heapsort;

class PrimitivesArray {
    static void swap (int[] arr, int i, int j) {
        final int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    static void reverse (int[] arr) {
        for (int i = 0; i < arr.length/2; i++) {
            swap(arr, i, arr.length-i-1);
        }
    }
}


public class Heap {
    private int[] heap;
    private int size;

    private void siftDown (int parent) {
        final int leftSon = 2*parent+1;
        final int rightSon = 2*parent+2;
        
        int smallest = parent; 

        if (rightSon < size) {
            smallest = heap[leftSon] < heap[rightSon] ? leftSon : rightSon;
        } else if (leftSon < size) {
            smallest = leftSon;
        }

        if (heap[smallest] < heap[parent]) {
            PrimitivesArray.swap(heap, smallest, parent);
            siftDown(smallest);
        }
    }

    private void buildHeap () {
        for (int i = heap.length/2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    Heap (int[] arr) throws NullPointerException {
        if (arr == null) {
            throw new NullPointerException("Input array have to exist");
        }

        heap = arr;
        size = heap.length;
        buildHeap();
    }

    int extractMin () {
        int rootValue = heap[0];

        size--;
        PrimitivesArray.swap(heap, 0, size);
        siftDown(0);

        return rootValue;
    }

    /**
     * Arranges an array in a non-descending order.
     * Method overrides source data.
     * 
     * The method is implemented with the use of binary heap data structure.
     * 
     * 
     * @param  arr  an input array 
     * @return      reference to the source and, accordingly, resulting array.
     */

    public static int[] sort (int[] arr) {  
        Heap heap = new Heap(arr);

        for (int i = 0; i < arr.length; i++) {
            heap.extractMin();
        }
        PrimitivesArray.reverse(arr);

        return arr;
    }
}