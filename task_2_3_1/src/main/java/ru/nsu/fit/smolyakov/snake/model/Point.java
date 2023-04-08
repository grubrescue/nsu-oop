package ru.nsu.fit.smolyakov.snake.model;

import java.security.SecureRandom;

public record Point(int x, int y) {
    public static Point random(int xLimit, int yLimit) {
        SecureRandom rand = new SecureRandom();

        return new Point(rand.nextInt(xLimit), rand.nextInt(yLimit));
    }
}