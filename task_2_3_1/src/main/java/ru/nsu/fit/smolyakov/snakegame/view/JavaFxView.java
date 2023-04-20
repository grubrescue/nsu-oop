package ru.nsu.fit.smolyakov.snakegame.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.point.Point;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * An implementation of the {@link View} interface that uses JavaFX.
 *
 * <p>A scene configuration is specified in the {@code gamefield.fxml} file and
 * all sprites are located in the {@code /sprites} directory.
 *
 * <p>This implementation is capable of scaling the game field correctly if the resolution
 * specified in {@code JavaFxProperties} is a multiple of the doubled size of the game field
 * in relevant dimensions.
 */
public class JavaFxView implements View, Initializable {
    private SnakePresenter snakePresenter;

    private GameProperties properties;
    private int resX;
    private int resY;

    @FXML
    private Pane pane;

    @FXML
    private Canvas canvas;

    @FXML
    private Text scoreAmountText;

    private Resources resources;

    private Map<KeyCode, SnakePresenter.EventAction> eventActionMap
        = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private Image imageInstance(String path) {
        return new Image(
            Objects.requireNonNull(getClass().getResourceAsStream(path)),
            properties.javaFxScaling(),
            properties.javaFxScaling(),
            true,
            false
        );
    }

    private void initializeResources() {
        resources = new Resources();
        resources.apple = imageInstance("/sprites/apple.png");
        resources.barrier = imageInstance("/sprites/barrier.png");
        resources.playerSnakeHead = imageInstance("/sprites/player/head.png");
        resources.playerSnakeTail = imageInstance("/sprites/player/tail.png");
        resources.enemySnakeHead = imageInstance("/sprites/enemy/head.png");
        resources.enemySnakeTail = imageInstance("/sprites/enemy/tail.png");
    }

    private void initializeEventActionMap() {
        eventActionMap.put(KeyCode.R, SnakePresenter.EventAction.RESTART);
        eventActionMap.put(KeyCode.Q, SnakePresenter.EventAction.EXIT);

        eventActionMap.put(KeyCode.UP, SnakePresenter.EventAction.UP);
        eventActionMap.put(KeyCode.W, SnakePresenter.EventAction.UP);

        eventActionMap.put(KeyCode.DOWN, SnakePresenter.EventAction.DOWN);
        eventActionMap.put(KeyCode.S, SnakePresenter.EventAction.DOWN);

        eventActionMap.put(KeyCode.LEFT, SnakePresenter.EventAction.LEFT);
        eventActionMap.put(KeyCode.A, SnakePresenter.EventAction.LEFT);

        eventActionMap.put(KeyCode.RIGHT, SnakePresenter.EventAction.RIGHT);
        eventActionMap.put(KeyCode.D, SnakePresenter.EventAction.RIGHT);
    }

    private void initializeEventActions() {
        this.canvas.getScene().setOnKeyPressed(e -> {
            var eventAction = eventActionMap.get(e.getCode());
            if (eventAction != null)
                eventAction.execute(snakePresenter);
        });

        this.canvas.getScene().getWindow().setOnCloseRequest(e ->
            SnakePresenter.EventAction.EXIT.execute(snakePresenter)
        );
    }

    /**
     * Initializes the view so that its parameters correspond to {@code javaFxProperties}
     * and {@code properties}. Also, the {@code snakePresenter} is set as the view's snakePresenter.
     *
     * @param properties     game properties
     * @param snakePresenter snake presenter
     */
    public void initializeField(GameProperties properties,
                                SnakePresenter snakePresenter) {
        if (snakePresenter == null || properties == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }

        this.snakePresenter = snakePresenter;
        this.properties = properties;

        pane.setStyle("-fx-background-color: white");
        
        this.resX = properties.width() * properties.javaFxScaling();
        this.resY = properties.height() * properties.javaFxScaling();

        canvas.setWidth(this.resX);
        canvas.setHeight(this.resY);

        initializeEventActionMap();
        initializeEventActions();
        initializeResources();
    }

    private void drawFigure(Point point, Image image) {
        var graphicsContext = canvas.getGraphicsContext2D(); // TODO надо ли хранить в поле или получать каждый раз???

        graphicsContext.drawImage(
            image,
            point.x() * properties.javaFxScaling(),
            point.y() * properties.javaFxScaling(),
            properties.javaFxScaling(),
            properties.javaFxScaling()
        );
    }

    /**
     * Sets the score amount to be displayed on the screen.
     * Scoreboard is implemented as {@code scoreAmountText} JavaFx node.
     *
     * @param scoreAmount score amount
     */
    public void setScoreAmount(int scoreAmount) {
        this.scoreAmountText.setText(String.valueOf(scoreAmount));
    }

    /**
     * Draws the apple on the screen.
     * Sprite used is located at {@code /sprites/apple.png}.
     *
     * @param apple the apple to draw
     */
    public void drawApple(Apple apple) {
        drawFigure(apple.point(), resources.apple);
    }

    /**
     * Draws the barrier on the screen.
     * Sprite used for each barrier point is located at {@code /sprites/barrier.png}.
     *
     * @param barrier the barrier to draw
     */
    public void drawBarrier(Barrier barrier) {
        barrier.barrierPoints().forEach(point -> drawFigure(point, resources.barrier));
    }

    private void drawSnake(Snake snake, Image head, Image tail) {
        drawFigure(snake.getSnakeBody().getHead(), head);
        snake.getSnakeBody().getTail().forEach(point -> drawFigure(point, tail));
    }

    /**
     * Draws the player snake on the screen.
     * Sprites used are located at {@code /sprites/player/head.png} and
     * {@code /sprites/player/tail.png}.
     *
     * @param snake the snake to draw
     */
    public void drawPlayerSnake(Snake snake) {
        drawSnake(snake, resources.playerSnakeHead, resources.playerSnakeTail);
    }

    /**
     * Draws the enemy snake on the screen.
     * Sprites used are located at {@code /sprites/enemy/head.png} and
     * {@code /sprites/enemy/tail.png}.
     *
     * @param snake the snake to draw
     */
    @Override
    public void drawEnemySnake(Snake snake) {
        drawSnake(snake, resources.enemySnakeHead, resources.enemySnakeTail);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, this.resX, this.resY);
    }

    /**
     * {@inheritDoc}
     */
    public void showMessage(String message) {
        canvas.getGraphicsContext2D().strokeText(message, this.resX / 3, this.resY / 2);
        // TODO сделать нормально в ссене билдере (или не делать уж...)
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        ((Stage) canvas.getScene().getWindow()).close();
    }


    /**
     * Does nothing.
     */
    @Override
    public void refresh() {
    }

    private class Resources {
        Image apple;
        Image barrier;
        Image playerSnakeHead;
        Image playerSnakeTail;
        Image enemySnakeHead;
        Image enemySnakeTail;
    }
}
