package ru.nsu.fit.smolyakov.consoleinterpreter.presenter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;

public class ConsolePresenter {
    private final ConsoleProcessor consoleProcessor;

    public ConsolePresenter(@NonNull ConsoleProcessor consoleProcessor) {
        this.consoleProcessor = consoleProcessor;
    }

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

    public String errorString(String message) {
        return "!!! Error: " + message;
    }
}
