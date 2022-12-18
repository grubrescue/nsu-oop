package ru.nsu.fit.smolyakov.diary;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.diary.core.Entry;

public class EntryTest {
    @Test
    void test() {
        System.out.println(Entry.create("head", "tail").toString());
    }
}
