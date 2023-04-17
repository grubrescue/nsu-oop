package ru.nsu.fit.smolyakov.snakegame.view;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;

import java.util.Set;

public interface View {
    void setScoreAmount(int scoreAmount);

    void drawAppleSet(Set<Apple> appleSet);

    void drawBarrier(Barrier barrier);

    void drawPlayerSnake(Snake snake);

    void showMessage(String string);

    void clear();

    void close();
}
