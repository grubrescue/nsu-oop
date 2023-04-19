package ru.nsu.fit.smolyakov.snakegame.properties;

/**
 * Represents game speed.
 */
public enum GameSpeed {
    /**
     * The slowest game speed. The snake moves every 800 milliseconds.
     */
    LEVEL_0(800, "Level 0: slowest"),

    /**
     * The medium game speed. The snake moves every 400 milliseconds.
     */
    LEVEL_1(400, "Level 1: medium"),

    /**
     * The fast game speed. The snake moves every 200 milliseconds.
     */
    LEVEL_2(200, "Level 2: fast"),

    /**
     * Faster game speed. The snake moves every 100 milliseconds.
     */
    LEVEL_3(100, "Level 3: faster"),

    /**
     * The fastest game speed. The snake moves every 50 milliseconds.
     */
    LEVEL_4(50, "Level 4: fastest"),

    /**
     * Extreme game speed. The snake moves every 25 milliseconds.
     */
    LEVEL_5(25, "Level 5: extreme"),

    /**
     * Kamikaze game speed. The snake moves every 10 milliseconds.
     */
    LEVEL_6(10, "Level 6: kamikaze");

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
