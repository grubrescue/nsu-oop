package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Assignment status.
 */
@ToString(exclude = {"assignment"})
@Getter
public class AssignmentStatus implements Serializable {
    /**
     * A date that is considered to be a date way after the assignment has started.
     */
    public static final LocalDate NOT_STARTED = LocalDate.MAX; // todo ??

    private final Assignment assignment;
    private final Pass pass = new Pass();
    private final Grade grade = new Grade();
    @Setter
    private String identifierAlias;
    @Setter
    private String branch;
    @Setter
    private String message = "(non overridden) empty message";

    /**
     * Creates a new instance of {@link AssignmentStatus} corresponding to specified assignment.
     *
     * @param assignment      an assignment
     * @param identifierAlias an identifier alias
     * @param branch          a branch (nullable)
     */
    public AssignmentStatus(@NonNull Assignment assignment, @NonNull String identifierAlias, String branch) {
        this.assignment = assignment;
        this.identifierAlias = identifierAlias;
        this.branch = branch;
    }

    /**
     * Returns if a student has a branch with intermediate results for this assignment.
     *
     * @return if a student has a branch with intermediate results for this assignment
     */
    public boolean hasBranch() {
        return Objects.nonNull(this.branch);
    }

    /**
     * Returns if a student has a branch with intermediate results for this assignment.
     *
     * @return {@link Optional} of a branch with intermediate results for this assignment
     * or {@link Optional#empty()} if a student has no branch with intermediate results for this assignment
     */
    public Optional<String> getBranch() {
        return Optional.ofNullable(this.branch);
    }

    /**
     * Pass.
     */
    @Setter
    @Getter
    @NonNull
    public class Pass implements Serializable { // todo rename
        private LocalDate started = LocalDate.MAX;
        private LocalDate finished = LocalDate.MAX;

        /**
         * Returns if a student has skipped the soft deadline.
         *
         * @return if a student has skipped the soft deadline
         */
        public boolean isSkippedSoftDeadline() {
            return this.started.isAfter(AssignmentStatus.this.assignment.getSoftDeadline().plusDays(1));
        }

        /**
         * Returns if a student has skipped the hard deadline.
         *
         * @return if a student has skipped the hard deadline
         */
        public boolean isSkippedHardDeadline() {
            return this.finished.isAfter(AssignmentStatus.this.assignment.getHardDeadline().plusDays(1));
        }

        /**
         * Returns if a student has started the assignment.
         *
         * @return if a student has started the assignment
         */
        public boolean isStarted() {
            return !this.started.equals(NOT_STARTED);
        }

        /**
         * Returns if a student has finished the assignment.
         *
         * @return if a student has finished the assignment
         */
        public boolean isFinished() {
            return !this.finished.equals(NOT_STARTED);
        }
    }

    /**
     * Grade.
     */
    public class Grade implements Serializable {
        @Getter
        @Setter
        private boolean buildPassed = false;
        @Getter
        @Setter
        private boolean javadocPassed = false;

        private Double jacocoCoverage = null;
        private Double overriddenTaskPoints = null;

        @Getter
        @Setter
        private int checkstyleWarnings = Integer.MAX_VALUE;

        /**
         * Returns if a student has tests that compile and have at least the required coverage.
         *
         * @return if a student has tests that compile and have at least the required coverage
         */
        public boolean isTestsPassed() {
            return isTestsCompile() &&
                jacocoCoverage >= AssignmentStatus.this.assignment.getJacocoPassCoefficient();
        }

        /**
         * Returns if a student has tests that at least compile.
         *
         * @return if a student has tests that at least compile
         */
        public boolean isTestsCompile() {
            return jacocoCoverage != null;
        }

        /**
         * Sets that a student has tests that at least compile.
         */
        public void setTestsCompile() {
            this.jacocoCoverage = 0.0;
        }

        /**
         * Returns a student's tests coverage.
         *
         * @return {@link Optional} of a student's tests coverage
         * or {@link Optional#empty()} if a student hasn't got any tests
         */
        public Optional<Double> getJacocoCoverage() {
            return Optional.ofNullable(jacocoCoverage);
        }

        /**
         * Sets a student's tests coverage.
         * Automatically sets that a student has tests that at least compile.
         *
         * @param coverage a student's tests coverage
         */
        public void setJacocoCoverage(double coverage) {
            this.jacocoCoverage = coverage;
        }

