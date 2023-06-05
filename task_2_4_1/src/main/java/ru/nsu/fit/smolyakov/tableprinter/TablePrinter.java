package ru.nsu.fit.smolyakov.tableprinter;

import java.io.IOException;
import java.util.List;

/**
 * Interface for printing tables.
 */
public interface TablePrinter {
    /**
     * Sets the title of the table.
     *
     * @param title title
     */
    void setTitle(String title);

    /**
     * Appends a row to the table.
     *
     * @param cells cells of the row
     */
    void appendRow(List<String> cells);

    /**
     * Prints the table.
     *
     * @throws IOException if an I/O error occurs
     */
    void print() throws IOException;

    /**
     * Clears the table.
     */
    void clear();
}
