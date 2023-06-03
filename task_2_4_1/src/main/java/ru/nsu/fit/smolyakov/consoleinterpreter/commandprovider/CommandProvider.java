package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CommandProvider {
    private final Map<String, Command<String>> commandMap
        = new HashMap<>();
    private final Map<String, CommandProvider> subCommandProviderMap
        = new HashMap<>();

    private final List<String> argsContext
        = new ArrayList<>();

    @Getter
    private final String representation;

    public CommandProvider(@NonNull String representation) {
        this(representation, Map.of(), Map.of());
    }

    public CommandProvider(@NonNull String representation,
                           @NonNull Map<String, Command<String>> commandMap,
                           @NonNull Map<String, CommandProvider> subprocessorsMap) {
        this.representation = representation;
        this.commandMap.putAll(commandMap);
        this.subCommandProviderMap.putAll(subprocessorsMap);
    }

    public boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.putIfAbsent(commandName, command));
    }

    public boolean registerSubprocessor(String subprocessorName, CommandProvider provider) {
        return Objects.isNull(subCommandProviderMap.putIfAbsent(subprocessorName, provider));
    }

    public Optional<Command<String>> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }

    public Optional<CommandProvider> getSubCommandProvider(String provider) {
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
