package ru.nsu.fit.smolyakov.diary.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class DiaryTest {
    @Test
    void serializeTest() throws IOException {
        File file = new File("src/test/resources/Diary1.json");
        var diary = Diary.fromJson(file);
        System.out.println(diary.toString());

//        diary.toJson(file);
    }
}
