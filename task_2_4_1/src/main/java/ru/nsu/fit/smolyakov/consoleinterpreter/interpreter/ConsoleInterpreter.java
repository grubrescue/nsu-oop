package ru.nsu.fit.smolyakov.consoleinterpreter.interpreter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.ConsoleInterpreterException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.EmptyInputException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.FatalInternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.MismatchedAmountOfCommandArgumentsException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.NoSuchCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.presenter.ConsolePresenter;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInterpreter {
    /**
     * Successful exit code.
     */
    public static final int EXIT_SUCCESS = 0;

    /**
     * Unsuccessful exit code.
     */
    public static final int EXIT_FAILURE = 1;

    private final ConsoleProcessor consoleProcessor;
    private final ConsolePresenter consolePresenter;

    /**
     * Creates a new console interpreter with given processor.
     *
     * @param consoleProcessor console processor
     */
    public ConsoleInterpreter(@NonNull ConsoleProcessor consoleProcessor) {
        this.consoleProcessor = consoleProcessor;
        this.consolePresenter = new ConsolePresenter(consoleProcessor);
    }

    /**
     * Prints an error message.
     *
     * @param message message
     */
    public void showError(String message) {
        System.out.println(consolePresenter.errorString(message));
    }

    /**
     * Starts the interpreter.
     *
     * @return exit code
     * @throws IOException if an I/O error occurs
     */
    public int start() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        while (!consoleProcessor.getProviderStack().isEmpty()) {
            System.out.print(consolePresenter.promptString());
            var line = reader.readLine();
            try {
                consoleProcessor.execute(line);
            } catch (EmptyInputException ignored) {
            } catch (FatalInternalCommandException e) {
                showError("FATAL: " + e.getMessage());
                return EXIT_FAILURE;
            } catch (NoSuchCommandException
                     | MismatchedAmountOfCommandArgumentsException
                     | InternalCommandException e) {
                showError(e.getMessage());
            } catch (ConsoleInterpreterException e) {
                showError("UNKNOWN ERROR, EXITING: " + e.getMessage());
                return EXIT_FAILURE;
            }
        }

        return EXIT_SUCCESS;
    }
}
