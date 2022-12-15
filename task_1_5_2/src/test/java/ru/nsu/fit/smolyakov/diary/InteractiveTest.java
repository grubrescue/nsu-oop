package ru.nsu.fit.smolyakov.diary;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.diary.core.Diary;
import ru.nsu.fit.smolyakov.diary.executable.Main;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class InteractiveTest {
    static final String filename = "src/test/resources/diary.json";
    static File file;

    @BeforeAll
    static void init() {
        file = new File("src/test/resources/diary.json");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void interactiveTest() throws IOException {
        assertThat(file).doesNotExist();

        Main.main(new String[]{"create", filename});
        assertThat(file).exists();

        Main.main(new String[]{"create", filename});
        assertThat(file).exists();

        Diary diary = Diary.fromJson(file);
        assertThat(diary.count()).isZero();

        Main.main(new String[]{"add", filename, "title", "wow"});
        Main.main(new String[]{"add", filename, "another title", "wooow!!"});
        Main.main(new String[]{"add", filename, "and this is heading", "why???"});
        Main.main(new String[]{"add", filename, "segmentation", "fault"});
        Main.main(new String[]{"add", filename, "segmentation", "fault"});

        diary = Diary.fromJson(file);
        assertThat(diary.count()).isEqualTo(5);

        assertThat(diary.query().after(ZonedDateTime.now()).select().count()).isZero();
        assertThat(diary.query().after(ZonedDateTime.now()).before(ZonedDateTime.now()).select().count()).isZero();
        assertThat(diary.query().before(ZonedDateTime.now()).select().count()).isEqualTo(5);

        assertThat(diary.query().containsInHeading("another title").select().count()).isEqualTo(1);
        assertThat(diary.query().containsInHeading("anothertitle").select().count()).isZero();
        assertThat(diary.query().containsInHeading("another").containsInHeading("heading").select().count()).isEqualTo(2);

        Main.main(new String[]{"rm", filename, "segmentation"});

        diary = Diary.fromJson(file);
        assertThat(diary.count()).isEqualTo(3);

        Main.main(new String[]{"rm", filename, "nonexistant entry"});

        diary = Diary.fromJson(file);
        assertThat(diary.count()).isEqualTo(3);
    }
}
