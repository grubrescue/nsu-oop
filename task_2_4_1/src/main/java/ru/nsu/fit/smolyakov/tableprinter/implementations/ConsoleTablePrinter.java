package ru.nsu.fit.smolyakov.tableprinter.implementations;

import lombok.NonNull;
import lombok.Setter;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@NonNull
public class ConsoleTablePrinter implements TablePrinter {
    private final PrintStream printStream;
    private final String cellFormat;

    public final static int DEFAULT_CELL_LENGTH = 12;
    public final static String CELL_SEPARATOR = " | ";

    private final List<List<String>> table
        = new ArrayList<>(); // inner are rows, outer are columns

    @Setter
    private String title = "(no title)";

    public ConsoleTablePrinter() {
        this(System.out);
    }

    public ConsoleTablePrinter(int cellWidth) {
        this(System.out, cellWidth);
    }

    public ConsoleTablePrinter(PrintStream printStream) {
        this(printStream, DEFAULT_CELL_LENGTH);
    }

    public ConsoleTablePrinter(String fileName) throws FileNotFoundException {
        this(new PrintStream(fileName));
    }

    public ConsoleTablePrinter(PrintStream printStream, int cellWidth) {
        this.printStream = printStream;
        this.cellFormat = "%-" + cellWidth + "." + cellWidth + "s";
    }

    public ConsoleTablePrinter(String fileName, int cellWidth) throws FileNotFoundException {
        this(new PrintStream(fileName), cellWidth);
    }

    private static List<String> tokenizeMultilineCell(String cell) {
        return List.of(cell.split("\n"));
    }

    /**
     * Assume we have a list like this:
     * [1 2 3]
     * [4 5]
     * [6 7 8]
     *
     * We want to transpose it to:
     * [1 4 6]
     * [2 5 7]
     * [3   8]
     *
     * @return
     */
    private static List<List<String>> transpose(List<List<String>> table) {
        var result = new ArrayList<List<String>>();

        int maxRowLen = table.stream()
            .map(List::size)
            .max(Integer::compareTo)
            .orElse(0);

        for (int i = 0; i < maxRowLen; i++) {
            var row = new ArrayList<String>();
            for (List<String> stringList : table) {
                if (stringList.size() > i) {
                    row.add(stringList.get(i));
                } else {
                    row.add("");
                }
            }
            result.add(row);
        }
        return result;
    }

    // trreats \n as a new row
    @Override
    public void appendRow(List<String> cells) {
        var initialSize = table.size();

        var rows = cells.stream()
            .map(ConsoleTablePrinter::tokenizeMultilineCell)
            .toList();

        table.addAll(transpose(rows));
    }

    @Override
    public void print() {
        for (List<String> row : table) {
            for (int col = 0; col < row.size(); col++) {
                printStream.printf(cellFormat, row.get(col));
                if (col != row.size() - 1) {
                    printStream.print(CELL_SEPARATOR);
                }
            }
            printStream.println();
        }
    }
}
