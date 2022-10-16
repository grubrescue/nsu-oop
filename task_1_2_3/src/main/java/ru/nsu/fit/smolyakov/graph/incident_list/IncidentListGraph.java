package ru.nsu.fit.smolyakov.graph.incident_list;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

import ru.nsu.fit.smolyakov.graph.AbstractGraph;
import ru.nsu.fit.smolyakov.graph.Edge;

/**
 * Mutable incident list graph representation which in general 
 * appears the most asymptotically profitable and memory-efficient.
 * Implements all methods of {@link AbstractGraph}.
 * 
 * <p>It's highly recommended to use this representation, unless the
 * graph is dense.
 * 
 * <p>Can be instanciated from a file input using 
 * {@link ru.nsu.fit.smolyakov.graph.parser.EdgesSetParser}.
 * 
 * @see ru.nsu.fit.smolyakov.graph.Graph
 * @see ru.nsu.fit.smolyakov.graph.AbstractGraph
 * @see ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph
 * @see ru.nsu.fit.smolyakov.graph.parser.EdgesSetParser
 */
public class IncidentListGraph<V> extends AbstractGraph<V> {
    private Map<V, Set<Edge<V>>> vertices = new HashMap<>();

    /**
     * A no-args constructor for an empty graph.
     */
    public IncidentListGraph() {
        graphInstance();
    }

    /**
     * An edges set constructor.
     * As a set of vertices is not specified, it's supposed that there is no 
     * terminal vertices in this graph.
     * 
     * <p>Be sure that this method is called by the inheritor of this class
     * at the moment when graph is clear and {@link addVertex} and {@link addEdge} 
     * methods are ready to use.
     * 
     * @param edgesSet a set of edges
     */
    public IncidentListGraph(Set<Edge<V>> edgesSet) {
        graphInstance(edgesSet);
    }

    /**
     * An edges set constructor with a specified
     * set of vertices. It allows terminal vertices to exist.
     * 
     * @param verticesSet a set of vertices
     * @param edgesSet a set of edges
     */
    public IncidentListGraph(Set<V> verticesSet, Set<Edge<V>> edgesSet) {
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
    public IncidentListGraph(List<V> verticesList, Integer[][] matrix) {
        graphInstance(verticesList, matrix);
    }

    /**
     * Returns this, as this object is already an instance of 
     * {@link IncidentListGraph}.
     * 
     * @return this graph
     * @see    IncidentListGraph
     */
    @Override
    public IncidentListGraph<V> toIncidentList() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean vertexExists(V vertex) {
        return vertices.containsKey(vertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Edge<V>> getEdge(V from, V to) {
        return Stream.ofNullable(vertices.get(from))
            .flatMap((set) -> set.stream())
            .filter((edge) -> edge.to() == to)
            .findAny();
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
            vertices.put(vertex, new HashSet<>());
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
        if (vertices.remove(vertex) != null) {
            getAllEdges().stream()
                         .filter((e) -> e.to().equals(vertex))
                         .forEach((e) -> removeEdge(e.from(), e.to()));
            return true;
        } else {
            return false;
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
        if (vertexExists(edge.to())
            && vertexExists(edge.from())) {
            vertices.get(edge.from()).add(edge);
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
        Set<Edge<V>> adjacentEdgesSet = vertices.get(from);

        Optional<Edge<V>> edge = Stream.ofNullable(adjacentEdgesSet)
            .flatMap((set) -> set.stream())
            .filter((e) -> e.to() == to)
            .findAny();

        edge.ifPresent(adjacentEdgesSet::remove);

        return edge.map(Edge::weight)
                   .map(OptionalInt::of)
                   .orElseGet(OptionalInt::empty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Edge<V>> getAdjacentEdges(V vertex) {
        return vertices.get(vertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getAllVertices() {
        return vertices.keySet();
    }

    /**
     * Turns a graph into an empty state.
     */
    @Override
    public void clear() {
        vertices.clear();
    }
}
