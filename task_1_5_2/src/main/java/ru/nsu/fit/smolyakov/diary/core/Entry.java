package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public record Entry (
        String heading,
        String contents,
        LocalDateTime date
) {


    public static Entry create(String heading, String contents) {
        return new Entry(heading, contents, LocalDateTime.now());
    }

    public boolean contains(String keyword) { // TODO maybe rename maybe not
        return heading.contains(keyword);
    }

    public boolean til(LocalDateTime date) {
        return true; // TODO
    }

    public boolean until(LocalDateTime date) {
        return true; // TODO
    }
}
