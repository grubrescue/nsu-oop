package ru.nsu.fit.smolyakov.snakegame;

import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;
import ru.nsu.fit.smolyakov.snakegame.properties.level.BorderLevel;

import java.util.List;

public class ExampleProperties {
    public static final GameProperties properties = new GameProperties(
        6,
        6,
        1,
        1,
        new BorderLevel(),
        List.of(),
        GameSpeed.SPEED_0
    );
}
