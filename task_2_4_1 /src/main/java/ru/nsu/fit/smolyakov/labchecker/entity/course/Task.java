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
    private final String description = "no description provided";
    private final double points = 1.0;

    private final boolean runTests = true;
}
