package ru.nsu.fit.smolyakov.consoleinterpreter.interpreter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.CommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.MismatchedAmountOfCommandArgumentsException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.NoSuchCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.presenter.Presenter;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.Processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interpreter {
    private final Processor processor;
    private final Presenter presenter;

    public Interpreter(@NonNull CommandProvider rootProvider) {
        this.processor = new Processor(rootProvider);
        this.presenter = new Presenter(processor);
    }

    public Interpreter(@NonNull Processor processor) {
        this.processor = processor;
        this.presenter = new Presenter(processor);
    }

    public void showError(String message) {
        System.out.println(presenter.errorString(message));
    }

    /**
     * Starts the interpreter.
     * @return exit code
     */
    public int start() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        while (!processor.getProviderStack().isEmpty()) {
            System.out.print(presenter.promptString());
            var line = reader.readLine();
            try {
                processor.execute(line);
            } catch (NoSuchCommandException e) {
                showError("No such command");
            } catch (EmptyInputException ignored) {
            } catch (MismatchedAmountOfCommandArgumentsException e) {
                showError("Mismatched amount of command arguments");
            }
        }

        return 0; // TODO
    }
}
