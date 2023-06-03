package ru.nsu.fit.smolyakov.consoleinterpreter.presenter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.Processor;

public class Presenter {
    private final Processor processor;

    public Presenter(@NonNull Processor processor) {
        this.processor = processor;
    }

    public String promptString() {
        StringBuilder stringBuilder = new StringBuilder();
        processor.getProviderStack().forEach(
            provider -> {
                stringBuilder.append(" > ");
                stringBuilder.append(provider.getRepresentation());
                if (!provider.getArgsContext().isEmpty()) {
                    stringBuilder.append(provider.getArgsContext());
                }
            }
        );

        stringBuilder.append("  $ ");
        return stringBuilder.toString();
    }

    public String errorString(String message) {
        return "!!! Error: " + message;
    }
}
