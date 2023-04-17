package ru.nsu.fit.smolyakov.snake.view;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.nsu.fit.smolyakov.snake.model.Apple;
import ru.nsu.fit.smolyakov.snake.model.Barrier;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;
import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class ConsoleView implements View {
    private class Resources {
        TextCharacter apple;
        TextCharacter barrier;
        TextCharacter playerSnakeHead;
        TextCharacter playerSnakeTail;
        TextCharacter enemySnakeHead;
        TextCharacter enemySnakeTail;

        TextCharacter messageChar;
        TextCharacter scoreChar;
    }

    private final GameFieldProperties properties;

    private Presenter presenter;

    private Terminal terminal;
    private Screen screen;

    private Resources resources;

    private Thread inputPollThread;

    public ConsoleView(GameFieldProperties properties) throws IOException {
        this.properties = properties;

        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);

        resources = new Resources();
        resources.apple =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('$').withForegroundColor(TextColor.ANSI.GREEN);
        resources.barrier =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('*').withForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        resources.playerSnakeHead =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('@').withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        resources.playerSnakeTail =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        resources.enemySnakeHead =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('@').withForegroundColor(TextColor.ANSI.RED_BRIGHT);
        resources.enemySnakeTail =
            TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(TextColor.ANSI.RED_BRIGHT);
        resources.messageChar =
            TextCharacter.DEFAULT_CHARACTER.withForegroundColor(TextColor.ANSI.BLACK).withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);
        resources.scoreChar =
            TextCharacter.DEFAULT_CHARACTER.withForegroundColor(TextColor.ANSI.BLACK).withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = Objects.requireNonNull(presenter);
    }

    public void start() {
        try {
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inputPollThread = new Thread(() ->{
            KeyStroke keyStroke;

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    keyStroke = terminal.readInput();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                switch (keyStroke.getKeyType()) {
                    case ArrowDown -> presenter.onDownKeyPressed();
                    case ArrowUp -> presenter.onUpKeyPressed();
                    case ArrowLeft -> presenter.onLeftKeyPressed();
                    case ArrowRight -> presenter.onRightKeyPressed();
                    case Character -> {
                        switch (keyStroke.getCharacter()) {
                            case 'r' -> presenter.onRestartKeyPressed();
                            case 'q' -> presenter.onExitKeyPressed();
                        }
                    }

                }
            }
        });
        inputPollThread.start();
    }

    @Override
    public void setScoreAmount(int scoreAmount) {
        String scoreString = "Score: " + scoreAmount;

        for (int i = 0; i < scoreString.length(); i++) {
            screen.setCharacter(i, properties.height(), resources.scoreChar.withCharacter(scoreString.charAt(i)));
        }

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawAppleSet(Set<Apple> appleSet) {
        appleSet.forEach(apple -> screen.setCharacter(apple.point().x(), apple.point().y(), resources.apple));
        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawBarrier(Barrier barrier) {
        barrier.barrierPoints().forEach(point -> screen.setCharacter(point.x(), point.y(), resources.barrier));
        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawPlayerSnake(Snake snake) {
        snake.getSnakeBody().getTail().forEach(point -> screen.setCharacter(point.x(), point.y(), resources.playerSnakeTail));
        screen.setCharacter(snake.getSnakeBody().getHead().x(), snake.getSnakeBody().getHead().y(), resources.playerSnakeHead);

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showMessage(String string) {
        for (int i = 0; i < string.length(); i++) {
            screen.setCharacter(5 + i, properties.height()/2,
                resources.messageChar.withCharacter(string.charAt(i)));
        }

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        screen.clear();
    }

    @Override
    public void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inputPollThread.interrupt();
    }
}
