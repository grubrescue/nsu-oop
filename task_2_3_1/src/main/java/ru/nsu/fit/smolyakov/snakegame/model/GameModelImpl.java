package ru.nsu.fit.smolyakov.snakegame.model;

import ru.nsu.fit.smolyakov.snakegame.model.snake.CollisionSolver;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.*;
import ru.nsu.fit.smolyakov.snakegame.point.Point;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 * It also contains a {@link Barrier} that is a reason of collisions that may lead to
 * one (or all) of snakes' death.
 *
 * <p>As specified in {@link GameProperties} (usually deserialized from {@code gamedata/config/gameProperties.yaml}
 * file), this class dynamically loads {@link AISnake}s that are capable of playing the game, as well as the user.
 *
 * <p>All other properties also specified in {@link GameProperties}, such as {@code apples},
 * {@code width} and {@code height} are used to initialize the game field and affect the representation of the latter.
 */
public class GameModelImpl implements GameModel {
    public final static int MAX_GENERATION_ITERATIONS = 5000;

    private final GameProperties properties;

    private final Snake playerSnake;
    private final Set<Apple> applesSet;
    private final Barrier barrier;
    private List<AISnake> AISnakesList;

    private final Apple.Factory appleFactory = new Apple.Factory(this, MAX_GENERATION_ITERATIONS);

    private void fulfilApplesSet() {
        while (applesSet.size() < properties.apples()) {
            applesSet.add(appleFactory.generateRandom());
        }
    }

    private Optional<AISnake> aiSnakeFromUrl(String url) {
        Class<?> aiSnakeClass;

        try {
            aiSnakeClass = Class.forName(url);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }

        if (!AISnake.class.isAssignableFrom(aiSnakeClass)) {
            return Optional.empty();
        }

        AISnake aiSnake;
        try {
            aiSnake = (AISnake) aiSnakeClass.getDeclaredConstructor(GameModel.class).newInstance(this);
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            return Optional.empty();
        }

        return Optional.of(aiSnake);
    }

    /**
     * Creates a game field with the specified properties.
     *
     * @param properties properties of the game field
     */
    public GameModelImpl(GameProperties properties) {
        this.properties = properties;
        this.barrier = Barrier.fromResource(properties);

        this.playerSnake = new Snake(this);
        this.AISnakesList = properties.aiClassNamesList()
            .stream()
            .map(this::aiSnakeFromUrl)
            .flatMap(Optional::stream)
            .toList();

        this.applesSet = new HashSet<>();
        fulfilApplesSet();
    }

    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    @Override
    public List<AISnake> getAISnakeList() {
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
        return (playerSnake == null || !playerSnake.getSnakeBody().headCollision(point))
            && (playerSnake == null || !playerSnake.getSnakeBody().tailCollision(point))
            && (barrier == null || !barrier.met(point))
            && (applesSet == null || !applesSet.contains(new Apple(point)))
            && (AISnakesList == null
                || AISnakesList.stream().noneMatch(snake -> snake.getSnakeBody().headCollision(point)))
            && (AISnakesList == null
                || AISnakesList.stream().noneMatch(snake -> snake.getSnakeBody().tailCollision(point)));
    }

    /**
     * Checks if the player snake collides with any AI snake.
     *
     * @return {@code true} if the player snake collides with any AI snake,
     *         {@code false} otherwise
     */
    private boolean checkPlayerSnakeCollisions() {
        var iter = AISnakesList.iterator();
        while (iter.hasNext()) {
            var snake = iter.next();
            if (CollisionSolver.solve(playerSnake, snake) == CollisionSolver.Result.BOTH_DEAD) {
                iter.remove();
                return true;
            }
        }
        // TODO змейки между собой

        return false;
    }

    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     *         {@code false} otherwise
     */
    @Override
    public boolean update() {
        var alive = playerSnake.update();
        AISnakesList = AISnakesList.stream().filter(Snake::update)
            .collect(Collectors.toList());

        fulfilApplesSet();

        var collisions = checkPlayerSnakeCollisions();
        return alive && !collisions;
    }

    /**
     * Returns the properties of the game field.
     *
     * @return the properties of the game field
     */
    public GameProperties getProperties() {
        return properties;
    }

    /**
     * Returns a new game field with the same properties as this one.
     *
     * @return a new game field with the same properties as this one
     */
    @Override
    public GameModel newGame() {
        return new GameModelImpl(properties);
    }
}