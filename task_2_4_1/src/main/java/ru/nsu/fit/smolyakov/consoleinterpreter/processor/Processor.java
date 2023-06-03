package ru.nsu.fit.smolyakov.consoleinterpreter.processor;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Processor {
    private final Stack<AbstractCommandProvider> providerStack
        = new Stack<>();

    public Processor(AbstractCommandProvider rootProvider) {
        this.providerStack.push(rootProvider);
    }

    private record ParseResult(
        String instruction,
        List<String> args
    ) {}

    private ParseResult parse(String line) {
        String[] tokens = line.split(" ");

        String commandName = tokens[0];
        List<String> args = Arrays.stream(tokens)
            .skip(1)
            .toList();

        return new ParseResult(commandName, args);
    }

    private boolean executeCommand(ParseResult parseResult) {
        var currentProvider = providerStack.peek();
        var maybeCommand = currentProvider.getCommand(parseResult.instruction());
        maybeCommand.ifPresent(command -> command.execute(parseResult.args()));
        return maybeCommand.isPresent();
    }

    private boolean tryAddSubCommandProvider(ParseResult parseResult) {
        var currentProvider = providerStack.peek();
        var maybeSubCommandProvider = currentProvider.getSubCommandProvider(parseResult.instruction());
        maybeSubCommandProvider.ifPresent(provider -> {
            providerStack.push(provider);
            provider.setArgsContext(parseResult.args());
        });
        return maybeSubCommandProvider.isPresent();
    }

    public void execute(String line) {
        var parseResult = parse(line);
    }
}
