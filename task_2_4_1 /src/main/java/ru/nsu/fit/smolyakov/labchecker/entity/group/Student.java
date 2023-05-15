package ru.nsu.fit.smolyakov.labchecker.entity.group;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Student {
    private final String name = "unspecified name";
    private String repo;
    private final String defaultBranchName = "main";
}
