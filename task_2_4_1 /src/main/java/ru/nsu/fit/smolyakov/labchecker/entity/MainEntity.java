package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;

@Value
@NonNull
public class MainEntity { // TODO rename
    Course course;
    Group group;
}
