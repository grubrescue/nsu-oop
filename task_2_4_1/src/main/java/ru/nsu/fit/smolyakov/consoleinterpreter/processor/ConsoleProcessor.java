package ru.nsu.fit.smolyakov.consoleinterpreter.processor;

import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.MismatchedAmountOfCommandArgumentsException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.NoSuchCommandException;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ConsoleProcessor {
    @Getter
    private final Stack<AbstractCommandProvider> providerStack
        = new Stack<>();
    // TODO надо бы ограничить доступ к этой штуке

    public void pushProvider(@NonNull AbstractCommandProvider rootProvider) {
        this.providerStack.push(rootProvider);
    }

    private record ParseResult(
        String instruction,
        List<String> args
    ) {}

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

    public void execute(@NonNull String line) {
        var parseResult = parse(line); // throws exceptions (дописать в жавадоке)
        executeCommand(parseResult); // throws exceptions
    }
}
