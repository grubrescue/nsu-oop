package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@Value
@With
@RequiredArgsConstructor
public class TaskInfo {
    Task task;
    String branch;

    LocalDate started
        = LocalDate.MAX;
    LocalDate finished
        = LocalDate.MAX;

    // TODO сделать может флаг чтобы если выставлено то поинты сами считались а иначе чтобы нет я не знаю даже
    // ЛИБО сделать чето по другому
    double points = 0;
}
