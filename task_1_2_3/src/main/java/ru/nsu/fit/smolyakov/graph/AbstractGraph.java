package ru.nsu.fit.smolyakov.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph;

/**
 * This class provides a skeletal implementation of the {@link Graph} interface to minimize 
 * the effort required to implement it. 
 * 
 * <p>This abstract class allows the programmer to choose between mutable and immutable
 * graph.
 * If he prefer the second, {@link clear}, {@link addVertex}, {@link addEdge}, {@link reweightEdge}, 
 * {@link removeVertex}, {@link removeEdge} are not required to implement and may throw 
 * {@link UnsupportedOperationException}. However, all mutable methods reusing them will fail too.
 * 
 * <p>{@link vertexExists}, {@link getEdge}, {@link getAdjacentEdges} and {@link getAllVertices} must
 * be implemented anyway.
 * 
 * <p>This abstract class implementats of Dijksta's algorithm for {@link findShortestPaths}
 * method, with all inherent limitations. If they are unacceptable for a programmer, he has to override it.
 * 
 * <p>Inherited classes are supposed to reuse {@link graphInstance} methods for their constructors, as they 
 * cover all desired variants of operands describing a graph. It's not obligatorily, though.
 * 
 * <p>Many methods are implemented with reusing not numerous programmer-dependant methods, so in some cases 
 * they might be very ineffective.
 * 
 * @see Graph
 * @see ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph
 * @see ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph
 */
public abstract class AbstractGraph<V> implements Graph<V> {
    /**
     * A template for a no-args constructor, does nothing
     * in a default implementation.
     */
    protected void graphInstance() {}

    /**
     * A template for an edges set constructor.
     * As a set of vertices is not specified, it's supposed that there is no 
     * terminal vertices in this graph.
     * 
     * <p>Be sure that this method is called by the inheritor of this class
     * at the moment when graph is clear and {@link addVertex} and {@link addEdge} 
     * methods are ready to use.
     * 
     * @param edgesSet a set of edges
     * 
     * @throws UnsupportedOperationException  if this graph doesn't support this
     *                                        representation or immutable
     */
    protected void graphInstance(Set<Edge<V>> edgesSet) {
        edgesSet.stream() 
                .peek((e) -> this.addVertex(e.from()))
                .peek((e) -> this.addVertex(e.to()))
                .forEach(this::addEdge);
    }

    /**
     * A template for an edges set constructor with a specified
     * set of vertices. It allows terminal vertices to exist.
     * 
     * <p>If one of edges specified in edges set is related to nonexistant 
     * vertex, it won't be added.
     * 
     * <p>Be sure that this method is called by the inheritor of this class
     * at the moment when graph is clear and {@link addVertex} and {@link addEdge} 
     * methods are ready to use.
     * 
     * @param verticesSet a set of vertices
     * @param edgesSet a set of edges
     * 
     * @throws UnsupportedOperationException  if this graph doesn't support this
     *                                        representation or immutable
     */
    protected void graphInstance(Set<V> verticesSet, Set<Edge<V>> edgesSet) {
        verticesSet.stream()
            .forEachOrdered(this::addVertex);
        edgesSet.stream()
            .forEachOrdered(this::addEdge);
    }

