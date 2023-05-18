package ru.nsu.fit.smolyakov.labchecker.dto.group;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
public class StudentDto {
    public StudentDto(String nickName) {
        this.nickName = nickName;
    }

    @NonFinal String fullName = "unspecified name";
    String nickName;
    @NonFinal String repo;
    @NonFinal String defaultBranchName;
}
