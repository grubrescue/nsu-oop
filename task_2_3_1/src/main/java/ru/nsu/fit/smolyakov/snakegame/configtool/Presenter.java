package ru.nsu.fit.smolyakov.snakegame.configtool;

import java.io.IOException;

public class Presenter {
    private Model model;
    private View view;

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;

        view.setPresenter(this);
        view.setGameSpeed(model.getProperties().speed());
    }

    public void saveConfig() {
        model.setProperties(
            model.getProperties()
                .withSpeed(view.getGameSpeed())
        );

        try {
            model.sync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
