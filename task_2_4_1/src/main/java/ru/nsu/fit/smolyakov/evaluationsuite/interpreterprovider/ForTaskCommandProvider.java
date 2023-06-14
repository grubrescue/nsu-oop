package ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;

import java.time.LocalDate;

/**
 * Command provider for the "forTask" command.
 * Allows to override task status.
 *
 * @see ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand
 */
public class ForTaskCommandProvider extends AbstractCommandProvider {
    private final SubjectData subjectData;
    private final AssignmentStatus assignmentStatus;

    /**
     * Creates a new command provider for the "forTask" command.
     *
     * @param consoleProcessor console processor
     * @param taskName         task name used for representation
     * @param assignmentStatus assignment status
     * @param subjectData      subject data
     */
    public ForTaskCommandProvider(ConsoleProcessor consoleProcessor,
                                  String taskName,
                                  AssignmentStatus assignmentStatus,
                                  SubjectData subjectData) {
        super(consoleProcessor, "task [" + taskName + "]");
        this.assignmentStatus = assignmentStatus;
        this.subjectData = subjectData;
    }

    @ConsoleCommand(description = "sets task started date")
    private void started(String date) {
        assignmentStatus.getPass().setStarted(LocalDate.parse(date));
    }

    @ConsoleCommand(description = "sets task started date to today")
    private void startedNow() {
        assignmentStatus.getPass().setStarted(LocalDate.now());
    }

    @ConsoleCommand(description = "sets task finished date to today")
    private void finishedNow() {
        assignmentStatus.getPass().setFinished(LocalDate.now());
    }

    @ConsoleCommand(description = "sets task finished date")
    private void finished(String date) {
        assignmentStatus.getPass().setFinished(LocalDate.parse(date));
    }

    @ConsoleCommand(description = "sets the task as able to build")
    private void buildPassed() {
        assignmentStatus.getGrade().setBuildPassed(true);
    }

    @ConsoleCommand(description = "sets javadoc as able to generate")
    private void javadocPassed() {
        assignmentStatus.getGrade().setJavadocPassed(true);
    }

    @ConsoleCommand(description = "sets tests as able to pass")
    private void testsPassed() {
        assignmentStatus.getGrade().setTestsPassed();
    }

    @ConsoleCommand(description = "overrides task points, so that they are not calculated automatically")
    private void overrideTaskPoints(String pointsString) {
        assignmentStatus.getGrade().overrideTaskPoints(Double.parseDouble(pointsString));
    }

    @ConsoleCommand(description = "denies overriding task points")
    private void notOverrideTaskPoints() {
        assignmentStatus.getGrade().notOverrideTaskPoints();
    }

    @ConsoleCommand(description = "sets the task as checkstyle passed")
    private void checkstylePassed() {
        assignmentStatus.getGrade().setCheckstylePassed();
    }
}
