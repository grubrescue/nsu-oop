package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GameFieldProperties(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("maxApples") int maxApples,
    @JsonProperty("barrierUrl") String barrierUrl
) {
}
