package ru.nsu.fit.smolyakov.graph;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph;
import ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph;

class ConversionTests {
    @Test 
    void incidentListConversions() {
        var incidentListGraph = 
            new IncidentListGraph<String>(TestLists.verticesSet(), TestLists.someGraphEdgesSet);
        var adjacencyMatrixGraph = 
            new AdjacencyMatrixGraph<String>(TestLists.verticesList(), TestLists.someGraphMatrix);

        assertThat(incidentListGraph.toIncidentList()).isEqualTo(incidentListGraph);

        assertThat(incidentListGraph.switchGraphRepresentation(new AdjacencyMatrixGraph<String>()))
            .isEqualTo(incidentListGraph);

        assertThat(incidentListGraph.switchGraphRepresentation(new AdjacencyMatrixGraph<String>()))
            .isEqualTo(adjacencyMatrixGraph);

        assertThat(incidentListGraph.switchGraphRepresentation(adjacencyMatrixGraph))
            .isEqualTo(incidentListGraph);
    }

    @Test 
    void adjacencyMatrixConversions() {
        var incidentListGraph = 
            new IncidentListGraph<String>(TestLists.verticesSet(), TestLists.someGraphEdgesSet);
        var adjacencyMatrixGraph = 
            new AdjacencyMatrixGraph<String>(TestLists.verticesList(), TestLists.someGraphMatrix);

        assertThat(adjacencyMatrixGraph.toIncidentList()).isEqualTo(incidentListGraph);
        assertThat(adjacencyMatrixGraph.toIncidentList()).isEqualTo(adjacencyMatrixGraph);

        assertThat(adjacencyMatrixGraph.switchGraphRepresentation(new IncidentListGraph<String>()))
            .isEqualTo(incidentListGraph);

        assertThat(adjacencyMatrixGraph.switchGraphRepresentation(new IncidentListGraph<String>()))
            .isEqualTo(adjacencyMatrixGraph);

        assertThat(adjacencyMatrixGraph.switchGraphRepresentation(incidentListGraph))
            .isEqualTo(adjacencyMatrixGraph);
    }
}
