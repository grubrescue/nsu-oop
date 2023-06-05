package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.Processor;

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
    private final Processor processor;

    protected AbstractCommandProvider(@NonNull Processor processor,
                                      @NonNull String representation) {
        this(processor, representation, Map.of());
    }

    protected AbstractCommandProvider(@NonNull Processor processor,
                                      @NonNull String representation,
                                      @NonNull Map<String, Command<String>> commandMap) {
        this.processor = processor;
        this.representation = representation;
        this.commandMap.putAll(commandMap);
    }

    protected boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.putIfAbsent(commandName, command));
    }

    protected Optional<Command<String>> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}
