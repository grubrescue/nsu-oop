package ru.nsu.fit.smolyakov.tableprinter.implementations;

import lombok.Setter;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlTablePrinter implements TablePrinter {
    private final File file;

    @Setter
    private String title = "(no title)";
    private final List<List<String>> table
        = new ArrayList<>(); // inner are rows, outer are columns

    public HtmlTablePrinter(String filename) {
        this(new File(filename));
    }

    public HtmlTablePrinter(File file) {
        this.file = file;
    }

    private static String convertMultilineCell(String cell) {
        return cell.replace("\n", "<p>");
    }

    @Override
    public void appendRow(List<String> cells) {
        var convertedRow = cells.stream()
            .map(HtmlTablePrinter::convertMultilineCell)
            .toList();

        table.add(convertedRow);
    }

    @Override
    public void print() {
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>" + title + "</title>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<table border=\"1\">\n");

            for (var row : table) {
                writer.write("<tr>\n");
                for (var cell : row) {
                    writer.write("<td>" + cell + "</td>\n");
                }
                writer.write("</tr>\n");
            }

            writer.write("</table>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear () {
        table.clear();
        title = "(no title)";
    }
}
