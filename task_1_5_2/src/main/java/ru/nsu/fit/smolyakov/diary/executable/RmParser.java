package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;

import java.time.LocalDateTime;

@CommandLine.Command(
        name = "rm",
        mixinStandardHelpOptions = true,
        description = "Removes entry"
)
class RmParser implements Runnable  {
    @CommandLine.Parameters(
            index = "0",
            description = "Heading"
    )
    private String heading;

    @Override
    public void run() {

    }
}
