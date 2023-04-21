package ru.nsu.fit.smolyakov.snakegame.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.view.ConsoleView;

import java.io.File;
import java.io.IOException;

/**
 * The class that executes the console variant of the snake game.
 * It creates the model, view and snakePresenter and connects them.
 */
public class ConsoleSnakeGame {
    private ConsoleView view;
    private GameModel model;
    private SnakePresenter snakePresenter;

    /**
     * Executes the game.
     *
     * @throws IOException if there is a problem with reading the properties files
     */
    public void execute() throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());

        var properties = mapper.readValue(
            new File(GameData.INSTANCE.GAME_PROPERTIES_YAML_PATH),
            GameProperties.class
        );


        this.model = new GameModelImpl(properties);
        this.view = new ConsoleView(properties);
        this.snakePresenter = new SnakePresenter(view, model, properties);

        this.view.setPresenter(snakePresenter);

        this.snakePresenter.start();
        this.view.start();
    }
}
