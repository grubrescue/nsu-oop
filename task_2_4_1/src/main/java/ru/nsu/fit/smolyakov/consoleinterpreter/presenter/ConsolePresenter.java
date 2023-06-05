package ru.nsu.fit.smolyakov.consoleinterpreter.presenter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;

/**
 * Presenter for the console.
 */
public class ConsolePresenter {
    private final ConsoleProcessor consoleProcessor;

    /**
     * Creates a new console presenter with given processor.
     *
     * @param consoleProcessor console processor
     */
    public ConsolePresenter(@NonNull ConsoleProcessor consoleProcessor) {
        this.consoleProcessor = consoleProcessor;
    }

    /**
     * Returns a string to be printed before user input.
     *
     * @return string to be printed before user input
     */
    public String promptString() {
        StringBuilder stringBuilder = new StringBuilder();
        consoleProcessor.getProviderStack().forEach(
            provider -> {
                stringBuilder.append(" > ");
                stringBuilder.append(provider.getRepresentation());
            }
        );

        stringBuilder.append(" $ ");
        return stringBuilder.toString();
    }

    /**
     * Returns a formatted error string.
     *
     * @return formatted error string
     */
    public String errorString(String message) {
        return "!!! Error: " + message;
    }
}
