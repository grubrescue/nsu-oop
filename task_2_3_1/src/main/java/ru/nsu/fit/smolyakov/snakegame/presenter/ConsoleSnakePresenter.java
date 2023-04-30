package ru.nsu.fit.smolyakov.snakegame.presenter;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.view.ConsoleView;

import java.io.IOException;
import java.util.*;

public class ConsoleSnakePresenter extends SnakePresenter {
    private Timer timer;

    private Resources resources
        = new Resources();

    private ConsoleView view;

    @Override
    protected void runFramesUpdater() {
        timer = new Timer();
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    drawFrame();
                    showMessage("READY???");
                    refresh();
                }
            },
            0
        );

        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    drawFrame();
                    showMessage("GO!!!!");
                    refresh();
                }
            },
            SnakePresenter.START_SLEEP_TIME_MILLIS
        );

        timer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    update();
                }
            },
            SnakePresenter.START_SLEEP_TIME_MILLIS * 2,
            properties.speed().getFrameDelayMillis());
    }

    @Override
    protected void stopFramesUpdater() {
        timer.cancel();
    }

    private final Map<Character, EventAction> characterEventActionMap
        = Map.ofEntries(
            Map.entry('r', SnakePresenter.EventAction.RESTART),
            Map.entry('к', SnakePresenter.EventAction.RESTART),
            Map.entry('q', SnakePresenter.EventAction.EXIT),
            Map.entry('й', SnakePresenter.EventAction.EXIT),

            Map.entry('w', SnakePresenter.EventAction.UP),
            Map.entry('ц', SnakePresenter.EventAction.UP),
            Map.entry('s', SnakePresenter.EventAction.DOWN),
            Map.entry('ы', SnakePresenter.EventAction.DOWN),
            Map.entry('a', SnakePresenter.EventAction.LEFT),
            Map.entry('ф', SnakePresenter.EventAction.LEFT),
            Map.entry('d', SnakePresenter.EventAction.RIGHT),
            Map.entry('в', SnakePresenter.EventAction.RIGHT)
        );

    private final Map<KeyType, SnakePresenter.EventAction> keyTypeEventActionMap
        = Map.of(
            KeyType.ArrowDown, SnakePresenter.EventAction.DOWN,
            KeyType.ArrowUp, SnakePresenter.EventAction.UP,
            KeyType.ArrowLeft, SnakePresenter.EventAction.LEFT,
            KeyType.ArrowRight, SnakePresenter.EventAction.RIGHT
        );

    public void keyEventHandler(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            var keyCharacterEvent = characterEventActionMap.get(keyStroke.getCharacter());
            if (keyCharacterEvent != null) {
                keyCharacterEvent.execute(this);
            }
        } else {
            var keyTypeEvent = keyTypeEventActionMap.get(keyStroke.getKeyType());
            if (keyTypeEvent != null) {
                keyTypeEvent.execute(this);
            }
        }
    }

    public void setView(ConsoleView view) {
        this.view = view;
    }

    /**
     * Sets the current amount of points the player
     * has on an attached scoreboard.
     * One is located two rows above the game field.
     *
     * @param scoreAmount the amount of points
     */
    @Override
    public void setScoreAmount(int scoreAmount) {
        String scoreString = "Score: " + scoreAmount;

        for (int i = 0; i < scoreString.length(); i++) {
            view.setCharacter(i, properties.height(), resources.scoreChar.withCharacter(scoreString.charAt(i)));
        }
    }

    /**
     * Draws an apple on the game field.
     * One is drawn as a green-colored {@code $} symbol.
     *
     * @param apple the apple to draw
     */
    @Override
    public void drawApple(Apple apple) {
        view.setCharacter(apple.point().x(), apple.point().y(), resources.apple);
    }

    /**
     * Draws a barrier on the game field.
     * Each point is drawn as a yellow-colored {@code *} symbol.
     *
     * @param barrier the barrier to draw
     */
    @Override
    public void drawBarrier(Barrier barrier) {
        barrier.barrierPoints().forEach(point -> view.setCharacter(point.x(), point.y(), resources.barrier));
    }

    private void drawSnake(Snake snake, TextCharacter head, TextCharacter tail) {
        snake.getSnakeBody().getTail().forEach(point -> view.setCharacter(point.x(), point.y(), tail));
        view.setCharacter(snake.getSnakeBody().getHead().x(), snake.getSnakeBody().getHead().y(), head);
    }

    /**
     * Draws the player snake on the game field.
     * The head is drawn as a white-colored {@code @} symbol.
     * The tail is drawn as a white-colored {@code 0} symbol.
     *
     * @param snake the snake to draw
     */
    @Override
    public void drawPlayerSnake(Snake snake) {
        drawSnake(snake, resources.playerSnakeHead, resources.playerSnakeTail);
    }

    /**
     * Draws the enemy snake on the game field.
     * The head is drawn as a red-colored {@code @} symbol.
     * The tail is drawn as a red-colored {@code 0} symbol.
     *
     * @param snake the snake to draw
     */
    @Override
    public void drawEnemySnake(Snake snake) {
        drawSnake(snake, resources.enemySnakeHead, resources.enemySnakeTail);
    }

    /**
     * Shows a message on the game field.
     * The message is centered on the screen.
     *
     * @param string the message to show
     */
    @Override
    public void showMessage(String string) {
        for (int i = 0; i < string.length(); i++) {
            view.setCharacter(5 + i, properties.height() / 2,
                resources.messageChar.withCharacter(string.charAt(i)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        view.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() {
        view.refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        view.close();
    }

    private class Resources {
        TextCharacter apple =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('$')
                .withForegroundColor(TextColor.ANSI.GREEN);

        TextCharacter barrier =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('*')
                .withForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);

        TextCharacter playerSnakeHead =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('@')
                .withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

        TextCharacter playerSnakeTail =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('0')
                .withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

        TextCharacter enemySnakeHead =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('@')
                .withForegroundColor(TextColor.ANSI.RED_BRIGHT);

        TextCharacter enemySnakeTail =
            TextCharacter.DEFAULT_CHARACTER
                .withCharacter('0')
                .withForegroundColor(TextColor.ANSI.RED_BRIGHT);

        TextCharacter messageChar =
            TextCharacter.DEFAULT_CHARACTER
                .withForegroundColor(TextColor.ANSI.BLACK)
                .withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);

        TextCharacter scoreChar =
            TextCharacter.DEFAULT_CHARACTER
                .withForegroundColor(TextColor.ANSI.BLACK)
                .withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);
    }
}
