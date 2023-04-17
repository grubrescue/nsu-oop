package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JavaFxProperties(
    @JsonProperty("title") String title,
    @JsonProperty("resX") int resX,
    @JsonProperty("resY") int resY
) {
}
