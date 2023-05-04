package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Creates a barrier from the specified text file.
 * One should use '*' to represent a barrier point, and '.' to represent an empty point.
 * The amount of rows and columns have to correspond with the game field size,
 * however, this is not a requirement:
 * if the file has less rows or columns than the game field,
 * the barrier will be created only on the specified rows and columns;
 * if the file has more rows or columns,
 * the barrier will be created only on the specified rows and columns,
 * and the rest of the file will be ignored.
 *
 * <p>If the file does not exist, the barrier is supposed to be empty.
 */
public final class CustomFileLevel extends Level {
    private final String fileName;

    @JsonCreator
    public CustomFileLevel(@JsonProperty("fileName") String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
