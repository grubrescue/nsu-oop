package ru.nsu.fit.smolyakov.graph.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileReader;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.graph.TestLists;
import ru.nsu.fit.smolyakov.graph.adjacency_matrix.AdjacencyMatrixGraph;

public class AdjacencyMatrixParserTest {
    @Test
    void adjacencyMatrixParserTest() throws DataFormatException, IOException {
        FileReader file = new FileReader("src/test/resources/ru/nsu/fit/smolyakov/graph/parser/AdjacencyMatrixCorrect.txt");

        var graphParser = new AdjacencyMatrixParser(file);

        var verticesList = graphParser.getGraphSets().getKey();
        var matrix = graphParser.getGraphSets().getValue();

        assertThat(verticesList).isEqualTo(TestLists.verticesList());
        assertThat(matrix).isEqualTo(TestLists.someGraphMatrix);

        assertThat(graphParser.toGraph())
            .isEqualTo(new AdjacencyMatrixGraph<>(TestLists.verticesList(), TestLists.someGraphMatrix));
    }

    @Test
    void adjacencyMatrixParserTestIncorrect() throws DataFormatException, IOException {
        FileReader file = new FileReader("src/test/resources/ru/nsu/fit/smolyakov/graph/parser/AdjacencyMatrixIncorrect.txt");

        assertThatThrownBy(() -> new AdjacencyMatrixParser(file))
            .isInstanceOf(DataFormatException.class);
    }
    
}
