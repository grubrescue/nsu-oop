package ru.nsu.fit.smolyakov.snakegame.configtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.io.File;
import java.io.IOException;

/**
 * The model of the configuration tool.
 */
public class Model {
    private static final ObjectMapper mapper =
        new ObjectMapper(new YAMLFactory());

    private final File gamePropertiesFile;
    private GameProperties gameProperties;

    /**
     * Creates a new model.
     *
     * @throws IOException if an I/O error occurs
     */
    public Model() throws IOException {
        gamePropertiesFile = new File(GameData.INSTANCE.GAME_PROPERTIES_YAML_PATH);
        gameProperties
            = mapper.readValue(gamePropertiesFile, GameProperties.class);
    }

    /**
     * Returns the game properties associated with this model.
     *
     * @return the game properties
     */
    public GameProperties getProperties() {
        return gameProperties;
    }

    /**
     * Sets the game properties.
     *
     * @param gameProperties the game properties
     */
    public void setProperties(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    /**
     * Saves the game properties to the file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void sync() throws IOException {
        System.out.println(gameProperties);
        mapper.writeValue(gamePropertiesFile, gameProperties);
    }
}
