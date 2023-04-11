package ru.nsu.fit.smolyakov.snake.model;

import java.util.Set;

public record Barrier(Set<Point> barrierPoints){
    public boolean met(Point point) {
        return barrierPoints.contains(point);
    }
}
