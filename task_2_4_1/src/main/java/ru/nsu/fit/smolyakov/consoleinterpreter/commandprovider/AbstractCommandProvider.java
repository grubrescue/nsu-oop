package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractCommandProvider {
    private final Map<String, Command<String>> commandMap
        = new HashMap<>();
    private final Map<String, AbstractCommandProvider> subprocessorsMap
        = new HashMap<>();

    public AbstractCommandProvider() {
        this(Map.of(), Map.of());
    }

    public AbstractCommandProvider(Map<String, Command<String>> commandMap,
                                   Map<String, AbstractCommandProvider> subprocessorsMap) {
        this.commandMap.putAll(commandMap);
        this.subprocessorsMap.putAll(subprocessorsMap);
    }

    public boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.put(commandName, command));
    }

    public boolean registerSubprocessor(String subprocessorName, AbstractCommandProvider processor) {
        return Objects.isNull(subprocessorsMap.put(subprocessorName, processor));
    }

    public Optional<Command<String>> executeCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }

    public Optional<AbstractCommandProvider> getSubprocessor(String subprocessorName) {
        return Optional.ofNullable(subprocessorsMap.get(subprocessorName));
    }
}
