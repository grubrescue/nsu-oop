package ru.nsu.fit.smolyakov.snakegame.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.snakegame.Application;

import java.util.List;
import java.util.Objects;

/**
 * Represents properties of presenter.
 *
 * @param width a width of the game field
 * @param height a height of the game field
 * @param javaFxScaling a resolution scaling of JavaFX. This value determines, how many pixels
 *                      every cell will take both in width and height. For example, if the value is 8, then every cell
 *                      will take 8x8 pixels
 * @param apples a number of apples
 * @param barrierFileName a name of file located in {@link Application#LEVEL_FOLDER_PATH} that contains
 *                        a barrier
 * @param aiClassNamesList a list of AI class names
 * @param speed a game speed
 */
public record GameProperties(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("javaFxScaling") int javaFxScaling,
    @JsonProperty("apples") int apples,
    @JsonProperty("barrierTxtFile") String barrierFileName,
    @JsonProperty("aiClassNames") List<String> aiClassNamesList,
    @JsonProperty("speed") GameSpeed speed
) {
    /**
     * Returns new {@code GameProperties} instance with the start timeout
     * changed to a specified one. This object is not modified.
     *
     * @param width a new width of the game field
     * @return a new {@code GameProperties}
     *         with the specified width
     * @throws IllegalArgumentException if the specified width is not positive
     */
    public GameProperties withWidth(int width) {
        if (width < 1) {
            throw new IllegalArgumentException("Width must be positive");
        }

        return new GameProperties(
            width,
            height(),
            javaFxScaling(),
            apples(),
            barrierFileName(),
            aiClassNamesList(),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the start timeout
     * changed to a specified one. This object is not modified.
     *
     * @param height a new height of the game field
     * @return a new {@code GameProperties}
     *         with the specified height
     * @throws IllegalArgumentException if the specified height is not positive
     */
    public GameProperties withHeight(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("Height must be positive");
        }

        return new GameProperties(
            width(),
            height,
            javaFxScaling(),
            apples(),
            barrierFileName(),
            aiClassNamesList(),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the resolution scaling
     * of JavaFX implementation changed to a specified one. This object is not modified.
     *
     * @param javaFxScaling a new resolution scaling of JavaFX
     * @return a new {@code GameProperties}
     *         with the specified resolution scaling of JavaFX
     * @throws IllegalArgumentException if the specified resolution scaling is not positive
     */
    public GameProperties withJavaFxScaling(int javaFxScaling) {
        if (javaFxScaling < 1) {
            throw new IllegalArgumentException("JavaFX scaling must be positive");
        }

        return new GameProperties(
            width(),
            height(),
            javaFxScaling,
            apples(),
            barrierFileName(),
            aiClassNamesList(),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the number of apples
     * changed to a specified one. This object is not modified.
     *
     * @param apples a new number of apples
     * @return a new {@code GameProperties}
     *         with the specified number of apples
     * @throws IllegalArgumentException if the specified number of apples is negative
     */
    public GameProperties withApples(int apples) {
        if (apples < 0) {
            throw new IllegalArgumentException("Number of apples must be non-negative");
        } else if (apples >= width() * height()) {
            throw new IllegalArgumentException("Number of apples must be less than the number of cells");
        }

        return new GameProperties(
            width(),
            height(),
            javaFxScaling(),
            apples,
            barrierFileName(),
            aiClassNamesList(),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the name of file
     * with barriers changed to a specified one. This object is not modified.
     *
     * @param barrierFilePath a new name of file with barriers
     * @return a new {@code GameProperties}
     *         with the specified path to the file with barriers
     */
    public GameProperties withBarrierFilePath(String barrierFilePath) {
        return new GameProperties(
            width(),
            height(),
            javaFxScaling(),
            apples(),
            barrierFilePath,
            aiClassNamesList(),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the list of AI class names
     * changed to a specified one. This object is not modified.
     *
     * @param aiClassNamesList a new list of AI class names
     * @return a new {@code GameProperties}
     *         with the specified list of AI class names
     * @throws NullPointerException if the specified list is null
     */
    public GameProperties withAiClassNamesList(List<String> aiClassNamesList) {
        return new GameProperties(
            width(),
            height(),
            javaFxScaling(),
            apples(),
            barrierFileName(),
            Objects.requireNonNull(aiClassNamesList),
            speed()
        );
    }

    /**
     * Returns new {@code GameProperties} instance with the game speed
     * changed to a specified one. This object is not modified.
     *
     * @param speed a new game speed
     * @return a new {@code GameProperties}
     *         with the specified game speed
     */
    public GameProperties withSpeed(GameSpeed speed) {
        return new GameProperties(
            width(),
            height(),
            javaFxScaling(),
            apples(),
            barrierFileName(),
            aiClassNamesList(),
            speed
        );
    }
}
