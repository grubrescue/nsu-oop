package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class EmptyLevel extends Level {
    @JsonCreator
    public EmptyLevel() {
        super();
    }
}
