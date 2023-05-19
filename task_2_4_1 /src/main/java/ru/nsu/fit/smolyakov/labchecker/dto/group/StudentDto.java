package ru.nsu.fit.smolyakov.labchecker.dto.group;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
public class StudentDto {
    public StudentDto(String nickName) {
        this.nickName = nickName;
    }

    String nickName;
    @NonFinal String fullName = "unspecified name";
    @NonFinal String repo;
    @NonFinal String defaultBranchName;
}
