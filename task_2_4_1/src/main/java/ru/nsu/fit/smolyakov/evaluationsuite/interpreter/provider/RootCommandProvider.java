package ru.nsu.fit.smolyakov.evaluationsuite.interpreter.provider;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;

import java.util.Map;

public class RootCommandProvider extends AbstractCommandProvider {
    public RootCommandProvider(@NonNull String username) {
        super("User [" + username + "]");
    }

    public RootCommandProvider(@NonNull String username,
                               @NonNull Map<String, Command<String>> commandMap,
                               @NonNull Map<String, AbstractCommandProvider> subprocessorsMap) {
        super("User [" + username + "]", commandMap, subprocessorsMap);
    }
}
