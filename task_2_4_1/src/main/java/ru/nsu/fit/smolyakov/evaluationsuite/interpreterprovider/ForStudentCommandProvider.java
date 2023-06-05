package ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

public class ForStudentCommandProvider extends AbstractCommandProvider {
    private final Student student;

    @ConsoleCommand
    private void beenOnLesson(String lessonDate) {

    }

    protected ForStudentCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                        @NonNull String studentName,
                                        @NonNull Student student) {
        super(consoleProcessor, "student [" + studentName + "]");
        this.student = student;
    }
}