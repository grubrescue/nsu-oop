package ru.nsu.fit.smolyakov.snake.presenter;

import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.view.View;

public class Presenter {
    private final View view;
    private GameField model;

    private final int height;
    private final int width;
    private Thread thread;

    public Presenter(View view, int width, int height) {
        this.view = view;
        this.height = height;
        this.width = width;
    }

    public void start() {
        model = new GameFieldImpl(width, height, 3);
        
        thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }

            while (!Thread.currentThread().isInterrupted()) {
                var playerDeath = model.update();

                view.clear();

                view.draw(model.getApplesSet());
                view.draw(model.getBarrier());
                view.draw(model.getPlayerSnake());
                view.setScoreAmount(model.getPlayerSnake().getPoints());

                if (playerDeath) {
                    view.showMessage("You died! You earned " + model.getPlayerSnake().getPoints() + " points.");
                    return;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        );
        thread.start();
    }

    public void update() {

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
