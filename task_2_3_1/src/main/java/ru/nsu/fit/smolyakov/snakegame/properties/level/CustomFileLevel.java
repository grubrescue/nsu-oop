package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a barrier from the specified text file.
 * One should use '*' to represent a barrier point, and '.' to represent an empty point.
 * The amount of rows and columns have to correspond with the game field size,
 * however, this is not a requirement:
 * if the file has fewer rows or columns than the game field,
 * the barrier will be created only on the specified rows and columns;
 * if the file has more rows or columns,
 * the barrier will be created only on the specified rows and columns,
 * and the rest of the file will be ignored.
 *
 * <p>If the file does not exist, the barrier is supposed to be empty.
 *
 * <p>This class is used as a DTO for the {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier} class,
 * so the game level is actually created by
 * {@link ru.nsu.fit.smolyakov.snakegame.model.Barrier#fromProperties(GameProperties)} method.
 */
public final class CustomFileLevel extends Level {
    /**
     * The pattern of the file name so one could be parsable by {@link #parseFilenameFieldSize(String)}.
     */
    public static final String pattern = "(?<width>\\d+)x(?<height>\\d+)_(\\w+).txt";
    private final String fileName;

    /**
     * Initializes a new instance of the {@link CustomFileLevel} class.
     *
     * @param fileName a name of the file with the barrier
     */
    @JsonCreator
    public CustomFileLevel(@JsonProperty("fileName") String fileName) {
        this.fileName = Objects.requireNonNull(fileName);
    }

    /**
     * Parses the specified filename and returns a {@link FieldSize} instance.
     *
     * @param fileName a name of the file with the barrier
     * @return a {@link FieldSize} instance
     */
    public static Optional<FieldSize> parseFilenameFieldSize(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        }

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

    /**
     * Returns a name of the file with the barrier.
     * One is relative to the {@link ru.nsu.fit.smolyakov.snakegame.GameData#LEVEL_FOLDER_PATH}.
     *
     * @return a name of the file with the barrier
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Parses the filename associated with current instance
     * and returns a {@link FieldSize} instance.
     *
     * @return a {@link FieldSize} instance
     */
    public Optional<FieldSize> parseFieldSize() {
        return parseFilenameFieldSize(fileName);
    }

    /**
     * A DTO for the field size.
     *
     * @param width  a width of the field
     * @param height a height of the field
     */
    public record FieldSize(int width, int height) {
    }
}
