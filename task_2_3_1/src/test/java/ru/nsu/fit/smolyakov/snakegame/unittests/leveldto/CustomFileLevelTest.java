package ru.nsu.fit.smolyakov.snakegame.unittests.leveldto;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.properties.level.CustomFileLevel;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomFileLevelTest {
    CustomFileLevel level = new CustomFileLevel("10x15_level.txt");

    @Test
    void fileNameTest() {
        assertThat(level.getFileName()).isEqualTo("10x15_level.txt");
    }

    @Test
    void nullConstructorTest() {
        assertThatThrownBy(() -> new CustomFileLevel(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void parseFieldSizeTest() {
        assertThat(level.parseFieldSize())
            .isEqualTo(Optional.of(new CustomFileLevel.FieldSize(10, 15)));
    }

    @Test
    void parseStaticFilenameFieldSizeTest() {
        assertThat(CustomFileLevel.parseFilenameFieldSize("10x_level.txt"))
            .isEmpty();
    }

    @Test
    void parseFilenameNullTest() {
        assertThatThrownBy(() -> CustomFileLevel.parseFilenameFieldSize(null))
            .isInstanceOf(NullPointerException.class);
    }
}
