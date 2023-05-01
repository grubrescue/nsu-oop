package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.executable.JavaFxSnakeGame;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The presenter of the configuration tool.
 */
public class Presenter {
    /**
     * The minimum width of the game field.
     */
    public static final int MIN_WIDTH = 3;

    /**
     * The minimum height of the game field.
     */
    public static final int MIN_HEIGHT = 3;

    /**
     * The maximum width of the game field.
     */
    public static final int MAX_WIDTH = 1000;

    /**
     * The maximum height of the game field.
     */
    public static final int MAX_HEIGHT = 1000;

    @FXML
    private Scene scene;

    @FXML
    private ListView<String> aiListView;

    @FXML
    private ChoiceBox<GameSpeed> speedChoiceBox;

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Spinner<Integer> applesSpinner;

    @FXML
    private Slider javaFxScalingSlider;
    @FXML
    private Text resolutionText;
    @FXML
    private ChoiceBox<String> levelChoiceBox;

    private Model model;

    /**
     * {@link ChangeListener} that is called when the maximum number of apples is changed.
     * Usually, that happens when the width or height of the game field is changed.
     */
    public final ChangeListener<Number> onMaxApplesLimitChangeListener = (observable, oldValue, newValue) -> {
        var newMax = getMaxApplesAvailable();
        var prevVal = getApplesAmount();

        setApplesAvailableRange(newMax);
        setApplesAmount(Math.min(prevVal, newMax));
    };

    /**
     * {@link ChangeListener} that is called when the scaling value is changed.
     */
    public final ChangeListener<Number> onScalingChangedListener = (observable, oldValue, newValue) -> {
        updateCalculatedResolution();
    };

    /**
     * {@link ChangeListener} that is called when the width or height of the game field is changed.
     */
    public final ChangeListener<Number> onFieldSizeChangeListener = (observable, oldValue, newValue) -> {
        onMaxApplesLimitChangeListener.changed(observable, oldValue, newValue);
        onScalingChangedListener.changed(observable, oldValue, newValue);
    };

    public void init() {
        initWidthSelector(model.getProperties().width());
        initHeightSelector(model.getProperties().height());
        initApplesAmountSelector(model.getProperties().apples(), getMaxApplesAvailable());

        setAvailableGameSpeedChoices(Arrays.asList(GameSpeed.values()));
        setGameSpeed(model.getProperties().speed());

        initAiNames();
        setAvailableAiNames(GameData.INSTANCE.getAvailableAiNames());
        setSelectedAiNames(model.getProperties().aiClassNamesList());

        setAvailableLevelNames(GameData.INSTANCE.levelFileNames());
        setSelectedLevel(model.getProperties().levelFileName());

        setSelectedJavaFxScalingValue(model.getProperties().javaFxScaling());
        updateCalculatedResolution();
    }

