package ru.nsu.fit.smolyakov.snakegame.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.nsu.fit.smolyakov.snakegame.presenter.ConsoleSnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Console view of the game. Uses Lanterna library.
 *
 * <p>Scene size is equal to the game field size plus additional
 * 2 rows for the score.
 */
public class ConsoleView {
    private final GameProperties properties;
    private final Terminal terminal;
    private final Screen screen;
    private final Map<Character, SnakePresenter.EventAction> characterEventActionMap
        = new HashMap<>();
    private final Map<KeyType, SnakePresenter.EventAction> keyTypeEventActionMap
        = new HashMap<>();
    private ConsoleSnakePresenter presenter;
    private Thread inputPollThread;

    /**
     * Creates a new {@link ConsoleView} instance.
     *
     * @param properties properties of the game field
     * @throws IOException if an I/O error occurs
     */
    public ConsoleView(GameProperties properties) throws IOException {
        this.properties = properties;

        this.terminal = new DefaultTerminalFactory()
            .setInitialTerminalSize(new TerminalSize(properties.width(), properties.height() + 2))
            .setPreferTerminalEmulator(true)
            .createTerminal();
        this.terminal.setCursorVisible(false);
        this.screen = new TerminalScreen(terminal);
    }


    private void pollKeyboardInput() {
        KeyStroke keyStroke;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                keyStroke = terminal.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            presenter.keyEventHandler(keyStroke);
        }
    }

    /**
     * Sets the {@link SnakePresenter} for this {@link ConsoleView}.
     *
     * @param snakePresenter the {@link SnakePresenter} to set
     */
    public void setPresenter(ConsoleSnakePresenter snakePresenter) {
        this.presenter = Objects.requireNonNull(snakePresenter);
    }

    /**
     * Starts the view.
     */
    public void start() {
        try {
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inputPollThread = new Thread(this::pollKeyboardInput);
        inputPollThread.start();
    }

    /**
     * Clears the screen.
     */
    public void clear() {
        screen.clear();
    }

    /**
     * Refreshes the screen.
     */
    public void refresh() {
        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets a character to the specified position.
     *
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param character the character to set
     */
    public void setCharacter(int x, int y, TextCharacter character) {
        screen.setCharacter(x, y, character);
    }

    /**
     * Closes the view.
     */
    public void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inputPollThread.interrupt();
    }
}
