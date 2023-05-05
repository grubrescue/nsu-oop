package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * The pattern of the file name so one could be parsable by {@link #parseFieldSize(String)}.
     */
    public static final String pattern = "(?<width>\\d+)x(?<height>\\d+)_(\\w+).txt";

    @JsonCreator
    public CustomFileLevel(@JsonProperty("fileName") String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public record FieldSize(int width, int height) {
    }

    public Optional<FieldSize> parse() {
        return parseFieldSize(fileName);
    }

    public static Optional<FieldSize> parseFieldSize(String fileName) {
        Matcher matcher = Pattern.compile(pattern).matcher(Objects.requireNonNull(fileName));

        if (matcher.matches()) {
            return Optional.of(
                new FieldSize(
                    Integer.parseInt(matcher.group("width")),
                    Integer.parseInt(matcher.group("height"))
                )
            );
        } else {
            return Optional.empty();
        }
    }
}
