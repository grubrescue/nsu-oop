package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@CommandLine.Command(
        name = "rm",
        mixinStandardHelpOptions = true,
        description = "Removes entry"
)
class RmParser implements Runnable  {
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
            System.out.println("Removal successful!");
        } else {
            System.out.println("No such note");
        }

        try {
            diary.toJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
