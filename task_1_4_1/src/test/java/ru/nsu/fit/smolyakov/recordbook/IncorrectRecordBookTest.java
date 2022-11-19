package ru.nsu.fit.smolyakov.recordbook;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IncorrectRecordBookTest {
    static Stream<File> filesList() {
        return Stream.of(
            new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/IncorrectBook1.json"),
            new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/IncorrectBook2.json"),
            new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/IncorrectBook3.json")
        );
    }

    @ParameterizedTest
    @MethodSource("filesList")
    void valueInstantiationTest(File file) throws Exception {
        assertThatThrownBy(() -> RecordBook.fromJson(file))
            .isInstanceOf(ValueInstantiationException.class);
    }

    @Test
    void micmatchedInputTest() {
        var file = new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/IncorrectBook4.json");
        assertThatThrownBy(() -> RecordBook.fromJson(file))
            .isInstanceOf(MismatchedInputException.class);
    }
}
