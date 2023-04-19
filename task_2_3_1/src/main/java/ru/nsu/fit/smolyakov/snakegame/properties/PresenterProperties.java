package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents properties of presenter.
 *
 * @param speed a game speed
 * @param startTimeoutMillis a timeout before the game starts in milliseconds. One will be multiplied by 4.
 */
public record PresenterProperties(
    @JsonProperty("speed") GameSpeed speed,
    @JsonProperty("startTimeoutMillis") int startTimeoutMillis
) {
    /**
     * Returns this {@code PresenterProperties} with the game speed
     * changed to a specified one. This object is not modified.
     *
     * @param speed a game speed
     * @return a new {@code PresenterProperties} with the
     *         game speed changed to a specified one
     */
    public PresenterProperties withSpeed(GameSpeed speed) {
        return new PresenterProperties(speed, this.startTimeoutMillis);
    }

    /**
     * Returns this {@code PresenterProperties} with the start timeout
     * changed to a specified one. This object is not modified.
     *
     * @param startTimeoutMillis a timeout before the game starts in
     *                           milliseconds (one will be multiplied by 4)
     * @return a new {@code PresenterProperties}
     *         with the start timeout changed to a specified one
     */
    public PresenterProperties withStartTimeoutMillis(int startTimeoutMillis) {
        return new PresenterProperties(this.speed, startTimeoutMillis);
    }
}
