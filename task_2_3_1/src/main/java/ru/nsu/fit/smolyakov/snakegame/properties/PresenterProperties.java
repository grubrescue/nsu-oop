package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents properties of presenter.
 *
 * @param speed a game speed
 * @param startTimeoutMillis a timeout before the game starts in milliseconds. One will be multiplied by 3.
 */
public record PresenterProperties(
    @JsonProperty("speed") GameSpeed speed,
    @JsonProperty("startTimeoutMillis") int startTimeoutMillis
) {
}
