package ru.nsu.fit.smolyakov.graph.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileReader;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.graph.TestLists;
import ru.nsu.fit.smolyakov.graph.incident_list.IncidentListGraph;

public class EdgesSetParserTest {
    @Test
    void edgesListParserTest() throws DataFormatException, IOException {
        FileReader file = new FileReader("src/test/resources/ru/nsu/fit/smolyakov/graph/parser/EdgesListCorrect.txt");

        var graphParser = new EdgesSetParser(file);

        var verticesSet = graphParser.getGraphSets().getKey();
        var edgesSet = graphParser.getGraphSets().getValue();

        assertThat(verticesSet).isEqualTo(TestLists.verticesSet());
        assertThat(edgesSet).isEqualTo(TestLists.someGraphEdgesSet);

        assertThat(graphParser.toGraph())
            .isEqualTo(new IncidentListGraph<>(TestLists.verticesSet(), TestLists.someGraphEdgesSet));
    }

    @Test
    void edgesListParserTestIncorrect() throws DataFormatException, IOException {
        FileReader file = new FileReader("src/test/resources/ru/nsu/fit/smolyakov/graph/parser/EdgesListIncorrect.txt");

        assertThatThrownBy(() -> new EdgesSetParser(file))
            .isInstanceOf(DataFormatException.class);
    }
}
