package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;

import java.time.LocalDateTime;

@CommandLine.Command(
        name = "add",
        mixinStandardHelpOptions = true,
        description = "Inserts entries"
)
class AddParser implements Runnable  {
    @CommandLine.Parameters(
            index = "0",
            description = "Heading"
    )
    private String heading;

    @CommandLine.Parameters(
            index = "1",
            description = "Contents"
    )
    private String contents;

    @Override
    public void run() {

    }
}
