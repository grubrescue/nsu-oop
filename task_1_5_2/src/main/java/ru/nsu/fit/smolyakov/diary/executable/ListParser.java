package ru.nsu.fit.smolyakov.diary.executable;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import ru.nsu.fit.smolyakov.diary.core.Diary;

@CommandLine.Command(
        name = "list",
        mixinStandardHelpOptions = true,
        description = "Lists entries"
)
class ListParser implements Runnable  {
    @CommandLine.Option(
            names = "--after",
            description = "Filter"
    )
    LocalDateTime after;

    @CommandLine.Option(
            names = "--before",
            description = "Filter"
    )
    LocalDateTime before;

    @CommandLine.Option(
            names = "--keywords",
            description = "Filter"
    )
    String keywords = null;

    List<String> keywordsSplit(String keywords) {
        if (keywords == null) {
            return null;
        }
        return Arrays.asList(keywords.split(","));
    }

    @Override
    public void run() {
        Diary diary;

        try {
            diary = Diary.fromJson(new File("aboba"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var query = diary.query();
        System.out.println(
                query.after(after)
                        .before(before)
                        .contains(keywordsSplit(keywords))
                        .select()
                        .toString()
        );
    }

}
