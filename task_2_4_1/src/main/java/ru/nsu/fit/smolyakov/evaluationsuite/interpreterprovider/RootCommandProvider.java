package ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.evaluator.Evaluator;
import ru.nsu.fit.smolyakov.evaluationsuite.presenter.EvaluationPresenter;
import ru.nsu.fit.smolyakov.evaluationsuite.util.ConfigToDtoDeserializer;
import ru.nsu.fit.smolyakov.evaluationsuite.util.SubjectDataDtoToEntity;
import ru.nsu.fit.smolyakov.evaluationsuite.util.SubjectDataEntitySerializer;
import ru.nsu.fit.smolyakov.tableprinter.implementations.ConsoleTablePrinter;
import ru.nsu.fit.smolyakov.tableprinter.implementations.HtmlTablePrinter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.DUMP_FILE_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.HTML_ATTENDANCE;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.HTML_EVALUATION;

public class RootCommandProvider extends AbstractCommandProvider {
    private SubjectData subjectData;

    @ConsoleCommand
    @SneakyThrows
    private void save() {
        SubjectDataEntitySerializer.serialize(this.subjectData, DUMP_FILE_PATH);
    }

    @ConsoleCommand
    @SneakyThrows
    private void backupDump() {
        var originalPath = Paths.get(DUMP_FILE_PATH);
        var copied = Paths.get(DUMP_FILE_PATH + ".bak");
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
    }

    @ConsoleCommand
    @SneakyThrows
    private void restoreDump() {
        var originalPath = Paths.get(DUMP_FILE_PATH);
        var copied = Paths.get(DUMP_FILE_PATH + ".bak");
        Files.copy(copied, originalPath, StandardCopyOption.REPLACE_EXISTING);
        this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
    }

    @ConsoleCommand
    @SneakyThrows
    private void cleanConfigDump() {
        backupDump();
        this.subjectData = new SubjectDataDtoToEntity(ConfigToDtoDeserializer.deserialize()).convert();
        this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
    }

    @ConsoleCommand
    private void evaluate() {
        cleanConfigDump();
        this.subjectData
            .getGroup()
            .getStudentList()
            .stream()
            .map(Evaluator::new)
            .forEach(Evaluator::evaluate);
    }

    @ConsoleCommand
    @SneakyThrows
    private void print(String what) {
        switch (what) {
            case "attendance" ->
                    new EvaluationPresenter(subjectData)
                        .printAttendance(new ConsoleTablePrinter());
            case "evaluation" ->
                    new EvaluationPresenter(subjectData)
                        .printEvaluation(new ConsoleTablePrinter());
            default -> throw new InternalCommandException("only attendance and evaluation are supported");
        }
    }

    @ConsoleCommand
    @SneakyThrows
    private void html(String what) {
        switch (what) {
            case "attendance" ->
                    new EvaluationPresenter(subjectData)
                        .printAttendance(new HtmlTablePrinter(new File(HTML_ATTENDANCE)));
            case "evaluation" ->
                    new EvaluationPresenter(subjectData)
                        .printEvaluation(new HtmlTablePrinter(new File(HTML_EVALUATION)));
            default -> throw new InternalCommandException("only attendance and evaluation are supported");
        }
    }

    @ConsoleCommand
    private void forGroup(String studentName) {
        this.subjectData.getGroup()
            .getByNickName(studentName)
            .ifPresentOrElse(
                student -> {
                    getConsoleProcessor().pushProvider(
                        new ForStudentCommandProvider(
                            getConsoleProcessor(),
                            studentName,
                            student
                        )
                    );
                },
                () -> {
                    throw new InternalCommandException("No student with such nickname");
                }
            );
    }

    public RootCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                               @NonNull String username) {
        super(consoleProcessor, "User [" + username + "]");
        if (new File(DUMP_FILE_PATH).exists()) {
            try {
                this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
                return;
            } catch (IOException ignored) {
            }
        }

        try {
            this.subjectData = new SubjectDataDtoToEntity(ConfigToDtoDeserializer.deserialize()).convert();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
