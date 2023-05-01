package ru.nsu.fit.smolyakov.snakegame.presenter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.Map;
import java.util.Objects;

public class JavaFxSnakePresenter extends SnakePresenter {
    private final Map<KeyCode, EventAction> eventActionMap
        = Map.of(
        KeyCode.R, SnakePresenter.EventAction.RESTART,
        KeyCode.Q, SnakePresenter.EventAction.EXIT,

        KeyCode.UP, SnakePresenter.EventAction.UP,
        KeyCode.W, SnakePresenter.EventAction.UP,

        KeyCode.DOWN, SnakePresenter.EventAction.DOWN,
        KeyCode.S, SnakePresenter.EventAction.DOWN,

        KeyCode.LEFT, SnakePresenter.EventAction.LEFT,
        KeyCode.A, SnakePresenter.EventAction.LEFT,

        KeyCode.RIGHT, SnakePresenter.EventAction.RIGHT,
        KeyCode.D, SnakePresenter.EventAction.RIGHT
    );
    private int resX;
    private int resY;
    @FXML
    private Canvas canvas;
    @FXML
    private Text scoreAmountText;
    @FXML
    private Text messageText;
    private Resources resources;
    private Timeline timeline;

    private void initializeEventActions() {
        this.canvas.getScene().setOnKeyPressed(e -> {
            var eventAction = eventActionMap.get(e.getCode());
            if (eventAction != null)
                eventAction.execute(this);
        });

        this.canvas.getScene().getWindow().setOnCloseRequest(e ->
            SnakePresenter.EventAction.EXIT.execute(this)
        );
    }

    public void initializeField() {
        this.resX = properties.width() * properties.javaFxScaling();
        this.resY = properties.height() * properties.javaFxScaling();

        canvas.setWidth(this.resX);
        canvas.setHeight(this.resY);

        initializeEventActions();
        resources = new Resources();
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

    @Override
    protected void runFramesUpdater() {
        drawFrame();
        showMessage("Prepare...");
        refresh();

        timeline = new Timeline(
            new KeyFrame(
                Duration.millis(START_SLEEP_TIME_MILLIS),
                event -> {
                    drawFrame();
                    showMessage("FIGHT!!!1");
                    refresh();
                }
            )
        );

        timeline.setCycleCount(2);
        timeline.play();

        timeline.setOnFinished(e -> {
            timeline.stop();
            timeline = new Timeline(
                new KeyFrame(Duration.millis(properties.speed().getFrameDelayMillis()), e1 -> this.update())
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }

    @Override
    protected void stopFramesUpdater() {
        timeline.stop();
    }

    /**
     * Sets the score amount to be displayed on the screen.
     * Scoreboard is implemented as {@code scoreAmountText} JavaFx node.
     *
     * @param scoreAmount score amount
     */
    @Override
    protected void setScoreAmount(int scoreAmount) {
        this.scoreAmountText.setText(String.valueOf(scoreAmount));
    }

    /**
     * Draws the apple on the screen.
     * Sprite used is located at {@code /sprites/apple.png}.
     *
     * @param apple the apple to draw
     */
    @Override
    protected void drawApple(Apple apple) {
        drawFigure(apple.point(), resources.apple);
    }

    /**
     * Draws the barrier on the screen.
     * Sprite used for each barrier point is located at {@code /sprites/barrier.png}.
     *
     * @param barrier the barrier to draw
     */
    @Override
    protected void drawBarrier(Barrier barrier) {
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
    @Override
    protected void drawPlayerSnake(Snake snake) {
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
    protected void drawEnemySnake(Snake snake) {
        drawSnake(snake, resources.enemySnakeHead, resources.enemySnakeTail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, this.resX, this.resY);
        messageText.setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void showMessage(String message) {
        messageText.setText(message);
    }

    /**
     * {@inheritDoc}
     */
    protected void close() {
        ((Stage) canvas.getScene().getWindow()).close();
    }

    /**
     * Does nothing.
     */
    @Override
    protected void refresh() {
    }

    private class Resources {
        Image apple = imageInstance("/sprites/apple.png");
        Image barrier = imageInstance("/sprites/barrier.png");
        Image playerSnakeHead = imageInstance("/sprites/player/head.png");
        Image playerSnakeTail = imageInstance("/sprites/player/tail.png");
        Image enemySnakeHead = imageInstance("/sprites/enemy/head.png");
        Image enemySnakeTail = imageInstance("/sprites/enemy/tail.png");

        private Image imageInstance(String path) {
            return new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(path)),
                properties.javaFxScaling(),
                properties.javaFxScaling(),
                true,
                false
            );
        }
    }
}
