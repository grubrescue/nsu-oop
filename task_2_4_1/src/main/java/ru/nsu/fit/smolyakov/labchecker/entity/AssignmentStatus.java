package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

 //TODO кажется я не те аннотации использую, поменять потом
@Getter
@Setter
@ToString(exclude = {"assignment"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentStatus {
    public static final LocalDate NOT_STARTED = LocalDate.MAX; // todo ??

    @NonNull
    @Setter(AccessLevel.NONE)
    final Assignment assignment;

    @NonNull
    String taskNameAlias;

    String branch;

    @NonNull
    LocalDate started = LocalDate.MAX;

    @NonNull
    LocalDate finished = LocalDate.MAX;

    @NonNull String message = "(non overridden) empty message";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    Double overriddenTaskPoints = null;

    boolean buildOk = false;
    boolean testsOk = false;
    boolean javadocOk = false;

    public AssignmentStatus(@NonNull Assignment assignment, @NonNull String taskNameAlias, String branch) {
        this.assignment = assignment;
        this.taskNameAlias = taskNameAlias;
        this.branch = branch;
    }

    public boolean hasBranch() {
        return Objects.nonNull(this.branch);
    }

    public Optional<String> getBranch() {
        return Optional.ofNullable(this.branch);
    }

    public void overrideTaskPoints(double points) {
        this.overriddenTaskPoints = points;
    }

    public void notOverrideTaskPoints() {
        this.overriddenTaskPoints = null;
    }

    public boolean isOverridden() {
        return Objects.nonNull(this.overriddenTaskPoints);
    }

    public double getCalculatedTaskPoints() {
        double sum = 0;
        int amount = 0;

        sum += (buildOk ? 1 : 0);
        amount++;
        sum += (javadocOk ? 1 : 0);
        amount++;

        if (this.assignment.isRunTests()) {
            sum += (testsOk ? 1 : 0);
            amount++;
        }
        return this.assignment.getSolvedPoints() * (sum / amount);
    }

    public double getTaskPoints() {
        return this.isOverridden() ? this.overriddenTaskPoints : this.getCalculatedTaskPoints();
    }

    public boolean isSkippedSoftDeadline() {
        return this.started.isAfter(this.assignment.getSoftDeadline());
    }

    public boolean isSkippedHardDeadline() {
        return this.finished.isAfter(this.assignment.getHardDeadline());
    }

    public double getFine() {
        double fine = 0;
        if (isSkippedSoftDeadline()) {
            fine += this.assignment.getSoftDeadlineSkipFine();
        }
        if (isSkippedHardDeadline()) {
            fine += this.assignment.getHardDeadlineSkipFine();
        }

        return fine;
    }

    public double getResultingPoints() {
        return getTaskPoints() + getFine();
    }
}
