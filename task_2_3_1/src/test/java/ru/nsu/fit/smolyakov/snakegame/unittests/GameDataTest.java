package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.ExampleProperties;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl.GreedyAISnake;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameDataTest {
    @Test
    void getAvailableAiNamesTest() {
        assertThat(GameData.INSTANCE.getAvailableAiNames())
            .isEqualTo(
                List.of(
                    "GreedyAISnake",
                    "KamikazeAISnake",
                    "SamuraiAISnake",
                    "StayinAliveAISnake"
                )
            );
    }

    @Test
    void aiSnakeByShortNameTest() {
        assertThat(GameData.INSTANCE.aiSnakeByShortName(
            "GreedyAISnake", new GameModelImpl(ExampleProperties.properties)).get()
        ).isInstanceOf(GreedyAISnake.class);

        assertThat(GameData.INSTANCE.aiSnakeByShortName(
            "БотКоторыйГрабитКараваны", new GameModelImpl(ExampleProperties.properties))
        ).isEmpty();
    }

    @Test
    void levelFileNamesTest() {
        assertThat(GameData.INSTANCE.levelFileNames())
            .isEqualTo(
                List.of(
                    "40x20_border.txt",
                    "40x20_empty.txt"
                )
            );
    }

    @Test
    void levelFileScannerTest() {
        assertThat(GameData.INSTANCE.levelFileScanner("40x20_border.txt"))
            .isNotEmpty();
        assertThat(GameData.INSTANCE.levelFileScanner("ыыыыыыыыыыыыыы"))
            .isEmpty();
    }
}
