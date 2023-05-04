package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RandomLevel extends Level {
    private final double density;

    @JsonCreator
    public RandomLevel(@JsonProperty("density") double density) {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be in range [0, 1]");
        }

        this.density = density;
    }

    public double getDensity() {
        return density;
    }
}
