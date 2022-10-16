package ru.nsu.fit.smolyakov.graph.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph;

/**
 * Provides a parser for specified {@link FileReader} containing graph specification
 * represented by an adjacency matrix.
 * 
 * <p>{@link getGraphSets} method returns parsed data represented in Java collections.
 * 
 * <p>{@link toGraph} method instanciates an 
 * {@link ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph} with {@link java.lang.String} as a class parameter.
 * 
 * @see java.util.Collection
 * @see java.util.Set
 * @see java.util.Arrays
 * @see java.lang.String
 * 
 * @see ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph
 */
public class AdjacencyMatrixParser {
    private List<String> verticesList = new ArrayList<>();
    private Integer[][] matrix;

    private Pattern pattern = Pattern.compile("([a-zA-Z0-9!]+)");

    private void parse(BufferedReader reader) throws DataFormatException, IOException {
        String verticesString = reader.readLine();

        if (verticesString == null) {
            return;
        }

        Matcher verticesMatcher = pattern.matcher(verticesString);
        while (verticesMatcher.find()) {
            if (verticesMatcher.group().equals("!nil")) {
                verticesList.add(null);
            } else {
                verticesList.add(verticesMatcher.group());
            }
        }

        matrix = new Integer[verticesList.size()][verticesList.size()];
        
        String edgesString;
        int row = 0;
        do {
            edgesString = reader.readLine();

            if (edgesString == null && row == matrix.length) {
                break;
            }

            if (edgesString == null
                || row > matrix.length) {
                throw new DataFormatException();
            }

            if (edgesString.isBlank()
                || edgesString.startsWith("#")) {
                continue;
            } 

            Matcher edgesMatcher = pattern.matcher(edgesString);
            int column = 0;
            while (edgesMatcher.find()) {
                if (column >= matrix.length) {
                    throw new DataFormatException();
                }
                if (edgesMatcher.group().equals("!nil")) {
                    matrix[row][column] = null;
                } else {
                    try {
                        matrix[row][column] = Integer.parseInt(edgesMatcher.group());
                    } catch (NumberFormatException e) {
                        throw new DataFormatException();
                    }
                }
                column++;
            }

            if (column < matrix.length) {
                throw new DataFormatException();
            }
            row++;
        } while (edgesString != null);
    }

    /**
     * Constructs an adjacency matrix parser for a specified {@link FileReader} source.
     * 
     * <p>First line should contain whitespace-separated names of vertices belong to 
     * graph which is being constructed. Remaining lines have to contain an adjacency matrix.
     * 
     * <p>For example:
     * 
     * <p>{@code a !nil c}
     * <p>{@code # Commentary starts with #}
     * <p>{@code !nil    2    1}
     * <p>{@code    0    2    1}
     * <p>{@code 1234    2 9000}
     * 
     * <p>This parser tries to ignore all incorrect input, however, in some
     * cases the behaviour may be unpredictable.
     * 
     * @param  source  a specified file
     * 
     * @throws FileNotFoundException  if a file is unavailable
     * @throws DataFormatException  if specified file has incorrect format
     * @throws IOException  if it is impossible to close the {@code source}.
     */
    public AdjacencyMatrixParser(FileReader source) throws FileNotFoundException, 
                                                           IOException, 
                                                           DataFormatException {
        try (BufferedReader reader = new BufferedReader(source)) {
            parse(reader);
        } 
    }

    /**
     * Returns a set of vertices and an adjacency matrix as a result of parsing.
     * 
     * @return a {@link Map.Entry} of set of vertices and set of edges
     */
    public Map.Entry<List<String>, Integer[][]> getGraphSets() {
        return Map.entry(verticesList, matrix);
    }

    /**
     * Instanciates an 
     * {@link ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph}
     * based on a result of parsing.
     * 
     * @return a new instance of 
     * {@link ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph}
     */
    public AdjacencyMatrixGraph<String> toGraph() {
        return new AdjacencyMatrixGraph<String>(verticesList, matrix);
    }
}
