package ru.nsu.fit.smolyakov.graph;

/**
 * Stores a vertex this edge comes from, a vertex this edge comes to and 
 * a weight of this edge (or a distance between vertices).
 * 
 * @see Graph
 */
public record Edge<V>(V from, V to, int weight) {}
