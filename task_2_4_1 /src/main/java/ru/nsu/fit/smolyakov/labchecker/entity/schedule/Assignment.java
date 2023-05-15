package ru.nsu.fit.smolyakov.labchecker.entity.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Assignment {
    private String softDeadline;
    private String hardDeadline;
}
