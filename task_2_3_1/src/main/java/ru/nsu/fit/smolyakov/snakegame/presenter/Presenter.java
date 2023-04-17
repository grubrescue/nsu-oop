package ru.nsu.fit.smolyakov.snakegame.presenter;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snakegame.view.View;

public class Presenter {
    private final View view;
    private final PresenterProperties presenterProperties;
    private GameField model;

    private Thread thread;

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
            var playerAlive = model.update();
            model.getAISnakeList().forEach(AISnake::thinkAboutTurn);
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

    ;

    public void start() {
        model = model.newGame();

        // TODO вынести в отдельный метод?
        thread = new Thread(this::mainLoop);
        thread.start();
    }

    public void showFrame() {
        view.clear();
        view.drawBarrier(model.getBarrier());
        model.getApplesSet().forEach(view::drawApple);
        view.drawPlayerSnake(model.getPlayerSnake());
        model.getAISnakeList().forEach(view::drawEnemySnake);
        view.setScoreAmount(model.getPlayerSnake().getPoints());
    }

    public void onLeftKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.LEFT);
    }

    public void onRightKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.RIGHT);
    }

    public void onUpKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.UP);
    }

    public void onDownKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.DOWN);
    }

    public void onRestartKeyPressed() {
        thread.interrupt();
        start();
    }

    public void onExitKeyPressed() {
        thread.interrupt();
        view.close();
    }
}
