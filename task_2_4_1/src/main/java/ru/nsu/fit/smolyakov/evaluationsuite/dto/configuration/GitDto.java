package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public class GitDto {
    @NonFinal
    String repoLinkPrefix;
    @NonFinal
    String repoLinkPostfix;
    @NonFinal
    String defaultRepoName;
    @NonFinal
    String docsBranch;
    @NonFinal
    String defaultBranch;
}
