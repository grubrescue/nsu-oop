package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
    name = "create",
    mixinStandardHelpOptions = true,
    description = "Creates new diary book."
)
class CreateParser implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "a specified Json-file associated with diary"
    )
    private File file;

    @Override
    public void run() {
        try {
            if (file.createNewFile()) {
                Diary diary = new Diary();
                diary.toJson(file);

                System.err.printf("New diary created in %s.", file.getPath());
            } else {
                System.err.printf("%s already exists!", file.getPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
