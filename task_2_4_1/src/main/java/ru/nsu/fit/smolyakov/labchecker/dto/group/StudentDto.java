package ru.nsu.fit.smolyakov.labchecker.dto.group;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
public class StudentDto {
    @NonNull String nickName;
    @NonFinal
    String fullName = "unspecified name";
    @NonFinal
    String repo;
    @NonFinal
    String defaultBranch;
}
