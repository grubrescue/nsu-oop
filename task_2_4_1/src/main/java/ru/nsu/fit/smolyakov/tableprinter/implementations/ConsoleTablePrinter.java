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

    public final static String CELL_SEPARATOR = " | ";

    private static String getCellFormat(int width) {
        return "%-" + width + "." + width + "s";
    }

    private final List<List<String>> table
        = new ArrayList<>(); // inner are rows, outer are columns

    @Setter
    private String title = "(no title)";



    public ConsoleTablePrinter() {
        this(System.out);
    }

    public ConsoleTablePrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public ConsoleTablePrinter(String fileName) throws FileNotFoundException {
        this(new PrintStream(fileName));
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
        printStream.println(title);

        int columnsAmount = table.stream()
            .map(List::size)
            .max(Integer::compareTo)
            .orElse(0);

        List<Integer> columnWidths = new ArrayList<>(columnsAmount);

        for (int i = 0; i < columnsAmount; i++) {
            columnWidths.add(1);
        }
//
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

    @Override
    public void clear() {
        table.clear();
        title = "cleared title";
    }
}
