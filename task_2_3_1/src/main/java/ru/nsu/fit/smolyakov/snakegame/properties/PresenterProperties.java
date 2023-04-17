package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PresenterProperties(
    @JsonProperty("speed") GameSpeed speed,
    @JsonProperty("startTimeoutMillis") int startTimeoutMillis
) {
}
