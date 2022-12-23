package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new ArgumentsParser()).execute(args);
    }
}
