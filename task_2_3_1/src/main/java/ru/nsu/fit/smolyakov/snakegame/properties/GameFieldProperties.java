package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents properties of game field.
 *
 * @param width a width of the game field
 * @param height a height of the game field
 * @param maxApples a maximum number of apples are allowed to be on the game field
 * @param barrierUrl a path to the image of the barrier
 */
public record GameFieldProperties(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("maxApples") int maxApples,
    @JsonProperty("barrierUrl") String barrierUrl
) {
}
