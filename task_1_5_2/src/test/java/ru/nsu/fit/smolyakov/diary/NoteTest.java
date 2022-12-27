package ru.nsu.fit.smolyakov.diary;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.diary.executable.Main;

import java.io.File;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class NoteTest {
    @Test
    void interactive() {
        var file = new File("diary.json");
        assertThat(file).doesNotExist();

        Main.main(new String[]{"create", "diary.json"});
        assertThat(file).exists();

        Main.main(new String[]{"add", "diary.json", "title", "wow"});
        Main.main(new String[]{"add", "diary.json", "another title", "wooow!!"});
        Main.main(new String[]{"add", "diary.json", "and this is heading", "why???"});
        Main.main(new String[]{"add", "diary.json", "segmentation", "fault"});
        Main.main(new String[]{"add", "diary.json", "segmentation", "fault"});

        file.delete();
    }

}
