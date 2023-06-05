package ru.nsu.fit.smolyakov.consoleinterpreter.processor;

import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.ConsoleInterpreterException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.FatalInternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.MismatchedAmountOfCommandArgumentsException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.NoSuchCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.interpreter.ConsoleInterpreter;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Class that processes user input.
 * It parses the input, executes the command and returns the result.
 * It also contains a stack of command providers.
 * When the user enters a command, the processor tries to find it in the topmost provider.
 */
public class ConsoleProcessor {
    @Getter
    private final Stack<AbstractCommandProvider> providerStack
        = new Stack<>();
    // TODO надо бы ограничить доступ к этой штуке

    private ParseResult parse(String line) {
        String[] tokens = line.split(" ");

        if (tokens.length == 0 || tokens[0].isBlank()) {
            throw new EmptyInputException();
        }

        String commandName = tokens[0];
        List<String> args = Arrays.stream(tokens)
            .skip(1)
            .toList();

        return new ParseResult(commandName, args);
    }

    private void executeCommand(ParseResult parseResult) {
        var currentProvider = providerStack.peek();

        currentProvider.getCommand(parseResult.instruction())
            .ifPresentOrElse(
                command -> {
                    if (parseResult.args().size() != command.getArity()) {
                        throw new MismatchedAmountOfCommandArgumentsException();
                    } else {
                        command.execute(parseResult.args());
                    }
                },
                () -> {
                    throw new NoSuchCommandException();
                }
            );
    }

    /**
     * Executes the command.
     * It parses the input, executes the command and returns the result.
     *
     * <p>Thrown exceptions are supposed to be caught by {@link ConsoleInterpreter}.
     *
     * @param line user input
     * @throws EmptyInputException                         if the user input is empty
     * @throws NoSuchCommandException                      if the command is not found
     * @throws MismatchedAmountOfCommandArgumentsException if the amount of arguments is wrong
     * @throws InternalCommandException                    if the command throws an exception
     * @throws FatalInternalCommandException               if the command throws a fatal exception
     * @throws ConsoleInterpreterException                 some other exception (not being described for now)
     */
    public void execute(@NonNull String line) {
        var parseResult = parse(line); // throws exceptions (дописать в жавадоке)
        executeCommand(parseResult); // throws exceptions
    }

    private record ParseResult(
        String instruction,
        List<String> args
    ) {
    }
}
