package ru.nsu.fit.smolyakov.evaluationsuite.interpreter.provider;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.presenter.EvaluationPresenter;
import ru.nsu.fit.smolyakov.tableprinter.implementations.ConsoleTablePrinter;

public class RootCommandProvider extends AbstractCommandProvider {
    private final SubjectData subjectData;

    @ConsoleCommand
    private void print(String what) {
        switch (what) {
            case "attendance" -> new EvaluationPresenter(subjectData).printAttendance(new ConsoleTablePrinter());
            case "evaluation" -> new EvaluationPresenter(subjectData).printEvaluation(new ConsoleTablePrinter());
        }
    }

    public RootCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                               @NonNull String username,
                               @NonNull SubjectData subjectData) {
        super(consoleProcessor, "User [" + username + "]");
        this.subjectData = subjectData;
    }
}
