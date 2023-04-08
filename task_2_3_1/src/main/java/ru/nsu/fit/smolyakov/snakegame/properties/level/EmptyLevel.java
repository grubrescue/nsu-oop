package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

/**
 * An empty level.
 *
 * <p>This class is used as a DTO for the {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier} class,
 * so the game level is actually created by
 * {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier#fromProperties(GameProperties)} method.
 */
public final class EmptyLevel extends Level {
    /**
     * Initializes a new instance of the {@link EmptyLevel} class.
     */
    @JsonCreator
    public EmptyLevel() {
        super();
    }
}
