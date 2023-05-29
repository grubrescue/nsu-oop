package ru.nsu.fit.smolyakov.tableprinter;

import java.util.List;

public interface TablePrinter {
    void setTitle (String title);

    void appendRow (List<String> cells);

    void print ();

    void clear ();
}
