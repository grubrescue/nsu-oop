package ru.nsu.fit.smolyakov.substringfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import ru.nsu.fit.smolyakov.substringfinder.implementation.KnuthMorrisPrattSubstringFinder;
import ru.nsu.fit.smolyakov.substringfinder.implementation.NaiveSubstringFinder;
import ru.nsu.fit.smolyakov.substringfinder.implementation.TwoWaySubstringFinder;

// also abstract methods tested
class SubstringFinderTests {
    static Stream<String> wordsToSearch() {
        return Stream.of(
            "Наташа",
            "дуб",
            "абоба",
            "поиск",
            "боб"
        );
    }

    File file;

    @BeforeEach 
    void init() {
        file = new File("src/test/resources/ru/nsu/fit/smolyakov/substring_finder/VoynaMir.txt");
    }
    
    @ParameterizedTest
    @MethodSource("wordsToSearch")
    void findTest(String needle) throws IOException {
        var naiveFinder = new NaiveSubstringFinder(new FileInputStream(file), needle);
        var kmpFinder = new KnuthMorrisPrattSubstringFinder(new FileInputStream(file), needle);
        var twoWayFinder = new TwoWaySubstringFinder(new FileInputStream(file), needle);

        var res = naiveFinder.find();

        assertThat(kmpFinder.find()).isEqualTo(res);
        assertThat(twoWayFinder.find()).isEqualTo(res);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void wrongConstructorTest(String needle) throws IOException {
        assertThatThrownBy(() -> new NaiveSubstringFinder(new FileInputStream(file), needle))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new KnuthMorrisPrattSubstringFinder(new FileInputStream(file), needle))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TwoWaySubstringFinder(new FileInputStream(file), needle))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
