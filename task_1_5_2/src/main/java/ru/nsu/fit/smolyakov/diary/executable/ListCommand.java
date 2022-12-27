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
    description = "Lists notes from a specified diary."
)
class ListCommand implements Runnable {
    @CommandLine.Parameters(
        index = "0",
        description = "a specified Json-file associated with diary"
    )
    private File file;

    @CommandLine.Option(
        names = "--after",
        description = "show notes created after a specified date, " +
            "formatted according to \"yyyy-MM-dd'T'HH:mm:ssZZZ\" pattern"
    )
    private ZonedDateTime after = null;

    @CommandLine.Option(
        names = "--before",
        description = "show notes created before a specified date, " +
            "formatted according to \"yyyy-MM-dd'T'HH:mm:ssZZZ\" pattern"
    )
    private ZonedDateTime before = null;

    @CommandLine.Option(
        names = "--keywords",
        description = "show notes containing specified keywords " +
            "in a comma-separated format",
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
