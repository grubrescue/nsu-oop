package ru.nsu.fit.smolyakov.graph;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/** 
 * A graph, an abstraction describing the relationship between lines (called edges) and points
 * (called vertices). The user of this interface is able to change the graph, however he wants,
 * e.g. add/remove vertices/edges, reweight edges, or get vertices' relationship to other 
 * components of this structure.
 * 
 * <p>Multigraphs are not supported! If a user attempts to use {@link addEdge} with the same vertices
 * more than one time, this method has to return {@code false} and insert one that occurs first.
 * 
 * <p>This interface specification admits inherited classes to be immutable.
 * It means that {@link clear}, {@link addVertex}, {@link addEdge}, {@link reweightEdge}, 
 * {@link removeVertex}, {@link removeEdge} and all methods reusing them
 * may throw {@link UnsupportedOperationException}.
 * 
 * @see AbstractGraph
 * @see ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph
 * @see ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph
 */
public interface Graph<V> {
    /**
     * Returns if this graph contains at least one vertex.
     * 
     * @return {@code true} if this graph is empty,
     *         {@code false} otherwise.
     */
    public boolean isEmpty();

    /**
     * Returns an amount of vertices in this graph.
     * 
     * @return an amount of vertices
     */
    public int verticesCount();

    /**
     * Returns an amount of edges in this graph.
     * 
     * @return an amount of edges
     */
    public int edgesCount();

    /**
     * Returns if a specified vertex exists.
     * 
     * @param  vertex  a specified vertex
     * 
     * @return {@code true} if a specified vertex exists, 
     *         {@code false} otherwise
     */
    public boolean vertexExists(V vertex);

    /**
     * Checks if an edge related to specified vertices exists.
     * 
     * @param  from  a vertex a specified edge comes from
     * @param  to  a vertex a specified edge comes to
     * 
     * @return {@code true} if an edge related to specified vertices exists, 
     *         {@code false} otherwise
     */
    public boolean edgeExists(V from, V to);

    /**
     * Returns a weight of an edge related to specified vertices. 
     * If this edge doesn't exist, this method returns 
     * {@link OptionalInt#empty()}.
     * 
     * @param  from  a vertex a specified edge comes from
     * @param  to  a vertex a specified edge comes to
     * 
     * @return {@link OptionalInt} of old weight if an edge related to specified 
     *         vertices exists, {@link OptionalInt#empty()} otherwise
     * @see OptionalInt
     */
    public OptionalInt getEdgeWeight(V from, V to);

    /**
     * Returns an {@link Edge} record related to specified vertices. 
     * If such edge doesn't exist, this method returns 
     * {@link Optional#empty()}.
     * 
     * @param  from  a vertex a specified edge comes from
     * @param  to  a vertex a specified edge comes to
     * 
     * @return {@link Optional} of an edge related to specified 
     *         vertices it it exists, 
     *         {@link Optional#empty()} otherwise
     * 
     * @see Optional
     * @see Edge
     */
    public Optional<Edge<V>> getEdge(V from, V to);

    /**
     * Inserts a single vertex to this graph (optional operation).
     * 
     * @param  vertex  a vertex to add
     * @return {@code true} if vertex successfully added, 
     *         {@code false} if it already existed.
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     */
    public boolean addVertex(V vertex);

    /**
     * Removes a single vertex from this graph, if one exists (optional operation).
     * 
     * @param  vertex  a vertex to remove
     * @return {@code true} if vertex successfully removed,
     *         {@code false} if it's not presented in this graph.
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     */
    public boolean removeVertex(V vertex);

    /**
     * Adds a single edge to this graph (optional operation).
     * 
     * <p>If this edge already exists, 
     * this method does nothing and returns {@code false}. 
     * 
     * <p>If a vertex specified by {@code from} or {@code to} doesn't exist,
     * this method also returns {@code false}. 
     * 
     * @param  from  a vertex a new edge comes from
     * @param  to  a vertex a new edge comes to
     * @param  weight  a weight of new edge
     * 
     * @return {@code true} if an edge is successfully added, 
     *         {@code false} otherwise
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     */
    public boolean addEdge(V from, V to, int weight);

    /**
     * Adds a single edge to this graph (optional operation).
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
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     */
    public boolean addEdge(Edge<V> edge);

    /**
     * Removes a single edge from this graph (optional operation).
     * If edge doesn't exist, this method does nothing and 
     * returns {@link OptionalInt#empty()}.
     * 
     * @param  from  a vertex a new edge comes from
     * @param  to  a vertex a new edge comes to
     * 
     * @return {@link OptionalInt} of old weight if an edge related to specified 
     *         vertices exists, 
     *         {@link OptionalInt#empty()} otherwise
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     * 
     * @see OptionalInt
     */
    public OptionalInt removeEdge(V from, V to);

    /**
     * Changes edge's weight to a specified one (optional operation).
     * If edge doesn't exist, this method does nothing and 
     * returns {@link OptionalInt#empty()}.
     * 
     * @param  from  a vertex a specified edge comes from
     * @param  to  a vertex a specified edge comes to
     * @param  newWeight  a new weight of an edge
     * 
     * @return {@link OptionalInt} of old weight if an edge related to specified 
     *         vertices exists, 
     *         {@link OptionalInt#empty()} otherwise
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     * 
     * @see OptionalInt
     */
    public OptionalInt reweightEdge(V from, V to, int newWeight);

    /**
     * Changes edge's weight to a specified one (optional operation).
     * If edge doesn't exist, this method does nothing and 
     * returns {@link OptionalInt#empty()}.
     * 
     * @param  edge  an {@link Edge} record containing new weight
     * 
     * @return {@link OptionalInt} of old weight, if an edge related to specified 
     *         vertices exists, 
     *         {@link OptionalInt#empty()} otherwise
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     * @see Edge
     */
    public OptionalInt reweightEdge(Edge<V> edge);

    /**
     * Returns a set of all edges adjacent to specified vertice.
     * 
     * @param  vertex  a vertex which adjacent edges are returned
     * 
     * @return the list of edges adjacent to the specified vertex
     * @see    Edge
     * @see    Set
     */
    public Set<Edge<V>> getAdjacentEdges(V vertex);

    /**
     * Returns a set of all vertices of this graph.
     * 
     * @return a set of all vertices of this graph
     * @see    Set
     */
    public Set<V> getAllVertices();

    /**
     * Returns a set of all edges of this graph.
     * 
     * @return a set of all edges of this graph
     * @see    Set
     */
    public Set<Edge<V>> getAllEdges();

    /**
     * Turns a graph into an empty state (optional operation).
     * 
     * @throws UnsupportedOperationException if this operation 
     *                                       is not supported by this graph
     */
    public void clear();

    /**
     * Finds shortest paths from specified vertex to all the rest.
     * 
     * @param  s  a vertex to find shortest paths from 
     * @return a map containing shortest paths from {@code s} vertex to all.
     * 
     * @see Map
     */
    public Map<V, Integer> findShortestPaths(V s);
}
