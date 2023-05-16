package ru.nsu.fit.smolyakov.labchecker.entity.group;

import groovy.lang.Closure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.entity.course.Task;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
public class Student {
    private final String name = "unspecified name";
    private String repo;
    private final String defaultBranchName = "main";
}
