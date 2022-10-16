package ru.nsu.fit.smolyakov.graph.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import ru.nsu.fit.smolyakov.graph.Edge;
import ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph;

/**
 * Provides a parser for specified {@link FileReader} containing graph specification
 * represented by an edges set.
 * 
 * <p>{@link getGraphSets} method returns parsed data represented in Java collections.
 * 
 * <p>{@link toGraph} method instanciates an 
 * {@link ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph} with {@link java.lang.String} as a class parameter.
 * 
 * @see java.util.Collection
 * @see java.util.Set
 * @see java.lang.String
 * 
 * @see ru.nsu.fit.smolyakov.graph.Edge
 * @see ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph
 */
public class EdgesSetParser {
    private Set<String> verticesSet = new HashSet<>();
    private Set<Edge<String>> edgesSet = new HashSet<>();

    private Pattern verticesSetPattern = Pattern.compile("([a-zA-Z0-9!]+)");
    private Pattern edgesSetPattern = Pattern.compile("(?<from>\\w+) -> (?<to>\\w+) : (?<weight>\\d+)");

    private void parse(BufferedReader reader) throws DataFormatException, IOException {
        String verticesString = reader.readLine();

        if (verticesString == null) {
            return;
        }

        Matcher verticesMatcher = verticesSetPattern.matcher(verticesString);
        while (verticesMatcher.find()) {
            if (verticesMatcher.group().equals("!nil")) {
                verticesSet.add(null);
            } else {
                verticesSet.add(verticesMatcher.group());
            }
        }
        
        String edgesString;
        do {
            edgesString = reader.readLine();
            if (edgesString == null) {
                break;
            }
            if (edgesString.isBlank()
                || edgesString.startsWith("#")) {
                    continue;
            } 

            Matcher edgesMatcher = edgesSetPattern.matcher(edgesString);
            if (edgesMatcher.find()) {
                var from = edgesMatcher.group("from");
                var to = edgesMatcher.group("to");
                var weight = Integer.parseInt(edgesMatcher.group("weight"));
                edgesSet.add(new Edge<>(from, to, weight));
            } else {
                throw new DataFormatException();
            }
        } while (edgesString != null);
    }

    /**
     * Constructs an edges list parser for a specified {@link FileReader} source.
     * 
     * <p>First line should contain whitespace-separated names of vertices belong to 
     * graph which is being constructed. Remaining lines have to describe a set of edges.
     * 
     * <p>For example:
     * 
     * <p>{@code a b c}
     * <p>{@code # Commentary starts with #}
     * <p>{@code a -> b : 12}
     * <p>{@code c -> a : 100500}
     * 
     * @param  source  a specified file
     * 
     * @throws FileNotFoundException  if a file is unavailable
     * @throws DataFormatException  if a specified file has incorrect format
     * @throws IOException  if it is impossible to close the {@code source}.
     */
    public EdgesSetParser(FileReader source) throws FileNotFoundException, 
                                                     IOException, 
                                                     DataFormatException {
        try (BufferedReader reader = new BufferedReader(source)) {
            parse(reader);
        } 
    }

    /**
     * Returns a set of vertices and a set of edges as a result of parsing.
     * 
     * @return a {@link Map.Entry} of set of vertices and set of edges
     */
    public Map.Entry<Set<String>, Set<Edge<String>>> getGraphSets() {
        return Map.entry(verticesSet, edgesSet);
    }

     /**
     * Instanciates an 
     * {@link ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph}
     * based on a result of parsing.
     * 
     * @return a new instance of {@link ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph}
     */
    public IncidentListGraph<String> toGraph() {
        return new IncidentListGraph<String>(verticesSet, edgesSet);
    }
}