    /**
     * A template for an adjacency matrix constructor. Each vertex from specified
     * {@code verticesList} is mapped into an index according to its 
     * position in the list, so the order in {@code verticesList} is considerable.
     * 
     * <p>Be sure that this method is called by inheritor of this class
     * at the moment when graph is clear and {@link addVertex} and {@link addEdge} 
     * methods are ready to use.
     *
     * @param  verticesList an ordered list of vertices
     * @param  matrix an adjacency matrix
     * 
     * @throws IllegalArgumentException if size of list is not equal to any of
     *                                  matrix dimensions
     * @throws UnsupportedOperationException  if this graph doesn't support this
     *                                        representation or immutable
     */
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
    
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (matrix[row][column] != null) {
                    addEdge(verticesList.get(row), verticesList.get(column), matrix[row][column]);
                }
            }
        }
    }

    /**
     * Turns this {@link AbstractGraph} into {@link IncidentListGraph},
     * as in general it's the most asymptotically profitable.
     * 
     * <p>Default implementation creates an empty instance of incident list graph
     * and than uses it as a parameter for {@link switchGraphRepresentation}.
     * 
     * @return this graph, represented as an incident list
     * @see    IncidentListGraph
     */
    public IncidentListGraph<V> toIncidentList() {
        var incidentList = new IncidentListGraph<V>();
        return (IncidentListGraph<V>) switchGraphRepresentation(incidentList);
    }

    /**
     * Turns this {@link AbstractGraph} into a specified {@link AbstractGraph}.
     * 
     * <p>In default implementation specified {@code graph} will be cleared 
     * by {@link clear} method if not empty, respectively be sure it's correct one. 
     * It's also not optimised well, so it's highly recommended to override that.
     * 
     * @param  graph  presentation to be turned into
     * @return this graph, represented as specified by {@code graph}
     * @throws UnsupportedOperationException if {@link clear}, {@link addVertex}
     *                                       or {@link addEdge} operations
     *                                       are not supported by the specified
     *                                       graph
     */
    public AbstractGraph<V> switchGraphRepresentation(AbstractGraph<V> graph) {
        if (!graph.isEmpty()) {
            graph.clear();
        }
        
        graph.graphInstance(getAllVertices(), getAllEdges());
        return graph;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return getAllVertices().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public int verticesCount() {
        return getAllVertices().size();
    }

    /**
     * {@inheritDoc}
     */
    public int edgesCount() {
        return getAllEdges().size();
    }

    /**
     * {@inheritDoc}
     */
    public abstract boolean vertexExists(V vertex);

    /**
     * {@inheritDoc}
     */
    public boolean edgeExists(V from, V to) {
        return getEdge(from, to).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    public OptionalInt getEdgeWeight(V from, V to) {
        return getEdge(from, to)
            .map(Edge::weight)
            .map(OptionalInt::of)
            .orElseGet(OptionalInt::empty);
    }

    /**
     * {@inheritDoc}
     */
    public abstract Optional<Edge<V>> getEdge(V from, V to);

    /**
     * {@inheritDoc}
     */
    public abstract boolean addVertex(V vertex);

    /**
     * {@inheritDoc}
     */
    public abstract boolean removeVertex(V vertex);

    /**
     * {@inheritDoc}
     */
    public boolean addEdge(V from, V to, int weight) {
        return addEdge(new Edge<V>(from, to, weight));
    }

    /**
     * {@inheritDoc}
     */
    public abstract boolean addEdge(Edge<V> edge);

    /**
     * {@inheritDoc}
     */
    public abstract OptionalInt removeEdge(V from, V to);

    /**
     * {@inheritDoc}
     */
    public OptionalInt reweightEdge(V from, V to, int newWeight) {
        return removeEdge(from, to)
            .stream()
            .peek((unused) -> addEdge(from, to, newWeight))
            .findAny();
    }

    /**
     * {@inheritDoc}
     */
    public OptionalInt reweightEdge(Edge<V> edge) {
        return reweightEdge(edge.from(), edge.to(), edge.weight());
    }

    /**
     * {@inheritDoc}
     */
    public abstract Set<Edge<V>> getAdjacentEdges(V vertex);

    /**
     * {@inheritDoc}
     */
    public abstract Set<V> getAllVertices();

    /**
     * {@inheritDoc}
     */
    public Set<Edge<V>> getAllEdges() {
        return getAllVertices().stream()
            .flatMap((v) -> getAdjacentEdges(v).stream())
            .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    public abstract void clear();

    /**
     * Finds shortest paths from specified vertex to all the rest.
     * 
     * <p>Implements Dijkstra shortest paths algorithm.
     * As it uses priority queue to always keep current shortest paths naturally sorted, 
     * this implementation is asymtotically effective.
     * 
     * Edges with negative weights are not allowed due to peculiarities of an algorithm!
     * 
     * @param  s  a vertex to find shortest paths from 
     * @return a map containing shortest paths from {@code s} vertex to all.
     * @throws IllegalStateException if one of edges has negative weight
     * @throws NullPointerException  if one of vertices is {@code null} 
     *                               (due to Java {@link PriorityQueue} contract)
     * 
     * @see Map
     * @see PriorityQueue
     */
    public Map<V, Integer> findShortestPaths(V s) {
        Map<V, Integer> weights = new HashMap<>();
        getAllVertices().stream()
            .forEach((vertex) -> weights.put(vertex, Integer.MAX_VALUE));

        weights.replace(s, 0);

        PriorityQueue<V> heap = 
            new PriorityQueue<>((a, b) -> Integer.compare(weights.get(a), weights.get(b)));
        heap.addAll(weights.keySet());

        while (!heap.isEmpty()) {
            for (Edge<V> e : getAdjacentEdges(heap.poll())) {
                if (e.weight() < 0) {
                    throw new IllegalStateException("negative weight edges are not supported");
                }
                int newWeight = weights.get(e.from()) + e.weight();
                if (newWeight < weights.get(e.to())) {
                    weights.replace(e.to(), newWeight);
                }
            }
        }

        return weights;
    }

    /**
     * Returns the hash code value for this AbstractGraph.
     * Hash code is the same for equal graphs, even differently
     * represented.
     * 
     * @return the hash code for this graph
     */
    @Override
    public int hashCode() {
        return getAllVertices().hashCode() +
               getAllEdges().hashCode();
    }

    /**
     * Compares the specified Object with this graph for equality. 
     * Returns {@code true} if and only if the specified object is also
     * a graph, both graphs has similar sets of vertices and edges related
     * to them, herewith graphs may be representated differently.
     * 
     * @param  obj  the object to be compared for equality with this graph
     * @return {@code true} if specified object is equal to this graph,
     *         {@code false} otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractGraph)) {
            return false;
        }

        AbstractGraph<V> other = (AbstractGraph<V>) obj;

        var thisVertices = this.getAllVertices();
        var otherVertices = other.getAllVertices();

        if (!thisVertices.equals(otherVertices)) {
            return false;
        } 

        for (var vertex : thisVertices) {
            if (!this.getAdjacentEdges(vertex).equals(other.getAdjacentEdges(vertex))) {
                return false;
            }
        }
        
        return true;
    }
}