        /**
         * Sets that a student has tests that has the maximum coverage.
         * Automatically sets that a student has tests that at least compile.
         */
        public void setTestsPassed() {
            this.jacocoCoverage = 1.0;
        }

        /**
         * Overrides a student's task points.
         * If task points are overridden, the student's grade is equal to {@code points}
         * and not being calculated automatically.
         *
         * @param points a student's overridden task points
         */
        public void overrideTaskPoints(double points) {
            this.overriddenTaskPoints = points;
        }

        /**
         * Denotes that a student's task points are not overridden and
         * should be calculated automatically.
         *
         * @see #getCalculatedTaskPoints()
         */
        public void notOverrideTaskPoints() {
            this.overriddenTaskPoints = null;
        }

        /**
         * Returns if a student's task points are overridden.
         *
         * @return if a student's task points are overridden
         */
        public boolean isOverridden() {
            return Objects.nonNull(this.overriddenTaskPoints);
        }

        /**
         * Returns a student's automatically calculated task points.
         * He may receive 0 or 1 point for build,
         * 0 or 1 point for javadoc and
         * 0 or a value from minimal coverage coefficient to 1 point for tests.
         * The final grade is calculated as the average of these points.
         *
         * <p>If tests are not enabled, the final grade is calculated
         * as the average of build and javadoc points.
         *
         * @return a student's task points
         */
        public double getCalculatedTaskPoints() {
            double sum = 0;
            int amount = 0;

            sum += (buildPassed ? 1 : 0);
            amount++;
            sum += (javadocPassed ? 1 : 0);
            amount++;

            if (AssignmentStatus.this.assignment.isRunTests()) {
                sum += jacocoCoverage != null
                    && jacocoCoverage >= AssignmentStatus.this.assignment.getJacocoPassCoefficient()
                    ? jacocoCoverage : 0;
                amount++;
            }
            return AssignmentStatus.this.assignment.getSolvedPoints() * (sum / amount);
        }

        /**
         * Returns a student's overridden task points.
         *
         * @return {@link Optional} of a student's overridden task points
         * or {@link Optional#empty()} if a student's task points are not overridden
         */
        public Optional<Double> getOverriddenTaskPoints() {
            return Optional.ofNullable(overriddenTaskPoints);
        }

        /**
         * Returns a student's earned points.
         * If task points are overridden, returns them.
         * Otherwise, returns automatically calculated task points.
         *
         * @return a student's earned points
         * @see #getCalculatedTaskPoints()
         */
        public double getEarnedPoints() {
            if (!AssignmentStatus.this.getPass().isFinished()) {
                return 0.0;
            } else if (this.isOverridden()) {
                return this.overriddenTaskPoints;
            } else {
                return this.getCalculatedTaskPoints();
            }
        }

        /**
         * Returns a student's fine for skipping deadlines.
         * WARNING! The value is negative, so it should be added to the final grade,
         * !!!!NOT!!!!! subtracted. Thank you.
         *
         * @return a student's fine
         */
        public double getFine() {
            double fine = 0;
            if (AssignmentStatus.this.getPass().isSkippedSoftDeadline()) {
                fine += AssignmentStatus.this.assignment.getSoftDeadlineSkipFine();
            }
            if (AssignmentStatus.this.getPass().isSkippedHardDeadline()) {
                fine += AssignmentStatus.this.assignment.getHardDeadlineSkipFine();
            }

            return fine;
        }

        /**
         * Returns a student's resulting points.
         * If a student hasn't finished the assignment, only returns his fine.
         * Otherwise, returns his earned points plus fine.
         *
         * @return a student's resulting points
         */
        public double getResultingPoints() {
            if (!AssignmentStatus.this.getPass().isFinished()) {
                return getFine();
            } else {
                return getEarnedPoints() + getFine();
            }
        }

        /**
         * Returns if this task passed checkstyle.
         * @return true if this task passed checkstyle
         */
        public boolean isPassedCheckstyle() {
            return checkstyleWarnings <= AssignmentStatus.this.assignment.getCheckstyleWarningsLimit();
        }

        /**
         * Sets checkstyle warnings to zero.
         */
        public void setCheckstylePassed() {
            this.checkstyleWarnings = 0;
        }
    }
}
