package ru.nsu.fit.smolyakov.graph.adjacency_matrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import ru.nsu.fit.smolyakov.graph.AbstractGraph;
import ru.nsu.fit.smolyakov.graph.Edge;


/**
 * Mutable adjacency matrix graph representation which is not memory-effective
 * in most cases, but allows to operate with edges in a constant time.
 * Implements all methods of {@link AbstractGraph}.
 * 
 * <p>It's recommended to use this representation only if the graph is dense.
 * 
 * <p>Can be instanciated from a file input using 
 * {@link ru.nsu.fit.smolyakov.graph.parser.AdjacencyMatrixParser}.
 * 
 * @see ru.nsu.fit.smolyakov.graph.Graph
 * @see ru.nsu.fit.smolyakov.graph.AbstractGraph
 * @see ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph
 * @see ru.nsu.fit.smolyakov.graph.parser.AdjacencyMatrixParser
 */
public class AdjacencyMatrixGraph<V> extends AbstractGraph<V>{
    private IntegerMatrix matrix = new IntegerMatrix();
    private Map<V, Integer> vertexToInteger = new HashMap<>();

    /**
     * A no-args constructor for an empty graph.
     */
    public AdjacencyMatrixGraph() {
        graphInstance();
    }

    /**
     * An edges set constructor with a specified
     * set of vertices. It allows terminal vertices to exist.
     * 
     * @param verticesSet a set of vertices
     * @param edgesSet a set of edges
     */
    public AdjacencyMatrixGraph(Set<V> verticesSet, Set<Edge<V>> edgesSet) {
        graphInstance(verticesSet, edgesSet);
    }

    /**
     * An adjacency matrix constructor. Each vertex from specified
     * {@code verticesList} is mapped into an index according to its 
     * position in the list, so the order in {@code verticesList} is considerable.
     * 
     * @param verticesList an ordered list of vertices
     * @param matrix an adjacency matrix
     * 
     * @throws IllegalArgumentException if size of list is not equal to any of
     *                                  matrix dimensions
     */
    public AdjacencyMatrixGraph(List<V> verticesList, Integer[][] matrix) {
        graphInstance(verticesList, matrix);
    }

    /**
     * Operation is unsupported.
     * 
     * @throws UnsupportedOperationException always
     */
    @Override
    protected void graphInstance(Set<Edge<V>> edgesSet) {
        throw new UnsupportedOperationException("unsupported");
    }

    /**
     * A template for an adjacency matrix constructor. Each vertex from 
     * {@code verticesList} is mapped into index according to it's 
     * position in the set, so the order in {@code verticesList} is significant.
     * 
     * <p>Be sure that this method is called by inheritor of this class
     * at the moment when graph is clear and {@link addVertex} and {@link addEdge} 
     * methods are ready to use.
     *
     * @param verticesList a set of vertices
     * @param matrix an adjacency matrix
     */
    @Override
    protected void graphInstance(List<V> verticesList, Integer[][] matrix) {
        verticesList.stream()
            .forEachOrdered(this::addVertex);

        int size = matrix.length;
        if (size != verticesList.size()) {
            throw new IllegalArgumentException("size of list is not equal to one of matrix dim");
        }
        
        if (matrix[0].length != size) {
            throw new IllegalArgumentException("input 2d array is not a matrix");
        }

        this.matrix = new IntegerMatrix(matrix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean vertexExists(V vertex) {
        return vertexToInteger.containsKey(vertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Edge<V>> getEdge(V from, V to) {
        var fromId = vertexToInteger.get(from);
        var toId = vertexToInteger.get(to);

        if (fromId != null && toId != null) {
            return matrix.getValue(fromId, toId)
                .stream()
                .mapToObj((w) -> new Edge<V>(from, to, w))
                .findAny();
        } else {
            return Optional.empty();
        }
    }

    /**
     * Inserts a single vertex to this graph.
     * 
     * @param  vertex  a vertex to add
     * @return {@code true} if vertex successfully added, 
     *         {@code false} if it already existed.
     */
    @Override
    public boolean addVertex(V vertex) {
        if (vertexExists(vertex)) {
            return false;
        } else {
            vertexToInteger.put(vertex, matrix.getSize());
            matrix.extend();
            return true;
        }
    }

    /**
     * Removes a single vertex from this graph, if one exists.
     * 
     * @param  vertex  a vertex to remove
     * @return {@code true} if vertex successfully removed,
     *         {@code false} if it's not presented in this graph.
     */
    @Override
    public boolean removeVertex(V vertex) {
        if (vertexToInteger.remove(vertex) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Adds a single edge to this graph.
     * 
     * <p>If this edge already exists, 
     * this method does nothing and returns {@code false}. 
     * 
     * <p>If a vertex specified by {@code from} or {@code to} doesn't exist,
     * this method also returns {@code false}. 
     * 
     * @param  edge  a new {@link Edge} 
     * 
     * @return {@code true} if an edge is successfully added,
     *         {@code false} otherwise
     */
    @Override
    public boolean addEdge(Edge<V> edge) {
        var fromId = vertexToInteger.get(edge.from());
        var toId = vertexToInteger.get(edge.to());

        if (fromId != null && toId != null) {
            matrix.setValue(fromId, toId, edge.weight());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a single edge from this graph.
     * If edge doesn't exist, this method does nothing and 
     * returns {@link OptionalInt#empty()}.
     * 
     * @param  from  a vertex a new edge comes from
     * @param  to  a vertex a new edge comes to
     * 
     * @return {@link OptionalInt} of old weight if an edge related to specified 
     *         vertices exists, 
     *         {@link OptionalInt#empty()} otherwise
     * 
     * @see OptionalInt
     */
    @Override
    public OptionalInt removeEdge(V from, V to) {
        var fromId = vertexToInteger.get(from);
        var toId = vertexToInteger.get(to);

        if (fromId != null && toId != null) {
            return matrix.removeValue(fromId, toId);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Edge<V>> getAdjacentEdges(V from) {
        var id = vertexToInteger.get(from);
        if (id != null) {
            return vertexToInteger.keySet()
                .stream()
                .map((to) -> getEdge(from, to))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getAllVertices() {
        return vertexToInteger.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptionalInt reweightEdge(V from, V to, int newWeight)  {
        var fromId = vertexToInteger.get(from);
        var toId = vertexToInteger.get(to);

        if (fromId != null && toId != null) {
            return matrix.setValue(fromId, toId, newWeight);
        } else {
            return OptionalInt.empty();
        }
    }   

    /**
     * Turns a graph into an empty state.
     */
    @Override
    public void clear() {
        vertexToInteger.clear();
        matrix = new IntegerMatrix();
    }
}
