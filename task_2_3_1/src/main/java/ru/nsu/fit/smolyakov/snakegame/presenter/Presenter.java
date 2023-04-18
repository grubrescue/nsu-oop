package ru.nsu.fit.smolyakov.snakegame.presenter;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snakegame.view.View;

/**
 * A presenter that connects a model and a view.
 *
 * <p>{@link #start()} method starts a new thread with the game's main loop.
 * {@link #onExitKeyPressed()} method stops both this presenter and an attached view.
 * {@link #onRestartKeyPressed()} method restarts the game.
 * {@link #onLeftKeyPressed()}, {@link #onRightKeyPressed()}, {@link #onUpKeyPressed()} and
 * {@link #onDownKeyPressed()} change the direction of the player snake.
 *
 * <p>One works correctly with both JavaFX and console views.
 */
public class Presenter {
    private final View view;
    private final PresenterProperties presenterProperties;
    private GameField model;

    private Thread thread;

    /**
     * Creates a presenter with the specified view, model and properties.
     *
     * @param view a view
     * @param model a model
     * @param properties properties of the presenter
     */
    public Presenter(View view, GameField model, PresenterProperties properties) {
        this.view = view;
        this.model = model;
        this.presenterProperties = properties;
    }

    private void startTimeOut() throws InterruptedException {
        for (int i = 3; i >= 0; i--) {
            showFrame();

            if (i != 0) {
                view.showMessage("Game starts in " + i);
            } else {
                view.showMessage("Go!");
            }
            Thread.sleep(presenterProperties.startTimeoutMillis());
        }
    }

    private void mainLoop() {
        try {
            startTimeOut();
        } catch (InterruptedException e) {
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            model.getAISnakeList().forEach(AISnake::thinkAboutTurn);
            var playerAlive = model.update();

            showFrame();

            if (!playerAlive) {
                view.showMessage("You died! You earned " + model.getPlayerSnake().getPoints() + " points.");
                return;
            }

            try {
                Thread.sleep(presenterProperties.speed().getFrameDelayMillis());
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * Starts a new game.
     */
    public void start() {
        model = model.newGame();

        // TODO вынести в отдельный метод?
        thread = new Thread(this::mainLoop);
        thread.start();
    }

    private void showFrame() {
        view.clear();
        view.drawBarrier(model.getBarrier());
        model.getApplesSet().forEach(view::drawApple);
        view.drawPlayerSnake(model.getPlayerSnake());
        model.getAISnakeList().forEach(view::drawEnemySnake);
        view.setScoreAmount(model.getPlayerSnake().getPoints());
    }

    /**
     * Changes the direction of the player snake to left.
     */
    public void onLeftKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.LEFT);
    }

    /**
     * Changes the direction of the player snake to right.
     */
    public void onRightKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.RIGHT);
    }

    /**
     * Changes the direction of the player snake to up.
     */
    public void onUpKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.UP);
    }

    /**
     * Changes the direction of the player snake to down.
     */
    public void onDownKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.DOWN);
    }

    /**
     * Restarts the game.
     */
    public void onRestartKeyPressed() {
        thread.interrupt();
        start();
    }

    /**
     * Stops the game.
     */
    public void onExitKeyPressed() {
        thread.interrupt();
        view.close();
    }
}
