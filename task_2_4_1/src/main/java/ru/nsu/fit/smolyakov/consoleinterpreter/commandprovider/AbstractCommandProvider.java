package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.NoArgsCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.CommandProviderAnnotationProcessor;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.TypeParametersUnsupportedByAnnotationException;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractCommandProvider {
    private final Map<String, Command<String>> commandMap
        = new HashMap<>();

    @Getter
    private final String representation;
    @Getter(value = AccessLevel.PROTECTED)
    private final ConsoleProcessor consoleProcessor;

    protected AbstractCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                      @NonNull String representation) {
        this(consoleProcessor, representation, Map.of());
    }

    protected AbstractCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                      @NonNull String representation,
                                      @NonNull Map<String, Command<String>> commandMap) {
        this.consoleProcessor = consoleProcessor;
        this.representation = representation;
        this.commandMap.putAll(commandMap);

        this.commandMap.put("exit", new NoArgsCommand<>(() -> consoleProcessor.getProviderStack().clear()));
        this.commandMap.put("done", new NoArgsCommand<>(() -> consoleProcessor.getProviderStack().pop()));

        try {
            CommandProviderAnnotationProcessor.registerAnnotatedCommands(this);
        } catch (TypeParametersUnsupportedByAnnotationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.put(commandName, command));
    }

    public Optional<Command<String>> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}
