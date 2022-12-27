package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;

/**
 * A {@code Main} class containing only one {@link #main(String[]) method} with
 * the obvious meaning.
 */
public class Main {
    /**
     * Parses command line arguments and does the
     * job specified by them.
     *
     * @param args diary commands
     */
    public static void main(String[] args) {
        new CommandLine(new ArgumentsParser()).execute(args);
    }
}
