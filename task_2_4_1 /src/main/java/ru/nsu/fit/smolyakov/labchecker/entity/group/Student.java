package ru.nsu.fit.smolyakov.labchecker.entity.group;

import groovy.lang.GroovyObjectSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Student extends GroovyObjectSupport {
    private String name = "unspecified name";
    private String repo;
    private String defaultBranchName = "main";
}
