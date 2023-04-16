package ru.nsu.fit.smolyakov.snake.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 */
public class GameFieldImpl implements GameField {
    private final int width;
    private final int height;
    private final int maxApples;

    private final Snake playerSnake;
    private List<Snake> AISnakesList;
    private final Set<Apple> applesSet;
    private final Barrier barrier;

    public GameFieldImpl(int width, int height, int maxApples) {
        this.barrier = new Barrier(Set.of());

        this.width = width;
        this.height = height;
        this.maxApples = maxApples;
        this.AISnakesList = List.of();

        this.playerSnake = new Snake(this);

        this.applesSet = new HashSet<>(); // TODO сделать нормально
        while (applesSet.size() < maxApples) {
            applesSet.add(new Apple.Factory(this).generateRandom(10000));
        }
    }

    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    @Override
    public List<Snake> getAISnakeList() {
        return AISnakesList;
    }

    /**
     * Returns a player-driven snake.
     *
     * @return a player-driven snake
     */
    @Override
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    /**
     * Returns a set of apples.
     *
     * @return a set of apples
     */
    @Override
    public Set<Apple> getApplesSet() {
        return applesSet;
    }

    /**
     * Returns a barrier.
     *
     * @return a barrier
     */
    @Override
    public Barrier getBarrier() {
        return barrier;
    }

    /**
     * Checks if the point is free.
     *
     * @param point point
     * @return {@code true} if the point is free,
     * {@code false} otherwise
     */
    @Override
    public boolean isFree(Point point) {
//        return playerSnake.getSnakeBody().headCollision(point) ||
//            playerSnake.getSnakeBody().tailCollision(point) ||
//            barrier.met(point) ||
//            applesSet.contains(new Apple(point)) ||
//            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().headCollision(point)) ||
//            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().tailCollision(point));

        return true;
    }

    private boolean checkPlayerSnakeCollisions() {
        if (playerSnake.getSnakeBody().tailCollision(playerSnake.getSnakeBody().getHead())) {
            return true;
        } else {
            var iter = AISnakesList.iterator();
            while (iter.hasNext()) {
                var snake = iter.next();
                if (snake.getSnakeBody().headCollision(playerSnake.getSnakeBody().getHead())) {
                    iter.remove();
                    return true;
                } else if (snake.getSnakeBody().tailCollision(playerSnake.getSnakeBody().getHead())) {
                    snake.getSnakeBody().cutTail(playerSnake.getSnakeBody().getHead());
                } else if (playerSnake.getSnakeBody().tailCollision(snake.getSnakeBody().getHead())) {
                    playerSnake.getSnakeBody().cutTail(snake.getSnakeBody().getHead());
                } else if (snake.getSnakeBody().tailCollision(snake.getSnakeBody().getHead())) {
                    snake.getSnakeBody().cutTail(snake.getSnakeBody().getHead());
                }
            }
            //TODO змейки жрут сами себя
        }

        return false;
    }

    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     * {@code false} otherwise
     */
    @Override
    public boolean update() {
        var death = playerSnake.update();
        AISnakesList = AISnakesList.stream().filter(Snake::update)
            .collect(Collectors.toList());

        death = checkPlayerSnakeCollisions();

        while (applesSet.size() < maxApples) {
            applesSet.add(new Apple.Factory(this).generateRandom(10000));
            // TODO объединить везде maxIterations, вынести в константу
            // TODO одна фабрика для всех яблок
        }

        return death;
    }

    /**
     * Returns the width of the game field.
     *
     * @return the width of the game field
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the game field.
     *
     * @return the height of the game field
     */
    @Override
    public int getHeight() {
        return height;
    }
}
