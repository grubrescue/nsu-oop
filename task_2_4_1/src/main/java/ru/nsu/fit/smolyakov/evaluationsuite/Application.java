package ru.nsu.fit.smolyakov.evaluationsuite;

import ru.nsu.fit.smolyakov.consoleinterpreter.interpreter.ConsoleInterpreter;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider.RootCommandProvider;

import java.io.IOException;

/**
 * Main class.
 */
public class Application {
    /**
     * Main method. Runs the {@link ConsoleInterpreter},
     * using {@link RootCommandProvider} as the root command provider.
     *
     * @param args command line arguments (ignored)
     * @throws IOException if an I/O error occurs
     */
    public static void main(String... args) throws IOException {
        var processor = new ConsoleProcessor();
        var rootCommandProvider = new RootCommandProvider(
            processor,
            System.getProperty("user.name")
        );
        processor.getProviderStack().push(rootCommandProvider);
        System.exit(new ConsoleInterpreter(processor).start());
    }
}
