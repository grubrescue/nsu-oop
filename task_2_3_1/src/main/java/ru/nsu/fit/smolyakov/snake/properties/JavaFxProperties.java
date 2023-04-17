package ru.nsu.fit.smolyakov.snake.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JavaFxProperties(
    @JsonProperty("title") String title,
    @JsonProperty("resX") int resX,
    @JsonProperty("resY") int resY
) {
}
