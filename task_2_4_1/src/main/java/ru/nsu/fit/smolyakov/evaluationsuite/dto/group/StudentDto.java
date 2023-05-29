package ru.nsu.fit.smolyakov.evaluationsuite.dto.group;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudentDto {
    @NonNull String nickName;
    String fullName = "unspecified name";
    String repo;
    String defaultBranch;
}
