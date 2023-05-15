package ru.nsu.fit.smolyakov.labchecker.entity.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Git {
    private String repoLinkPrefix = "https://github.com/";
    private String repoLinkPostfix = ".git";
}
