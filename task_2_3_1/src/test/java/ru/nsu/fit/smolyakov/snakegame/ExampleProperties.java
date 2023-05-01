package ru.nsu.fit.smolyakov.snakegame;

import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.util.List;

public class ExampleProperties {
    public static final GameProperties properties = new GameProperties(
        6,
        6,
        1,
        1,
        "40x20_border.txt",
        List.of(),
        GameSpeed.SPEED_0
    );
}
