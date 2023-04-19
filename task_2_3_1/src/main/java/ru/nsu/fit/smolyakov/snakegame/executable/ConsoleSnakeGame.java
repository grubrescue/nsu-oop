package ru.nsu.fit.smolyakov.snakegame.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snakegame.view.ConsoleView;

import java.io.IOException;
import java.util.Objects;

/**
 * The class that executes the console variant of the snake game.
 * It creates the model, view and snakePresenter and connects them.
 */
public class ConsoleSnakeGame {
    private ConsoleView view;
    private GameField model;
    private SnakePresenter snakePresenter;

    /**
     * Executes the game.
     *
     * @throws IOException if there is a problem with reading the properties files
     */
    public void execute() throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());

        var gameFieldProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/config/game_field_properties.yaml")),
            GameFieldProperties.class
        );

        var presenterProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/config/presenter_properties.yaml")),
            PresenterProperties.class
        );

        this.model = new GameFieldImpl(gameFieldProperties);
        this.view = new ConsoleView(gameFieldProperties);
        this.snakePresenter = new SnakePresenter(view, model, presenterProperties);

        this.view.setPresenter(snakePresenter);

        this.snakePresenter.start();
        this.view.start();
    }
}
