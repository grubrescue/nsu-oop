package ru.nsu.fit.smolyakov.diary;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.diary.core.Note;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NoteTest {
    static ZonedDateTime time =
        Instant.EPOCH
            .plusSeconds((long) Integer.MAX_VALUE)
            .atZone(ZoneId.of("America/Los_Angeles"));
    static Note note
        = new Note(
            "hello nsu from 2022",
            "Irtegov still has the same tasks for unix course in 2038",
            time
    );

    @Test
    void toStringTest() {
        assertThat(note.toString())
            .isEqualTo(
                """
                    Heading: hello nsu from 2022
                    Text: Irtegov still has the same tasks for unix course in 2038
                    ------------
                    Date: Mon, 18 Jan 2038 19:14:07 -0800
                    
                    """
            );
    }

    @Test
    void beforeTest() {
        assertThat(note.before(time.minusYears(20)))
            .isFalse();
        assertThat(note.before(time.plusYears(20)))
            .isTrue();
    }

    @Test
    void afterTest() {
        assertThat(note.after(time.plusNanos(20)))
            .isFalse();
        assertThat(note.after(time.minusNanos(20)))
            .isTrue();
    }
}
