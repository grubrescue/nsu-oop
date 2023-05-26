package ru.nsu.fit.smolyakov.evaluationsuite;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.SubjectDataDto;
import ru.nsu.fit.smolyakov.evaluationsuite.util.SubjectDataDtoToEntity;

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

        var checkerScript = new SubjectDataDto();

        app.parseDto(checkerScript.getConfigurationDto(), CONFIGURATION_SCRIPT_PATH);
        app.parseDto(checkerScript.getGroupDto(), GROUP_FILE_PATH);
        app.parseDto(checkerScript.getScheduleDto(), SCHEDULE_FILE_PATH);
        app.parseDto(checkerScript.getCourseDto(), COURSE_FILE_PATH);
        app.parseDto(checkerScript.getAcademicProgressDto(), PROGRESS_FILE_PATH);

        var subjectData = new SubjectDataDtoToEntity(checkerScript).convert();

//        var a = GradleRunner.builder()
//            .projectPath(".checks_tmp/1684779963745/streafy/task_1_1_1")
//            .task(new GradleRunner.GradleTask("build", () -> {
//                System.out.println("success");
//            }))
//            .build();
//        a.run();
//
//        subjectData.getGroup()
//            .getStudentList()
//            .stream()
//            .map(Evaluator::new)
//            .forEach(Evaluator::evaluate);




//        SubjectDataEntitySerializer.serialize(subjectData, "privet.dat");

//        var entity = SubjectDataEntitySerializer.deserialize("privet.dat");
//        var presenter = new EvaluationPresenter(entity);
//
//
//
//        presenter.printEvaluation(new ConsoleTablePrinter());
//        presenter.printAttendance(new ConsoleTablePrinter());
//        presenter.printEvaluation(new HtmlTablePrinter("privet.html"));
//        presenter.printAttendance(new HtmlTablePrinter("privet2.html"));

//        var parser = JacocoReportParser.parse("../task_1_1_1/build/reports/jacoco/test/jacocoTestReport.xml");
//        parser.getCoverageByType(JacocoReportParser.CounterType.INSTRUCTION)
//            .ifPresentOrElse(
//                System.out::println,
//                () -> System.out.println("No such type"));
    }

    public void parseDto(Object dto, String path) throws IOException {
        DelegatingScript script = (DelegatingScript) sh.parse(new File(path));
        script.setDelegate(dto);
        script.run();
    }
}
