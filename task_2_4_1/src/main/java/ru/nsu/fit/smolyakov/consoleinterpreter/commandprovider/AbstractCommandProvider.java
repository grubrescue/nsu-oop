package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractCommandProvider {
    private final Map<String, Command<String>> commandMap
        = new HashMap<>();
    private final Map<String, AbstractCommandProvider> subCommandProviderMap
        = new HashMap<>();

    private final List<String> argsContext
        = new ArrayList<>();

    public AbstractCommandProvider() {
        this(Map.of(), Map.of());
    }

    public AbstractCommandProvider(Map<String, Command<String>> commandMap,
                                   Map<String, AbstractCommandProvider> subprocessorsMap) {
        this.commandMap.putAll(commandMap);
        this.subCommandProviderMap.putAll(subprocessorsMap);
    }

    public boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.put(commandName, command));
    }

    public boolean registerSubprocessor(String subprocessorName, AbstractCommandProvider provider) {
        return Objects.isNull(subCommandProviderMap.put(subprocessorName, provider));
    }

    public Optional<Command<String>> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }

    public Optional<AbstractCommandProvider> getSubCommandProvider(String provider) {
        return Optional.ofNullable(subCommandProviderMap.get(provider));
    }

    public void setArgsContext(List<String> argsContext) {
        this.argsContext.clear();
        this.argsContext.addAll(argsContext);
    }

    public List<String> getArgsContext() {
        return List.copyOf(argsContext);
    }
}
