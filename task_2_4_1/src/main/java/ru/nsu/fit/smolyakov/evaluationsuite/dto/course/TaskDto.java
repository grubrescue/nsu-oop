package ru.nsu.fit.smolyakov.evaluationsuite.dto.course;

import lombok.Getter;

@Getter
public class TaskDto {
    private final String description = "no description provided in configuration";
    private final boolean runTests = true;
    private String name;
    private String branch;
    private Double points;
}
