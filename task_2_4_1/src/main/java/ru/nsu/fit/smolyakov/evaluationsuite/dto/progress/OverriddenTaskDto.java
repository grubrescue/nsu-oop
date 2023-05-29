package ru.nsu.fit.smolyakov.evaluationsuite.dto.progress;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class OverriddenTaskDto {
    private final String taskName;
    private final boolean noBranch = false;
    private String identifierAlias;
    private LocalDate started;
    private LocalDate finished;
    private Double points;
    private String message;
    private String branch;

    void startedAt (String dateString) {
        this.started = LocalDate.parse(dateString);
    }

    void finishedAt (String dateString) {
        this.finished = LocalDate.parse(dateString);
    }

    void solvedWithPoints (double points) {
        this.points = points;
    }
}
