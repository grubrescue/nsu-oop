package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
    name = "rm",
    mixinStandardHelpOptions = true,
    description = "Removes entry"
)
class RmParser implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "File."
    )
    private File file;

    @CommandLine.Parameters(
        index = "1",
        description = "Heading"
    )
    private String heading;

    @Override
    public void run() {
        Diary diary;
        try {
            diary = Diary.fromJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (diary.remove(heading)) {
            System.err.printf("Removed an entry with heading \"%s\".", heading);
        } else {
            System.err.printf("Cannot find an entry with heading \"%s\".", heading);
        }

        try {
            diary.toJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
