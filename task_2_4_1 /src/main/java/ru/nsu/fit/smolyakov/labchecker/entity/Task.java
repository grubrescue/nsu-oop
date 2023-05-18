package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Task {
    String identifier;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    TaskInfo defaultTaskInfo;

    boolean runTests;

    public Task(String identifier,
                LocalDate softDeadline,
                LocalDate hardDeadline,
                String branch,
                boolean runTests) {
        this.identifier = identifier;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
        this.defaultTaskInfo = new TaskInfo(this, branch);
        this.runTests = runTests;
    }
}
