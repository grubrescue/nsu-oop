package ru.nsu.fit.smolyakov.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph;

// also abstract methods tested
class IncidentListGraphTest {
    AbstractGraph<String> graph;

    @BeforeEach
    void init() {
        graph = new IncidentListGraph<String>(TestLists.verticesSet(), TestLists.someGraphEdgesSet);
    }

    // constructors test
    @Test
    void noArgsAndEdgesSetConstructorTest() {
        var emptyGraph = new IncidentListGraph<String>();
        TestLists.verticesSet()
            .stream()
            .forEach(emptyGraph::addVertex);

        assertThat(emptyGraph).isNotEqualTo(graph);

        TestLists.someGraphEdgesSet
            .stream()
            .forEach(emptyGraph::addEdge);

        assertThat(emptyGraph).isEqualTo(graph);
    }

    @Test 
    void matrixConstructorTest() {
        var matrixGraph = new IncidentListGraph<String>(TestLists.verticesList(), TestLists.someGraphMatrix);
        assertThat(matrixGraph).isEqualTo(graph);

        assertThatThrownBy(() -> new IncidentListGraph<String>(List.of("not", "correct"), TestLists.someGraphMatrix))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new IncidentListGraph<String>(List.of("not", "correct"), new Integer[2][3]))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new IncidentListGraph<String>(List.of("not", "correct"), new Integer[3][2]))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test 
    void edgesSetWithoutVerticesSetConstructorTest() {
        var noVerticesSetGraph = new IncidentListGraph<String>(TestLists.someGraphEdgesSet);
        assertThat(noVerticesSetGraph)
            .isNotEqualTo(graph);
        assertThat(noVerticesSetGraph.getAllVertices())
            .isEqualTo(TestLists.verticesSetNoTerminal);
        assertThat(noVerticesSetGraph.getAllEdges())
            .isEqualTo(TestLists.someGraphEdgesSet);
    }

    // equals & hashcode test

    @Test 
    void incorrectEqualsTests() {
        assertThat(graph).isEqualTo(graph);
        assertThat(graph).isNotEqualTo(null);
        assertThat(graph).isNotEqualTo(new Object());
        assertThat(graph).isNotEqualTo(new IncidentListGraph<Integer>());
        assertThat(graph).isNotEqualTo(new IncidentListGraph<String>());
    }

    @Test 
    void equalsTest() {
        var graph2 = new IncidentListGraph<String>(TestLists.verticesSet(), TestLists.someGraphEdgesSet);

        assertThat(graph.hashCode()).isEqualTo(graph2.hashCode());
        assertThat(graph).isEqualTo(graph2);

        graph2.addEdge("a", null, 666);
        assertThat(graph2).isNotEqualTo(graph);
    }

    // overriden methods test

    @Test
    void addEdgesWithoutVerticesTest() {
        var emptyGraph = new IncidentListGraph<String>();
        assertThat(emptyGraph.addEdge(new Edge<String>("abo", "ba",1))).isFalse();
        assertThat(emptyGraph).isEqualTo(new IncidentListGraph<String>());
    }

    @Test
    void addVerticesTest() {
        var emptyGraph = new IncidentListGraph<String>();
        assertThat(emptyGraph.addVertex("a")).isTrue();
        assertThat(emptyGraph.addVertex("b")).isTrue();
        assertThat(emptyGraph.addVertex("b")).isFalse();

        assertThat(emptyGraph.addEdge(new Edge<String>("a", "b", 1))).isTrue();
    }

    @Test 
    void getEdgeTest() {
        assertThat(graph.getEdge("a", "b")).isEqualTo(Optional.of(new Edge<String>("a", "b", 1)));
        assertThat(graph.getEdge("a", "boba")).isEqualTo(Optional.empty());
    }

    @Test
    void removeEdgeTest() {
        assertThat(graph.removeEdge("c", "a")).isEqualTo(OptionalInt.of(2));
        assertThat(graph.edgeExists("c", "a")).isFalse();
        
        assertThat(graph.removeEdge("a", "boba")).isEqualTo(OptionalInt.empty());
        assertThat(graph.edgeExists("a", "boba")).isFalse();
    }

    @Test 
    void getAllVerticesTest() {
        assertThat(graph.getAllVertices()).isEqualTo(TestLists.verticesSet());
    }

    @Test 
    void getAllEdgesTest() {
        assertThat(graph.getAllEdges()).isEqualTo(TestLists.someGraphEdgesSet);
    }

    @Test
    void clearTest() {
        graph.clear();
        assertThat(graph).isEqualTo(new IncidentListGraph<String>());
    }

    @Test
    void removeVertexTest() {
        graph.removeVertex("c");
        assertThat(graph.getAllEdges()).isEqualTo(TestLists.someGraphEdgesSetWithoutC);
        assertThat(graph.getAllVertices()).isEqualTo(TestLists.verticesSetNoC());
    }

    @Test
    void removeNonexistantVertexTest() {
        assertThat(graph.removeVertex("aboba")).isFalse();
    }

    @Test
    void getAdjacentEdgesTest() {
        assertThat(graph.getAdjacentEdges("c")).isEqualTo(TestLists.edgesAdjacentToC);
        assertThat(graph.getAdjacentEdges("aboba")).isNull();
    }

    // inherited methods test

    @Test 
    void edgeExistsTest() {
        assertThat(graph.edgeExists("a", "b")).isTrue();
        assertThat(graph.edgeExists("a", "boba")).isFalse();
    }

    @Test 
    void getEdgeWeightTest() {
        assertThat(graph.getEdgeWeight("a", "b")).isEqualTo(OptionalInt.of(1));
        assertThat(graph.getEdgeWeight("a", "boba")).isEqualTo(OptionalInt.empty());
    }

    @Test 
    void getEdgeWeight() {
        assertThat(graph.getEdgeWeight("a", "b")).isEqualTo(OptionalInt.of(1));
        assertThat(graph.getEdgeWeight("a", "boba")).isEqualTo(OptionalInt.empty());
    }

    @Test 
    void isEmptyTest() {
        assertThat(graph.isEmpty()).isFalse();
        assertThat(new IncidentListGraph<String>().isEmpty()).isTrue();
    }

    @Test 
    void countVerticesTest() {
        assertThat(graph.verticesCount()).isEqualTo(TestLists.verticesSet().size());
    }

    @Test 
    void countEdgesTest() {
        assertThat(graph.edgesCount()).isEqualTo(TestLists.someGraphEdgesSet.size());
    }

    @Test
    void reweightEdgeTest() {
        assertThat(graph.reweightEdge("a", "b", 666)).isEqualTo(OptionalInt.of(1));
        
        assertThat(graph.reweightEdge("a", "boba", 666)).isEqualTo(OptionalInt.empty());
        assertThat(graph.getEdgeWeight("a", "b")).isEqualTo(OptionalInt.of(666));
        assertThat(graph.getEdgeWeight("a", "boba")).isEqualTo(OptionalInt.empty());
    }

    @Test
    void reweightEdgeEdgeTest() {
        
        assertThat(graph.reweightEdge(new Edge<>("a", "b", 666))).isEqualTo(OptionalInt.of(1));
        
        assertThat(graph.reweightEdge(new Edge<>("a", "boba", 666))).isEqualTo(OptionalInt.empty());
        assertThat(graph.getEdgeWeight("a", "b")).isEqualTo(OptionalInt.of(666));
        assertThat(graph.getEdgeWeight("a", "boba")).isEqualTo(OptionalInt.empty());
    }

    @Test
    void shortestPathsTest() {
        graph.removeVertex(null);
        graph.addVertex("imagine i am null");
        var shortestPaths = graph.findShortestPaths("c");
        assertThat(shortestPaths).isEqualTo(TestLists.shortestPathsFromC);
    }

    @Test
    void incorrectShortestPathsTest() {
        graph.removeVertex(null);
        graph.addVertex("imagine i am null");

        graph.reweightEdge("a", "b", -1);
        assertThatThrownBy(() -> graph.findShortestPaths("c"))
            .isInstanceOf(IllegalStateException.class);
    }
}
