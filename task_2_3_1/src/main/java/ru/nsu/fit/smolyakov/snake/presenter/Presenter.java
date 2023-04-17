package ru.nsu.fit.smolyakov.snake.presenter;

import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snake.view.View;

public class Presenter {
    private final View view;
    private GameField model;

    private final GameFieldProperties properties;
    private Thread thread;

    public Presenter(View view, GameFieldProperties properties) {
        this.view = view;
        this.properties = properties;
    }

    public void start() {
        model = new GameFieldImpl(properties);
        
        thread = new Thread(() -> {
            for (int i = 3; i >= 0; i--) {
                updateView();
                System.out.println(model.getPlayerSnake().getSnakeBody().getHead());
                System.out.println(model.getPlayerSnake().getSnakeBody().getTail().get(0));

                if (i != 0) {
                    view.showMessage("Game starts in " + i);
                } else {
                    view.showMessage("Go!");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
            }

            while (!Thread.currentThread().isInterrupted()) {
                var playerDeath = model.update();

                updateView();

                if (playerDeath) {
                    view.showMessage("You died! You earned " + model.getPlayerSnake().getPoints() + " points.");
                    return;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        );
        thread.start();
    }

    public void updateView() {
        view.clear();
        view.draw(model.getBarrier());
        view.draw(model.getApplesSet());
        view.draw(model.getPlayerSnake());
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