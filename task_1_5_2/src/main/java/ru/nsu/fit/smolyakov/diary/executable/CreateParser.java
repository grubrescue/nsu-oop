package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@CommandLine.Command(
        name = "create",
        mixinStandardHelpOptions = true,
        description = "Creates new diary."
)
class CreateParser implements Runnable  {
    @CommandLine.Parameters(
            index = "0",
            description = "File."
    )
    private File file;

    @Override
    public void run() {
        try {
            if (file.createNewFile()) {
                System.out.println("Creation successful!");
                Diary diary = new Diary();
                diary.toJson(file);
            } else {
                System.out.println("Creation unsuccessful!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
