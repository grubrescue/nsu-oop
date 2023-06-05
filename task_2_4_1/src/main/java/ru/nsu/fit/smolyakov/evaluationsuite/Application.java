package ru.nsu.fit.smolyakov.evaluationsuite;

import ru.nsu.fit.smolyakov.consoleinterpreter.interpreter.ConsoleInterpreter;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider.RootCommandProvider;

import java.io.IOException;

public class Application {
    public static void main(String... args) throws IOException {
        var processor = new ConsoleProcessor();
        var rootCommandProvider = new RootCommandProvider(
            processor,
            System.getProperty("user.name")
            );
        processor.pushProvider(rootCommandProvider);
        System.exit(new ConsoleInterpreter(processor).start());
    }
}
