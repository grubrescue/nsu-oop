package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import lombok.Getter;

@Getter
public class GitDto {
    private String repoLinkPrefix;
    private String repoLinkPostfix;
    private String defaultRepoName;
    private String docsBranch;
    private String defaultBranch;
}
