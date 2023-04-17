package ru.nsu.fit.smolyakov.snake.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;
import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snake.properties.JavaFxProperties;
import ru.nsu.fit.smolyakov.snake.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snake.view.ConsoleView;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import java.io.IOException;
import java.util.Objects;

public class ConsoleSnakeGame {
    private ConsoleView view;
    private GameField model;
    private Presenter presenter;

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
