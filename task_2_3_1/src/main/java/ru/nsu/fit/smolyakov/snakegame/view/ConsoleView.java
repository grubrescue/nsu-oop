//package ru.nsu.fit.smolyakov.snakegame.view;
//
//import com.googlecode.lanterna.TerminalSize;
//import com.googlecode.lanterna.TextCharacter;
//import com.googlecode.lanterna.TextColor;
//import com.googlecode.lanterna.input.KeyStroke;
//import com.googlecode.lanterna.input.KeyType;
//import com.googlecode.lanterna.screen.Screen;
//import com.googlecode.lanterna.screen.TerminalScreen;
//import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
//import com.googlecode.lanterna.terminal.Terminal;
//import ru.nsu.fit.smolyakov.snakegame.model.Apple;
//import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
//import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
//import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
//import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * Console view of the game. Uses Lanterna library.
// *
// * <p>Scene size is equal to the game field size plus additional
// * 2 rows for the score.
// */
//public class ConsoleView {
//    private final GameProperties properties;
//    private final Terminal terminal;
//    private final Screen screen;
//    private Resources resources;
//    private SnakePresenter snakePresenter;
//    private Thread inputPollThread;
//
//    private final Map<Character, SnakePresenter.EventAction> characterEventActionMap
//        = new HashMap<>();
//    private final Map<KeyType, SnakePresenter.EventAction> keyTypeEventActionMap
//        = new HashMap<>();
//
//    /**
//     * Creates a new {@link ConsoleView} instance.
//     *
//     * @param properties properties of the game field
//     * @throws IOException if an I/O error occurs
//     */
//    public ConsoleView(GameProperties properties) throws IOException {
//        this.properties = properties;
//
//        this.terminal = new DefaultTerminalFactory()
//            .setInitialTerminalSize(new TerminalSize(properties.width(), properties.height() + 2))
//            .setPreferTerminalEmulator(true)
//            .createTerminal();
//        this.terminal.setCursorVisible(false);
//        this.screen = new TerminalScreen(terminal);
//
//        initializeResources();
//        initializeEventHandler();
//    }
//
//    private void initializeResources() {
//        resources = new Resources();
//        resources.apple =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('$')
//                .withForegroundColor(TextColor.ANSI.GREEN);
//        resources.barrier =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('*')
//                .withForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
//        resources.playerSnakeHead =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('@')
//                .withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
//        resources.playerSnakeTail =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('0')
//                .withForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
//        resources.enemySnakeHead =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('@')
//                .withForegroundColor(TextColor.ANSI.RED_BRIGHT);
//        resources.enemySnakeTail =
//            TextCharacter.DEFAULT_CHARACTER
//                .withCharacter('0')
//                .withForegroundColor(TextColor.ANSI.RED_BRIGHT);
//        resources.messageChar =
//            TextCharacter.DEFAULT_CHARACTER
//                .withForegroundColor(TextColor.ANSI.BLACK)
//                .withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);
//        resources.scoreChar =
//            TextCharacter.DEFAULT_CHARACTER
//                .withForegroundColor(TextColor.ANSI.BLACK)
//                .withBackgroundColor(TextColor.ANSI.CYAN_BRIGHT);
//    }
//
//    private void initializeEventHandler() {
//        characterEventActionMap.put('r', SnakePresenter.EventAction.RESTART);
//        characterEventActionMap.put('к', SnakePresenter.EventAction.RESTART);
//        characterEventActionMap.put('q', SnakePresenter.EventAction.EXIT);
//        characterEventActionMap.put('й', SnakePresenter.EventAction.EXIT);
//
//        characterEventActionMap.put('w', SnakePresenter.EventAction.UP);
//        characterEventActionMap.put('ц', SnakePresenter.EventAction.UP);
//        characterEventActionMap.put('s', SnakePresenter.EventAction.DOWN);
//        characterEventActionMap.put('ы', SnakePresenter.EventAction.DOWN);
//        characterEventActionMap.put('a', SnakePresenter.EventAction.LEFT);
//        characterEventActionMap.put('ф', SnakePresenter.EventAction.LEFT);
//        characterEventActionMap.put('d', SnakePresenter.EventAction.RIGHT);
//        characterEventActionMap.put('в', SnakePresenter.EventAction.RIGHT);
//
//        keyTypeEventActionMap.put(KeyType.ArrowDown, SnakePresenter.EventAction.DOWN);
//        keyTypeEventActionMap.put(KeyType.ArrowUp, SnakePresenter.EventAction.UP);
//        keyTypeEventActionMap.put(KeyType.ArrowLeft, SnakePresenter.EventAction.LEFT);
//        keyTypeEventActionMap.put(KeyType.ArrowRight, SnakePresenter.EventAction.RIGHT);
//    }
//
//
//    private void keyEventHandler(KeyStroke keyStroke) {
//        if (keyStroke.getKeyType().equals(KeyType.Character)) {
//            var keyCharacterEvent = characterEventActionMap.get(keyStroke.getCharacter());
//            if (keyCharacterEvent != null) {
//                keyCharacterEvent.execute(snakePresenter);
//            }
//        } else {
//            var keyTypeEvent = keyTypeEventActionMap.get(keyStroke.getKeyType());
//            if (keyTypeEvent != null) {
//                keyTypeEvent.execute(snakePresenter);
//            }
//        }
//    }
//
//    private void pollKeyboardInput() {
//        KeyStroke keyStroke;
//
//        while (!Thread.currentThread().isInterrupted()) {
//            try {
//                keyStroke = terminal.readInput();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            keyEventHandler(keyStroke);
//        }
//    }
//
//    /**
//     * Sets the {@link SnakePresenter} for this {@link View}.
//     *
//     * @param snakePresenter the {@link SnakePresenter} to set
//     */
//    public void setPresenter(SnakePresenter snakePresenter) {
//        this.snakePresenter = Objects.requireNonNull(snakePresenter);
//    }
//
//    /**
//     * Starts the view.
//     */
//    public void start() {
//        try {
//            screen.startScreen();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        inputPollThread = new Thread(this::pollKeyboardInput);
//        inputPollThread.start();
//    }
//
//    /**
//     * Sets the current amount of points the player
//     * has on an attached scoreboard.
//     * One is located two rows above the game field.
//     *
//     * @param scoreAmount the amount of points
//     */
//    @Override
//    public void setScoreAmount(int scoreAmount) {
//        String scoreString = "Score: " + scoreAmount;
//
//        for (int i = 0; i < scoreString.length(); i++) {
//            screen.setCharacter(i, properties.height(), resources.scoreChar.withCharacter(scoreString.charAt(i)));
//        }
//    }
//
//    /**
//     * Draws an apple on the game field.
//     * One is drawn as a green-colored {@code $} symbol.
//     *
//     * @param apple the apple to draw
//     */
//    @Override
//    public void drawApple(Apple apple) {
//        screen.setCharacter(apple.point().x(), apple.point().y(), resources.apple);
//    }
//
//    /**
//     * Draws a barrier on the game field.
//     * Each point is drawn as a yellow-colored {@code *} symbol.
//     *
//     * @param barrier the barrier to draw
//     */
//    @Override
//    public void drawBarrier(Barrier barrier) {
//        barrier.barrierPoints().forEach(point -> screen.setCharacter(point.x(), point.y(), resources.barrier));
//    }
//
//    private void drawSnake(Snake snake, TextCharacter head, TextCharacter tail) {
//        snake.getSnakeBody().getTail().forEach(point -> screen.setCharacter(point.x(), point.y(), tail));
//        screen.setCharacter(snake.getSnakeBody().getHead().x(), snake.getSnakeBody().getHead().y(), head);
//    }
//
//    /**
//     * Draws the player snake on the game field.
//     * The head is drawn as a white-colored {@code @} symbol.
//     * The tail is drawn as a white-colored {@code 0} symbol.
//     *
//     * @param snake the snake to draw
//     */
//    @Override
//    public void drawPlayerSnake(Snake snake) {
//        drawSnake(snake, resources.playerSnakeHead, resources.playerSnakeTail);
//    }
//
//    /**
//     * Draws the enemy snake on the game field.
//     * The head is drawn as a red-colored {@code @} symbol.
//     * The tail is drawn as a red-colored {@code 0} symbol.
//     *
//     * @param snake the snake to draw
//     */
//    @Override
//    public void drawEnemySnake(Snake snake) {
//        drawSnake(snake, resources.enemySnakeHead, resources.enemySnakeTail);
//    }
//
//    /**
//     * Shows a message on the game field.
//     * The message is centered on the screen.
//     *
//     * @param string the message to show
//     */
//    @Override
//    public void showMessage(String string) {
//        for (int i = 0; i < string.length(); i++) {
//            screen.setCharacter(5 + i, properties.height() / 2,
//                resources.messageChar.withCharacter(string.charAt(i)));
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void clear() {
//        screen.clear();
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void refresh() {
//        try {
//            screen.refresh();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void close() {
//        try {
//            terminal.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        inputPollThread.interrupt();
//    }
//
//    private class Resources {
//        TextCharacter apple;
//        TextCharacter barrier;
//        TextCharacter playerSnakeHead;
//        TextCharacter playerSnakeTail;
//        TextCharacter enemySnakeHead;
//        TextCharacter enemySnakeTail;
//
//        TextCharacter messageChar;
//        TextCharacter scoreChar;
//    }
//}
