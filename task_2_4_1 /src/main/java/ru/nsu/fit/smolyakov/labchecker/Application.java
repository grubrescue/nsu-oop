package ru.nsu.fit.smolyakov.labchecker;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.fit.smolyakov.labchecker.entity.Checker;

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

        var checker = new Checker();

        app.parseDto(checker.getConfiguration(), CONFIGURATION_SCRIPT_PATH);
        app.parseDto(checker.getGroup(), GROUP_FILE_PATH);
        app.parseDto(checker.getSchedule(), SCHEDULE_FILE_PATH);
        app.parseDto(checker.getCourse(), COURSE_FILE_PATH);

        System.out.println(checker);
    }

    public void parseDto(Object dto, String path) throws IOException {
        DelegatingScript script = (DelegatingScript) sh.parse(new File(path));
        script.setDelegate(dto);
        script.run();
    }
}
