package ru.nsu.fit.smolyakov.snake.view;

import ru.nsu.fit.smolyakov.snake.model.Apple;
import ru.nsu.fit.smolyakov.snake.model.Barrier;
import ru.nsu.fit.smolyakov.snake.model.Snake;

import java.util.Set;

public interface View {
    void setScoreAmount(int scoreAmount);

    void draw(Set<Apple> appleSet);

    void draw(Barrier barrier);

    void draw(Snake snake);

    void showMessage(String string);

    void clear();
}
