package ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
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
import java.time.ZonedDateTime;

import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.DUMP_FILE_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.HTML_ATTENDANCE;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.HTML_EVALUATION;

@Log4j2
public class RootCommandProvider extends AbstractCommandProvider {
    private SubjectData subjectData;

    public RootCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                               @NonNull String username) {
        super(consoleProcessor, "" + username + ":");
        if (new File(DUMP_FILE_PATH).exists()) {
            try {
                this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
                return;
            } catch (IOException ignored) {
                log.warn("Failed to deserialize dump file, creating new one");
            }
        }

        try {
            this.subjectData = new SubjectDataDtoToEntity(ConfigToDtoDeserializer.deserialize()).convert();
        } catch (IOException e) {
            log.fatal("Failed to deserialize config file");
            throw new RuntimeException(e);
        }
    }

    @ConsoleCommand(description = "saves current state of the evaluation to dumps/dump")
    private void save() throws IOException {
        SubjectDataEntitySerializer.serialize(this.subjectData, DUMP_FILE_PATH);
    }

    @ConsoleCommand(description = "copies dumps/dump into dumps/dump.bak")
    private void backupDump() throws IOException {
        var originalPath = Paths.get(DUMP_FILE_PATH);
        var copied = Paths.get(DUMP_FILE_PATH + ".bak");
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
    }

    @ConsoleCommand(description = "restores dumps/dump from dumps/dump.bak")
    private void restoreDump() throws IOException {
        var originalPath = Paths.get(DUMP_FILE_PATH);
        var copied = Paths.get(DUMP_FILE_PATH + ".bak");
        Files.copy(copied, originalPath, StandardCopyOption.REPLACE_EXISTING);
        this.subjectData = SubjectDataEntitySerializer.deserialize(DUMP_FILE_PATH);
    }

    @ConsoleCommand(description = "generates an empty evaluation from groovy dsl configuration")
    private void cleanFromConfig() throws IOException {
        backupDump();
        this.subjectData = new SubjectDataDtoToEntity(ConfigToDtoDeserializer.deserialize()).convert();
    }

    @ConsoleCommand(description = "generates an empty evaluation from groovy dsl configuration and evaluates it")
    private void evaluate() throws IOException {
        cleanFromConfig();
        this.subjectData
            .getGroup()
            .getStudentList()
            .stream()
            .map(Evaluator::new)
            .forEach(Evaluator::evaluate);
        this.subjectData
            .setLastUpdate(ZonedDateTime.now());
    }

    @ConsoleCommand(description = "prints evaluation or attendance table to console")
    private void print(String what) throws IOException {
        switch (what) {
            case "attendance" -> new EvaluationPresenter(subjectData)
                .printAttendance(new ConsoleTablePrinter());
            case "evaluation" -> new EvaluationPresenter(subjectData)
                .printEvaluation(new ConsoleTablePrinter());
            default -> throw new InternalCommandException("only attendance and evaluation are supported");
        }
    }

    @ConsoleCommand(description = "prints evaluation or attendance table to html file (located at html/)")
    private void html(String what) throws IOException {
        switch (what) {
            case "attendance" -> new EvaluationPresenter(subjectData)
                .printAttendance(new HtmlTablePrinter(new File(HTML_ATTENDANCE)));
            case "evaluation" -> new EvaluationPresenter(subjectData)
                .printEvaluation(new HtmlTablePrinter(new File(HTML_EVALUATION)));
            default -> throw new InternalCommandException("only attendance and evaluation are supported");
        }
    }

    @ConsoleCommand(description = "opens new block of commands for student with given nickname")
    private void forStudent(String studentName) {
        this.subjectData.getGroup()
            .getByNickName(studentName)
            .ifPresentOrElse(
                student -> {
                    getConsoleProcessor().getProviderStack().push(
                        new ForStudentCommandProvider(
                            getConsoleProcessor(),
                            studentName,
                            student,
                            subjectData
                        )
                    );
                },
                () -> {
                    throw new InternalCommandException("No student with such nickname");
                }
            );
    }
}
