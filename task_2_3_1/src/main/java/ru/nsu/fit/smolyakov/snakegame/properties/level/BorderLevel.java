package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A level with a border around the field.
 */
public final class BorderLevel extends Level {
    /**
     * Initializes a new instance of the {@link BorderLevel} class.
     */
    @JsonCreator
    public BorderLevel() {
        super();
    }
}
