package ru.nsu.fit.smolyakov.snakegame.properties;

/**
 * Represents game speed.
 */
public enum GameSpeed {
    /**
     * The slowest game speed. The snake moves every 800 milliseconds.
     */
    LEVEL_0(800),

    /**
     * The medium game speed. The snake moves every 400 milliseconds.
     */
    LEVEL_1(400),

    /**
     * The fast game speed. The snake moves every 200 milliseconds.
     */
    LEVEL_2(200),

    /**
     * Faster game speed. The snake moves every 100 milliseconds.
     */
    LEVEL_3(100),

    /**
     * The fastest game speed. The snake moves every 50 milliseconds.
     */
    LEVEL_4(50),

    /**
     * Extreme game speed. The snake moves every 25 milliseconds.
     */
    LEVEL_5(25),

    /**
     * Kamikaze game speed. The snake moves every 10 milliseconds.
     */
    LEVEL_6(10);

    private final int frameDelayMillis;

    /**
     * Creates a new game speed.
     *
     * @param frameDelayMillis a delay between frames in milliseconds
     */
    GameSpeed(int frameDelayMillis) {
        this.frameDelayMillis = frameDelayMillis;
    }

    /**
     * Returns a delay between frames in milliseconds.
     *
     * @return a delay between frames in milliseconds
     */
    public int getFrameDelayMillis() {
        return frameDelayMillis;
    }
}
