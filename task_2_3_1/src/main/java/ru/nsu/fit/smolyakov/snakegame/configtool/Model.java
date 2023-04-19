package ru.nsu.fit.smolyakov.snakegame.configtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.Application;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.io.File;
import java.io.IOException;

public class Model {
    private static ObjectMapper mapper =
        new ObjectMapper(new YAMLFactory());

    private File gamePropertiesFile;
    private GameProperties gameProperties;

    public Model() throws IOException {
        gamePropertiesFile = new File(Application.GAME_PROPERTIES_YAML_PATH);
        gameProperties
            = mapper.readValue(gamePropertiesFile, GameProperties.class);

    }

    public GameProperties getProperties() {
        return gameProperties;
    }

    public void setProperties(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    public void sync() throws IOException {
        mapper.writeValue(gamePropertiesFile, gameProperties);
    }
}
