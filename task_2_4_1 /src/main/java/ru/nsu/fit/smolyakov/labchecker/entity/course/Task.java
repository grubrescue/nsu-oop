package ru.nsu.fit.smolyakov.labchecker.entity.course;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Task {
    private String name;
    private String branch;
    private String description = "no description provided";
    private double points = 1.0;

    private boolean runTests = true;
}
