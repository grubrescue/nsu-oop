package ru.nsu.fit.smolyakov.snakegame.configtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Model {
    private static ObjectMapper mapper =
        new ObjectMapper(new YAMLFactory());

    private File presenterPropertiesFile;
    private File gameFieldPropertiesFile;

    private GameFieldProperties gameFieldProperties;
    private PresenterProperties presenterProperties;

    public Model() throws IOException {
        presenterPropertiesFile = new File("config/presenterProperties.yaml");
        gameFieldPropertiesFile = new File("config/gameFieldProperties.yaml");

        gameFieldProperties
            = mapper.readValue(gameFieldPropertiesFile, GameFieldProperties.class);

        presenterProperties
            = mapper.readValue(presenterPropertiesFile, PresenterProperties.class);
    }

    public GameFieldProperties getGameFieldProperties() {
        return gameFieldProperties;
    }

    public PresenterProperties getPresenterProperties() {
        return presenterProperties;
    }

    public void setGameFieldProperties(GameFieldProperties gameFieldProperties) {
        this.gameFieldProperties = gameFieldProperties;
    }

    public void setPresenterProperties(PresenterProperties presenterProperties) {
        this.presenterProperties = presenterProperties;
    }

    public void sync() throws IOException {
        mapper.writeValue(presenterPropertiesFile, presenterProperties);
        mapper.writeValue(gameFieldPropertiesFile, gameFieldProperties);
    }
}
