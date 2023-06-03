package ru.nsu.fit.smolyakov.evaluationsuite.dto.group;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {
    public StudentDto(@NonNull String nickName) {
        this.nickName = nickName;
    }
    private final String nickName;

    private String fullName = "unspecified name";
    private String repo;
    private String defaultBranch;
}
