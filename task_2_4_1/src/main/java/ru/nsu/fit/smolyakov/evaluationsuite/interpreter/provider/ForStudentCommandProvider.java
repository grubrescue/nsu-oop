package ru.nsu.fit.smolyakov.evaluationsuite.interpreter.provider;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.Processor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

public class ForStudentCommandProvider extends AbstractCommandProvider {
    private final Student student;

    protected ForStudentCommandProvider(@NonNull Processor processor, @NonNull String representation) {
        super(processor, representation);
    }
}
