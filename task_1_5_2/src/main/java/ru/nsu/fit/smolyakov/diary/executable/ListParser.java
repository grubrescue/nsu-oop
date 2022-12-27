package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;
import ru.nsu.fit.smolyakov.diary.core.Diary;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@CommandLine.Command(
    name = "list",
    mixinStandardHelpOptions = true,
    description = "Lists entries"
)
class ListParser implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "File."
    )
    private File file;

    @CommandLine.Option(
        names = "--after",
        description = "Filter"
    )
    private ZonedDateTime after = null;

    @CommandLine.Option(
        names = "--before",
        description = "Filter"
    )
    private ZonedDateTime before = null;

    @CommandLine.Option(
        names = "--keywords",
        description = "Filter",
        split = ",",
        arity = "1..*"
    )
    private List<String> keywords = null;

    @Override
    public void run() {
        Diary diary;

        try {
            diary = Diary.fromJson(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var query = diary.query();
        System.out.println(
            query.after(after)
                .before(before)
                .contains(keywords)
                .select()
                .toString()
        );
    }

}
