package ru.nsu.fit.smolyakov.labchecker.dto.group;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public class StudentDto {
    String nickName;
    @NonFinal
    String fullName = "unspecified name";
    @NonFinal
    String repo;
    @NonFinal
    String defaultBranchName;
    public StudentDto(String nickName) {
        this.nickName = nickName;
    }
}
