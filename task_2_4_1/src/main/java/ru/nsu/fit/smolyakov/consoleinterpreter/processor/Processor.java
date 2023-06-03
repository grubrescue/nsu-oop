package ru.nsu.fit.smolyakov.consoleinterpreter.processor;

import lombok.Getter;
import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.NoArgsCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.CommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.MismatchedAmountOfCommandArgumentsException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.NoSuchCommandException;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Processor {
    @Getter
    private final Stack<CommandProvider> providerStack
        = new Stack<>();

    public Processor(@NonNull CommandProvider rootProvider) {
        rootProvider.registerCommand("done", new NoArgsCommand<>(providerStack::pop));
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

    private boolean executeCommand(ParseResult parseResult) {
        var currentProvider = providerStack.peek();

        var maybeCommand = currentProvider.getCommand(parseResult.instruction());
        maybeCommand.ifPresent(command -> {
            if (parseResult.args().size() != command.getArity()) {
                throw new MismatchedAmountOfCommandArgumentsException();
            } else {
                command.execute(parseResult.args());
            }
        });
        return maybeCommand.isPresent();
    }

    private boolean tryPushSubCommandProvider(ParseResult parseResult) {
        var currentProvider = providerStack.peek();

        var maybeSubCommandProvider = currentProvider.getSubCommandProvider(parseResult.instruction());
        maybeSubCommandProvider.ifPresent(provider -> {
            providerStack.push(provider);
            provider.setArgsContext(parseResult.args());
            provider.registerCommand("done", new NoArgsCommand<>(providerStack::pop));
            // TODO ее могут перезаписать, надо придумать, как запретить :>
        });
        return maybeSubCommandProvider.isPresent();
    }

    public void execute(@NonNull String line) {
        var parseResult = parse(line);
        if (tryPushSubCommandProvider(parseResult)) {
            //
        } else if (executeCommand(parseResult)) {
            //
        } else {
            throw new NoSuchCommandException();
        }
    }
}
