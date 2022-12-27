package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
    name = "add",
    mixinStandardHelpOptions = true,
    description = "Inserts entries to the specified diary."
)
class AddParser implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "a specified Json-file associated with diary"
    )
    private File file;
    @CommandLine.Parameters(
        index = "1",
        description = "a title of a note"
    )
    private String heading;

    @CommandLine.Parameters(
        index = "2",
        description = "a text of a note"
    )
    private String contents;

    @Override
    public void run() {
        Diary diary;
        try {
            diary = Diary.fromJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        diary.insert(heading, contents);

        System.err.printf("Inserted \"%s\".", heading);

        try {
            diary.toJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
