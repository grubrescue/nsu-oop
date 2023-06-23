package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.NoArgsCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.CommandProviderAnnotationProcessor;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract class for command providers.
 * Contains a map of commands and a method to execute them.
 *
 * @see Command
 * @see ConsoleProcessor
 * @see CommandProviderAnnotationProcessor
 */
public abstract class AbstractCommandProvider {
    private final Map<String, Command<String>> commandMap
        = new HashMap<>();

    @Getter
    private final String representation;
    @Getter(value = AccessLevel.PROTECTED)
    private final ConsoleProcessor consoleProcessor;

    /**
     * Creates a new command provider with given processor, representation and empty map of commands.
     *
     * @param consoleProcessor console processor
     * @param representation   representation of this command provider
     */
    protected AbstractCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                      @NonNull String representation) {
        this(consoleProcessor, representation, Map.of());
    }

    /**
     * Creates a new command provider with given processor, representation and map of commands.
     *
     * @param consoleProcessor console processor
     * @param representation   representation of this command provider
     * @param commandMap       map of commands
     */
    protected AbstractCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                      @NonNull String representation,
                                      @NonNull Map<String, Command<String>> commandMap) {
        this.consoleProcessor = consoleProcessor;
        this.representation = representation;
        this.commandMap.putAll(commandMap);

        var annotationProcessor = new CommandProviderAnnotationProcessor(this);
        this.commandMap.putAll(annotationProcessor.generateCommandsMap());

        var helpMsg = "Help message for " + representation + "\n"
            + annotationProcessor.generateHelpMessage()
            + "\n~~~~~~~~~~\n\n"
            + "SYSTEM | help [ 0 ] :: show this message\n"
            + "SYSTEM | done [ 0 ] :: go block down\n"
            + "SYSTEM | exit [ 0 ] :: exit the application\n\n";

        this.commandMap.put("exit", new NoArgsCommand<>(() -> consoleProcessor.getProviderStack().clear()));
        this.commandMap.put("done", new NoArgsCommand<>(() -> consoleProcessor.getProviderStack().pop()));
        this.commandMap.put("help", new NoArgsCommand<>(() -> System.out.println(helpMsg)));
    }

    /**
     * Registers a command with given name and command.
     *
     * @param commandName name of command
     * @param command     command
     * @return {@code true} if the name was not associated with any command before,
     * {@code false} otherwise
     */
    public boolean registerCommand(String commandName, Command<String> command) {
        return Objects.isNull(commandMap.put(commandName, command));
    }

    /**
     * Returns a command with given name.
     *
     * @param commandName name of command
     * @return {@link Optional} of command with given name if it exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<Command<String>> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}
