package ru.nsu.fit.smolyakov.diary;

public record Entry(
        String heading,
        String contents,
        Timestamp timestamp
) {

}