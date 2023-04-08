package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents game speed.
 */
public enum GameSpeed {
    /**
     * The slowest game speed. The snake moves every 800 milliseconds.
     */
    @JsonProperty("slowest") SPEED_0(800, "Speed 0: slowest"),

    /**
     * The medium game speed. The snake moves every 400 milliseconds.
     */
    @JsonProperty("medium") SPEED_1(400, "Speed 1: medium"),

    /**
     * The fast game speed. The snake moves every 200 milliseconds.
     */
    @JsonProperty("fast") SPEED_2(200, "Speed 2: fast"),

    /**
     * Faster game speed. The snake moves every 100 milliseconds.
     */
    @JsonProperty("faster") SPEED_3(100, "Speed 3: faster"),

    /**
     * The fastest game speed. The snake moves every 50 milliseconds.
     */
    @JsonProperty("fastest") SPEED_4(50, "Speed 4: fastest"),

    /**
     * Extreme game speed. The snake moves every 25 milliseconds.
     */
    @JsonProperty("extreme") SPEED_5(25, "Speed 5: extreme"),

    /**
     * Kamikaze game speed. The snake moves every 10 milliseconds.
     */
    @JsonProperty("kamikaze") SPEED_6(10, "Speed 6: kamikaze");

    private final int frameDelayMillis;
    private final String representation;

    /**
     * Creates a new game speed.
     *
     * @param frameDelayMillis a delay between frames in milliseconds
     */
    GameSpeed(int frameDelayMillis, String representation) {
        this.frameDelayMillis = frameDelayMillis;
        this.representation = representation;
    }

    /**
     * Returns a delay between frames in milliseconds.
     *
     * @return a delay between frames in milliseconds
     */
    public int getFrameDelayMillis() {
        return frameDelayMillis;
    }

    /**
     * Returns a string representation of the game speed.
     *
     * @return a string representation of the game speed
     */
    @Override
    public String toString() {
        return representation;
    }
}
