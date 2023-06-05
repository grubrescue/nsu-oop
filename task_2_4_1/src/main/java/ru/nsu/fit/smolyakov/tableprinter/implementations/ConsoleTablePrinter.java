package ru.nsu.fit.smolyakov.tableprinter.implementations;

import lombok.NonNull;
import lombok.Setter;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Table printer that prints to console.
 */
@NonNull
public class ConsoleTablePrinter implements TablePrinter {
    /**
     * Separator between cells.
     */
    public final static String CELL_SEPARATOR = " | ";

    private final PrintStream printStream;
    private final List<List<String>> table
        = new ArrayList<>(); // inner are rows, outer are columns

    @NonNull
    @Setter
    private String title = "(no title)";

    /**
     * Creates a new console table printer.
     * Prints to {@link System#out}.
     */
    public ConsoleTablePrinter() {
        this(System.out);
    }

    /**
     * Creates a new console table printer.
     * Prints to given print stream.
     *
     * @param printStream print stream to print to
     */
    public ConsoleTablePrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * Creates a new console table printer.
     * Prints to given file.
     *
     * @param fileName file to print to
     * @throws FileNotFoundException if the file exists but is a directory
     *                               rather than a regular file, does not exist but cannot be created,
     *                               or cannot be opened for any other reason
     */
    public ConsoleTablePrinter(String fileName) throws FileNotFoundException {
        this(new PrintStream(fileName));
    }

    private static String getCellFormat(int width) {
        return "%-" + width + "." + width + "s";
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
     * <p>We want to transpose it to:
     * [1 4 6]
     * [2 5 7]
     * [3   8]
     *
     * @return transposed list
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

    /**
     * Appends a row to the table.
     * Multiline cells are supported (lines split by {@code \n}
     * character turns into separate rows).
     *
     * @param cells cells of the row
     */
    @Override
    public void appendRow(List<String> cells) {
        var rows = cells.stream()
            .map(ConsoleTablePrinter::tokenizeMultilineCell)
            .toList();

        table.addAll(transpose(rows));
    }

    /**
     * Prints the table.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void print() throws IOException {
        printStream.println(title);

        int columnsAmount = table.stream()
            .map(List::size)
            .max(Integer::compareTo)
            .orElse(0);

        List<Integer> columnWidths = new ArrayList<>(columnsAmount);

        for (int i = 0; i < columnsAmount; i++) {
            columnWidths.add(1);
        }

        for (List<String> rows : table) {
            for (int columnNo = 0; columnNo < rows.size(); columnNo++) {
                var newWidth = rows.get(columnNo).length();
                if (newWidth > columnWidths.get(columnNo)) {
                    columnWidths.set(columnNo, newWidth);
                }
            }
        }

        for (List<String> row : table) {
            for (int columnNo = 0; columnNo < row.size(); columnNo++) {
                printStream.printf(
                    getCellFormat(columnWidths.get(columnNo)),
                    row.get(columnNo)
                );
                if (columnNo != row.size() - 1) {
                    printStream.print(CELL_SEPARATOR);
                }
            }
            printStream.println();
        }

        printStream.println();
    }

    /**
     * Clears the table.
     */
    @Override
    public void clear() {
        table.clear();
        title = "(title cleared)";
    }
}
