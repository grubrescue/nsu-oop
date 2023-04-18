package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NotWonnaDieAISnake extends AISnake {
    public NotWonnaDieAISnake(GameField gameField) {
        super(gameField);
    }

    private final Random rand = new SecureRandom();

    private boolean isCollidingTurn(MovingDirection direction) {
        return getGameField().getBarrier().met(getSnakeBody().getHead().shift(direction.move()));
    }

    @Override
    public void thinkAboutTurn() {
        List<MovingDirection> options = new LinkedList<>(
            Arrays.stream(MovingDirection.values())
                .filter(movePoint -> !isCollidingTurn(movePoint))
                .filter(movePoint -> !movePoint.move().equals(getMovingDirection().opposite()))
                .toList()
        );

        if (!options.isEmpty()) {
            setMovingDirection((MovingDirection) options.get(rand.nextInt(options.size())));
        }
    }
}