    /**
     * Sets the {@link Model}'s properties to the values from the view
     * and saves them to the file.
     */
    public void saveConfig() {
        model.setProperties(
            model.getProperties()
                .withSpeed(getGameSpeed())
                .withApples(getApplesAmount())
                .withWidth(getWidth())
                .withHeight(getHeight())
                .withJavaFxScaling(getJavaFxScalingValue())
                .withBarrierFilePath(getLevel())
                .withAiClassNamesList(getSelectedAiNames())
        );

        try {
            model.sync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs the game with the saved configuration.
     * Usually, if this method is called, it is called after {@link #saveConfig()}.
     */
    public void runJavaFxSnake() {
        var app = new JavaFxSnakeGame();
        var stage = new Stage();
        hide();

        try {
            app.runGame(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the maximum number of apples that can be placed on the game field.
     *
     * @return the maximum number of apples that can be placed on the game field
     */
    public int getMaxApplesAvailable() {
        return getWidth() * getHeight() - 4;
    }

    /**
     * Updates the calculated resolution based on
     * the current scaling factor and width and
     * height of the game field.
     */
    @FXML
    public void updateCalculatedResolution() {
        var scaling = getJavaFxScalingValue();
        setResolutionText(
            scaling * getWidth(),
            scaling * getHeight()
        );
    }

    /**
     * Saves the configuration and runs the game.
     */
    @FXML
    public void saveAndRunGame() {
        saveConfig();
        runJavaFxSnake();
    }

    /**
     * Initializes the list of available AI names.
     */
    public void initAiNames() {
        aiListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        javaFxScalingSlider.valueProperty().addListener(onScalingChangedListener);
    }

    /**
     * Initializes the width selector.
     *
     * @param width the initial width
     */
    public void initWidthSelector(int width) {
        SpinnerValueFactory<Integer> widthSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH);
        widthSpinner.setEditable(true);
        widthSpinnerValue.setValue(width);
        widthSpinner.setValueFactory(widthSpinnerValue);
        widthSpinner.valueProperty().addListener(onFieldSizeChangeListener);
    }

    /**
     * Initializes the height selector.
     *
     * @param height the initial height
     */
    public void initHeightSelector(int height) {
        SpinnerValueFactory<Integer> heightSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT);
        heightSpinner.setEditable(true);
        heightSpinnerValue.setValue(height);
        heightSpinner.setValueFactory(heightSpinnerValue);
        heightSpinner.valueProperty().addListener(onFieldSizeChangeListener);
    }

    /**
     * Initializes the apples amount selector.
     *
     * @param initValue the initial value
     * @param maxApples the maximum amount of apples
     */
    public void initApplesAmountSelector(int initValue, int maxApples) {
        SpinnerValueFactory<Integer> applesSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxApples - 2);
        applesSpinner.setEditable(true);
        applesSpinnerValue.setValue(initValue);
        applesSpinner.setValueFactory(applesSpinnerValue);
    }

    /**
     * Sets the list of available AI names the user able to choose.
     *
     * @param aiNames the list of available AI names
     */
    public void setAvailableAiNames(List<String> aiNames) {
        aiListView.getItems().clear();
        aiListView.getItems().addAll(aiNames);
    }

    /**
     * Sets the list of names of available levels the user able to choose.
     *
     * @param levelNames the list of names of available levels
     */
    public void setAvailableLevelNames(List<String> levelNames) {
        levelChoiceBox.getItems().clear();
        levelChoiceBox.getItems().addAll(levelNames);
    }

    /**
     * Sets the list of available game speeds the user able to choose.
     *
     * @param gameSpeeds the list of available game speeds
     */
    public void setAvailableGameSpeedChoices(List<GameSpeed> gameSpeeds) {
        speedChoiceBox.getItems().clear();
        speedChoiceBox.getItems().addAll(gameSpeeds);
    }

    /**
     * Returns the selected game speed.
     *
     * @return the selected game speed
     */
    public GameSpeed getGameSpeed() {
        return speedChoiceBox.getValue();
    }

    /**
     * Sets the selected game speed.
     *
     * @param gameSpeed the selected game speed
     */
    public void setGameSpeed(GameSpeed gameSpeed) {
        speedChoiceBox.setValue(gameSpeed);
    }

    /**
     * Sets the JavaFx implementation scaling factor.
     * One is described in the {@link java.util.Properties} class.
     *
     * @param javaFxScaling the JavaFx implementation scaling factor
     */
    public void setSelectedJavaFxScalingValue(int javaFxScaling) {
        javaFxScalingSlider.setValue(javaFxScaling);
    }

    /**
     * Sets the text of the resolution label.
     *
     * @param resX the X-dimensional resolution
     * @param resY the Y-dimensional resolution
     */
    public void setResolutionText(int resX, int resY) {
        String format = "Resolution will be %d x %d px.";
        resolutionText.setText(format.formatted(resX, resY));
    }

    /**
     * Returns the amount of apples.
     *
     * @return the amount of apples
     */
    public int getApplesAmount() {
        return applesSpinner.getValue();
    }

    /**
     * Sets the amount of apples.
     *
     * @param apples the amount of apples
     */
    public void setApplesAmount(int apples) {
        applesSpinner.getValueFactory().setValue(apples);
    }

    /**
     * Returns the width of the game field.
     *
     * @return the width of the game field
     */
    public int getWidth() {
        return widthSpinner.getValue();
    }

    /**
     * Sets the width of the game field.
     *
     * @param width the width of the game field
     */
    public void setWidth(int width) {
        widthSpinner.getValueFactory().setValue(width);
    }

    /**
     * Returns the height of the game field.
     *
     * @return the height of the game field
     */
    public int getHeight() {
        return heightSpinner.getValue();
    }

    /**
     * Sets the height of the game field.
     *
     * @param height the height of the game field
     */
    public void setHeight(int height) {
        heightSpinner.getValueFactory().setValue(height);
    }

    /**
     * Returns the JavaFx implementation scaling factor.
     *
     * @return the JavaFx implementation scaling factor
     */
    public int getJavaFxScalingValue() {
        return (int) javaFxScalingSlider.getValue();
    }

    /**
     * Returns the selected level.
     *
     * @return the selected level
     */
    public String getLevel() {
        return levelChoiceBox.getValue();
    }

    /**
     * Sets the selected level.
     *
     * @param level the selected level
     */
    public void setSelectedLevel(String level) {
        levelChoiceBox.setValue(level);
    }

    /**
     * Returns the list of selected AI names.
     *
     * @return the list of selected AI names
     */
    public List<String> getSelectedAiNames() {
        return aiListView.getSelectionModel().getSelectedItems();
    }

    /**
     * Sets the list of selected AI names.
     *
     * @param aiNames the list of selected AI names
     */
    public void setSelectedAiNames(List<String> aiNames) {
        aiListView.getSelectionModel().clearSelection();
        aiNames.forEach(aiName -> {
            var index = aiListView.getItems().indexOf(aiName);
            aiListView.getSelectionModel().select(aiName);
        });
    }

    /**
     * Sets the range of available apples amount.
     *
     * @param upon the upper bound of the range (lower is always 0)
     */
    public void setApplesAvailableRange(int upon) {
        var svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, upon);
        applesSpinner.setValueFactory(svf);
    }

    /**
     * Hides the window.
     */
    public void hide() {
        scene.getWindow().hide();
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
