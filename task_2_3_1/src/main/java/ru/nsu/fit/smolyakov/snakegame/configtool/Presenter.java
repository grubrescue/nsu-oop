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
import ru.nsu.fit.smolyakov.snakegame.properties.level.*;

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
    private Slider randomLevelDensitySlider;

    @FXML
    private Button matchFieldButton;

    @FXML
    private Slider javaFxScalingSlider;

    @FXML
    private Text resolutionText;

    @FXML
    private RadioButton borderLevelRadioButton;

    @FXML
    private RadioButton randomLevelRadioButton;

    @FXML
    private RadioButton emptyLevelRadioButton;

    @FXML
    private RadioButton customLevelRadioButton;

    @FXML
    private ChoiceBox<String> customFileLevelChoiceBox;
    private Model model;

    private ToggleGroup levelToggleGroup;

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

    @FXML
    private void matchFieldSize() {
        var fieldSize = CustomFileLevel.parseFilenameFieldSize(getLevelFileName());
        fieldSize.ifPresent(size -> {
            setWidth(size.width());
            setHeight(size.height());
        });
    }

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

    /**
     * {@link ChangeListener} that is called when the selected custom level is changed.
     */
    public final ChangeListener<String> onLevelChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
        if (newValue == null) {
            return;
        }
        matchFieldButton.setDisable(CustomFileLevel.parseFilenameFieldSize(newValue).isEmpty());
    };

    /**
     * {@link ChangeListener} that is called when the selected level type is changed.
     */
    public final ChangeListener<Toggle> onLevelRadioButtonsChangeListener = (observable, oldValue, newValue) -> {
        if (newValue.equals(customLevelRadioButton)) {
            customFileLevelChoiceBox.setDisable(false);
            randomLevelDensitySlider.setDisable(true);

            boolean canParseFieldSize;
            if (customFileLevelChoiceBox.getValue() != null) {
                canParseFieldSize = CustomFileLevel.parseFilenameFieldSize(customFileLevelChoiceBox.getValue()).isPresent();
            } else {
                canParseFieldSize = false;
            }
            matchFieldButton.setDisable(!canParseFieldSize);
        } else if (newValue.equals(randomLevelRadioButton)){
            customFileLevelChoiceBox.setDisable(true);
            randomLevelDensitySlider.setDisable(false);
            matchFieldButton.setDisable(true);
        } else {
            customFileLevelChoiceBox.setDisable(true);
            randomLevelDensitySlider.setDisable(true);
            matchFieldButton.setDisable(true);
        }
    };

    /**
     * Initializes the view components.
     */
    public void initializeViewFields() {
        initWidthSelector(model.getProperties().width());
        initHeightSelector(model.getProperties().height());
        initApplesAmountSelector(model.getProperties().apples(), getMaxApplesAvailable());

        setAvailableGameSpeedChoices(Arrays.asList(GameSpeed.values()));
        setGameSpeed(model.getProperties().speed());

        initAiNames();
        setAvailableAiNames(GameData.INSTANCE.getAvailableAiNames());
        setSelectedAiNames(model.getProperties().aiClassNamesList());

        initLevelRadioButtons();

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
                .withAiClassNamesList(getSelectedAiNames())
                .withLevel(instanceLevel())
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

    private void initLevelRadioButtons() {
        levelToggleGroup = new ToggleGroup();

        borderLevelRadioButton.setToggleGroup(levelToggleGroup);
        randomLevelRadioButton.setToggleGroup(levelToggleGroup);
        emptyLevelRadioButton.setToggleGroup(levelToggleGroup);
        customLevelRadioButton.setToggleGroup(levelToggleGroup);

        levelToggleGroup.selectedToggleProperty().addListener(onLevelRadioButtonsChangeListener);

        customFileLevelChoiceBox.setDisable(true);
        customFileLevelChoiceBox.valueProperty().addListener(onLevelChoiceBoxChangeListener);

        setAvailableLevelNames(GameData.INSTANCE.levelFileNames());
        if (model.getProperties().level() instanceof CustomFileLevel lvl) {
            setSelectedLevel(lvl.getFileName());
            levelToggleGroup.selectToggle(customLevelRadioButton);
            customFileLevelChoiceBox.setDisable(false);
        } else if (model.getProperties().level() instanceof RandomLevel lvl) {
            levelToggleGroup.selectToggle(randomLevelRadioButton);
            randomLevelDensitySlider.setValue(lvl.getDensity() * 100);
        } else if (model.getProperties().level() instanceof EmptyLevel) {
            levelToggleGroup.selectToggle(emptyLevelRadioButton);
        } else {
            levelToggleGroup.selectToggle(borderLevelRadioButton);
        }
    }

    /**
     * Initializes the list of available AI names.
     */
    private void initAiNames() {
        aiListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        javaFxScalingSlider.valueProperty().addListener(onScalingChangedListener);
    }

    /**
     * Initializes the width selector.
     *
     * @param width the initial width
     */
    private void initWidthSelector(int width) {
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
    private void initHeightSelector(int height) {
        SpinnerValueFactory<Integer> heightSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT);
        heightSpinner.setEditable(true);
        heightSpinnerValue.setValue(height);
        heightSpinner.setValueFactory(heightSpinnerValue);
        heightSpinner.valueProperty().addListener(onFieldSizeChangeListener);
    }

    private void initApplyButton() {
        if (levelToggleGroup.getSelectedToggle().equals(customLevelRadioButton)) {
            matchFieldButton.setDisable(CustomFileLevel.parseFilenameFieldSize(customFileLevelChoiceBox.getValue()).isEmpty());
        } else {
            matchFieldButton.setDisable(false);
        }
    }

    /**
     * Initializes the apples amount selector.
     *
     * @param initValue the initial value
     * @param maxApples the maximum amount of apples
     */
    private void initApplesAmountSelector(int initValue, int maxApples) {
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
    private void setAvailableAiNames(List<String> aiNames) {
        aiListView.getItems().clear();
        aiListView.getItems().addAll(aiNames);
    }

    /**
     * Sets the list of names of available levels the user able to choose.
     *
     * @param levelNames the list of names of available levels
     */
    private void setAvailableLevelNames(List<String> levelNames) {
        customFileLevelChoiceBox.getItems().clear();
        customFileLevelChoiceBox.getItems().addAll(levelNames);
    }

    /**
     * Sets the list of available game speeds the user able to choose.
     *
     * @param gameSpeeds the list of available game speeds
     */
    private void setAvailableGameSpeedChoices(List<GameSpeed> gameSpeeds) {
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
     * Returns the selected level file name, if custom level is selected.
     *
     * @return the selected level file name or {@code null} if custom level is not selected
     */
    public String getLevelFileName() {
        if (!levelToggleGroup.getSelectedToggle().equals(customLevelRadioButton)) {
            return null;
        }
        return customFileLevelChoiceBox.getValue();
    }

    /**
     * Sets the selected level.
     *
     * @param level the selected level
     */
    public void setSelectedLevel(String level) {
        customFileLevelChoiceBox.setValue(level);
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
     * Returns the selected random level density.
     * @return the selected random level density
     */
    public double getRandomLevelDensity() {
        return randomLevelDensitySlider.getValue() / 100;
    }

    /**
     * Instantiates and returns the selected level.
     *
     * @return the selected level
     */
    public Level instanceLevel() {
        if (levelToggleGroup.getSelectedToggle().equals(customLevelRadioButton)) {
            return new CustomFileLevel(getLevelFileName());
        } else if (levelToggleGroup.getSelectedToggle().equals(randomLevelRadioButton)) {
            return new RandomLevel(getRandomLevelDensity());
        } else if (levelToggleGroup.getSelectedToggle().equals(emptyLevelRadioButton)){
            return new EmptyLevel();
        } else {
            return new BorderLevel();
        }
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

    /**
     * Sets the model.
     *
     * @param model the model
     */
    public void setModel(Model model) {
        this.model = model;
    }
}
