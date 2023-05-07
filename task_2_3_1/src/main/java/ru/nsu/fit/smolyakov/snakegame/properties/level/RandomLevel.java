package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

/**
 * A level with a random barrier. The barrier is created with the specified density.
 *
 * <p>This class is used as a DTO for the {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier} class,
 * so the game level is actually created by
 * {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier#fromProperties(GameProperties)} method.
 */
public final class RandomLevel extends Level {
    private final double density;

    /**
     * Initializes a new instance of the {@link RandomLevel} class.
     * The level will be created with the specified density.
     * That means that the {@link Barrier#barrierPoints()} will contain
     * no more than {@code density * width * height} points.
     *
     * @param density a density of the barrier
     */
    @JsonCreator
    public RandomLevel(@JsonProperty("density") double density) {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be in range [0, 1]");
        }

        this.density = density;
    }

    /**
     * Returns a density of the barrier.
     *
     * @return a density of the barrier
     */
    public double getDensity() {
        return density;
    }
}
