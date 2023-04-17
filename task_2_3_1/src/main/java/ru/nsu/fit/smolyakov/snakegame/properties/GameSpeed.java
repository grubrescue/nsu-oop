package ru.nsu.fit.smolyakov.snakegame.properties;

public enum GameSpeed {
    LEVEL_0(400),
    LEVEL_1(200),
    LEVEL_2(100),
    LEVEL_3(50);

    private final int frameDelayMillis;

    GameSpeed(int frameDelayMillis) {
        this.frameDelayMillis = frameDelayMillis;
    }

    public int getFrameDelayMillis() {
        return frameDelayMillis;
    }
}
