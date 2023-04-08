package ru.nsu.fit.smolyakov.snake.model;

import java.util.Set;

public record Barrier(Set<Point> barrierPoints){
    public boolean metBarrier(Snake snake) {
        return barrierPoints.contains(snake.head());
    }
}
