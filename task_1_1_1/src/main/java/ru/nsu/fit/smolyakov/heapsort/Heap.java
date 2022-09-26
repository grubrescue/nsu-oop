package ru.nsu.fit.smolyakov.heapsort;

/**
 * Contains a binary heap structure.
 * Nevertheless, only heapsort-required methods are implemented 
 * and the only public method is sort.
 */
public class Heap {
    private int[] heap;
    private int size;

    /**
     * Methods belong to this class are implemented only for the internal use,
     * so all checks are skipped.
     * Input parameters are guaranteed to be correct.
     */
    private static class PrimitivesArray {
        private static void swap(int[] arr, int i, int j) {
            final int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        
        private static void reverse(int[] arr) {
            for (int i = 0; i < arr.length/2; i++) {
                swap(arr, i, arr.length-i-1);
            }
        }
    }

    private void siftDown(int parent) {
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

    private void buildHeap() {
        for (int i = heap.length/2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    private Heap(int[] arr) throws IllegalArgumentException {
        if (arr == null) {
            throw new IllegalArgumentException("Input array has to exist");
        }

        heap = arr;
        size = heap.length;
        buildHeap();
    }

    private int extractMin() {
        int rootValue = heap[0];

        size--;
        PrimitivesArray.swap(heap, 0, size);
        siftDown(0);

        return rootValue;
    }

    /**
     * Arranges an array in a non-descending order.
     * Method overrides source data.
     * <p>
     * Sorting is implemented with the use of binary heap data structure.
     * 
     * @param  arr  an input array 
     * @return      reference to the source and, accordingly, resulting array
     * 
     * @throws IllegalArgumentException  if input array is null
     */
    public static int[] sort(int[] arr) throws IllegalArgumentException {
        Heap heap;

        try {
            heap = new Heap(arr);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        for (int i = 0; i < arr.length; i++) {
            heap.extractMin();
        }
        PrimitivesArray.reverse(arr);

        return arr;
    }
}