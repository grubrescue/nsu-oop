package ru.nsu.fit.smolyakov.graph.adjacency_matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

/* 
 * An extendable self-resizing implementation of a matrix.
 * For internal use only.
 */
class IntegerMatrix {
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
    private int size = 0;

    IntegerMatrix() {}

    IntegerMatrix(Integer[][] matrix) {
        size = matrix.length;
        if (matrix[0].length != size) {
            throw new IllegalArgumentException("input 2d array is not a matrix");
        }

        for (int i = 0; i < size; i++) {
            this.matrix.add(i, new ArrayList<>(Arrays.asList(matrix[i])));
        }
    }

    int extend() {
        for (var row : matrix) {
            row.add(null);
        }

        matrix.add(size, new ArrayList<>(Arrays.asList(new Integer[size+1])));
        return size++;
    }

    OptionalInt getValue(int row, int column) {
        if (row >= size || column >= size) {
            return OptionalInt.empty();
        }

        Integer val = matrix.get(row).get(column);

        if (val == null) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(val);
        }
    }

    OptionalInt setValue(int row, int column, Integer val) {
        if (row >= size || column >= size) {
            return OptionalInt.empty();
        } else {
            var oldVal = getValue(row, column);
            matrix.get(row).set(column, val);
            return oldVal;
        }
    }

    OptionalInt removeValue(int row, int column) {
        return setValue(row, column, null);
    }
    
    int getSize() {
        return size;
    }
}
