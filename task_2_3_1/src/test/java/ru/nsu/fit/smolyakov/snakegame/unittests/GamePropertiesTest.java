package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.ExampleProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GamePropertiesTest {
    @Test
    void withApplesTest() {
        assertThat(
            ExampleProperties.properties
                .withApples(2)
                .apples()
        ).isEqualTo(2);

        assertThatThrownBy(() -> ExampleProperties.properties.withApples(-1))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
            ExampleProperties.properties.withApples(
                ExampleProperties.properties.height() *
                ExampleProperties.properties.width()
            )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withWidthTest() {
        assertThat(
            ExampleProperties.properties
                .withWidth(666)
                .width()
        ).isEqualTo(666);

        assertThatThrownBy(() -> ExampleProperties.properties.withWidth(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withHeightTest() {
        assertThat(
            ExampleProperties.properties
                .withHeight(666)
                .height()
        ).isEqualTo(666);

        assertThatThrownBy(() -> ExampleProperties.properties.withHeight(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withJavaFxScalingTest() {
        assertThat(
            ExampleProperties.properties
                .withJavaFxScaling(666)
                .javaFxScaling()
        ).isEqualTo(666);

        assertThatThrownBy(() -> ExampleProperties.properties.withJavaFxScaling(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withSpeedTest() {
        assertThat(
            ExampleProperties.properties
                .withSpeed(GameSpeed.SPEED_5)
                .speed()
        ).isEqualTo(GameSpeed.SPEED_5);
    }

    @Test
    void withAiClassNamesListTest() {
        var list =
            List.of(
                "прес качат",
                "т) бегит",
                "турник",
                "анжуманя"
            );

        assertThat(
            ExampleProperties.properties
                .withAiClassNamesList(list)
                .aiClassNamesList()
        ).isEqualTo(list);
    }

    @Test
    void withBorderFileNameTest() {
        assertThat(
            ExampleProperties.properties
                .withLevelFileName("луууууч сооооонца золотоооооооооооооооооооооваааааа")
                .levelFileName()
        ).isEqualTo("луууууч сооооонца золотоооооооооооооооооооооваааааа");
    }
}
