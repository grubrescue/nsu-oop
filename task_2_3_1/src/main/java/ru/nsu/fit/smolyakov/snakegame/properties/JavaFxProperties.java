package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents properties of JavaFX window.
 *
 * @param title a title of the window
 * @param resX a width of the window
 * @param resY a height of the window
 */
public record JavaFxProperties(
    @JsonProperty("title") String title,
    @JsonProperty("resX") int resX,
    @JsonProperty("resY") int resY
) {
}
