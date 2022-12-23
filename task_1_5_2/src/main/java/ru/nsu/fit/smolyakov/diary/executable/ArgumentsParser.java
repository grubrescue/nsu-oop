package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "diary",
        mixinStandardHelpOptions = true,
        description = "Saves your thoughts.",
        subcommands = {
                CreateParser.class,
                ListParser.class,
                AddParser.class,
                RmParser.class
        }
)
class ArgumentsParser implements Runnable {
    @Override
    public void run() {

    }
}
