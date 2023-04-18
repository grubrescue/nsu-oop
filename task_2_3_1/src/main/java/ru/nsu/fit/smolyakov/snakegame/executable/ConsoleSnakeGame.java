package ru.nsu.fit.smolyakov.snakegame.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.Presenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snakegame.view.ConsoleView;

import java.io.IOException;
import java.util.Objects;

/**
 * The class that executes the console variant of the snake game.
 * It creates the model, view and presenter and connects them.
 */
public class ConsoleSnakeGame {
    private ConsoleView view;
    private GameField model;
    private Presenter presenter;

    /**
     * Executes the game.
     *
     * @throws IOException if there is a problem with reading the properties files
     */
    public void execute() throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());

        var gameFieldProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/game_field_properties.yaml")),
            GameFieldProperties.class
        );

        var presenterProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/presenter_properties.yaml")),
            PresenterProperties.class
        );

        this.model = new GameFieldImpl(gameFieldProperties);
        this.view = new ConsoleView(gameFieldProperties);
        this.presenter = new Presenter(view, model, presenterProperties);

        this.view.setPresenter(presenter);

        this.presenter.start();
        this.view.start();
    }
}
