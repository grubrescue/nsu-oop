package ru.nsu.fit.smolyakov.labchecker;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.eclipse.jgit.api.errors.GitAPIException;
import ru.nsu.fit.smolyakov.labchecker.checker.EvaluationRunner;
import ru.nsu.fit.smolyakov.labchecker.entity.CheckerScript;

import java.io.File;
import java.io.IOException;

public class Application {
    public static final String DSL_FOLDER_PATH = "dsl_config/";

    public static final String CONFIGURATION_SCRIPT_PATH = DSL_FOLDER_PATH + "configuration.groovy";
    public static final String GROUP_FILE_PATH = DSL_FOLDER_PATH + "group.groovy";
    public static final String SCHEDULE_FILE_PATH = DSL_FOLDER_PATH + "schedule.groovy";
    public static final String COURSE_FILE_PATH = DSL_FOLDER_PATH + "course.groovy";
    public static final String PROGRESS_FILE_PATH = DSL_FOLDER_PATH + "progress.groovy";

    private final GroovyShell sh;

    public Application() {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        sh = new GroovyShell(Application.class.getClassLoader(), new Binding(), cc);
    }

    public static void main(String... args) throws IOException {
        var app = new Application();

        var checkerScript = new CheckerScript();

//        app.parseDto(checkerScript.getConfiguration(), CONFIGURATION_SCRIPT_PATH);
//        app.parseDto(checkerScript.getGroup(), GROUP_FILE_PATH);
//        app.parseDto(checkerScript.getSchedule(), SCHEDULE_FILE_PATH);
//        app.parseDto(checkerScript.getCourse(), COURSE_FILE_PATH);

//        var evaluator = new EvaluationRunner(checkerScript); // TODO связности шибко много??? но мб не страшно
//        evaluator.runAll();
    }

    public void parseDto(Object dto, String path) throws IOException {
        DelegatingScript script = (DelegatingScript) sh.parse(new File(path));
        script.setDelegate(dto);
        script.run();
    }
}
