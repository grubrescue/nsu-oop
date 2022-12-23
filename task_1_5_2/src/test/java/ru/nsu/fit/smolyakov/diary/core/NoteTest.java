package ru.nsu.fit.smolyakov.diary.core;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.diary.core.Note;

public class NoteTest {
    @Test
    void test() {
        System.out.println(Note.create("head", "tail").toString());
    }
}
