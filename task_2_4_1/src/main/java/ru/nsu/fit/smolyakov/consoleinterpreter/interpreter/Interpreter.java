package ru.nsu.fit.smolyakov.consoleinterpreter.interpreter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.ConsoleInterpreterException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.FatalInternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;
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

    public final int EXIT_SUCCESS = 0;
    public final int EXIT_FAILURE = 1;

    public Interpreter(@NonNull AbstractCommandProvider rootProvider) {
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
            } catch (EmptyInputException ignored) {
            } catch (FatalInternalCommandException e) {
                showError("FATAL: " + e.getMessage());
                return EXIT_FAILURE;
            } catch (NoSuchCommandException
                     | MismatchedAmountOfCommandArgumentsException
                     | InternalCommandException e) {
                showError(e.getMessage());
            } catch (ConsoleInterpreterException e) {
                showError("UNKNOWN ERROR, EXITING: "+ e.getMessage());
                return EXIT_FAILURE;
            }
        }

        return EXIT_SUCCESS;
    }
}
