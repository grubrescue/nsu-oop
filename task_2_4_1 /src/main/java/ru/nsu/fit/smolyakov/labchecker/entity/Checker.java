package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Group;

@Getter
@EqualsAndHashCode
@ToString
public class Checker {
    private Group group = new Group();
//    private
}
