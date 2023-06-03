package ru.nsu.fit.smolyakov.evaluationsuite.dto.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    private String description = "no description provided in configuration";
    private boolean runTests = true;
    private String name;
    private String branch;
    private Double points;
}
