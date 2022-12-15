package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

@Command(
    name = "diary",
    mixinStandardHelpOptions = true,
    description = "Does the job digitalizing your thoughts.",
    subcommands = {
        CreateCommand.class,
        ListCommand.class,
        AddCommand.class,
        RmCommand.class
    }
)
class ArgumentsParser implements Runnable {
    @CommandLine.Spec
    CommandSpec spec;

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }
}
