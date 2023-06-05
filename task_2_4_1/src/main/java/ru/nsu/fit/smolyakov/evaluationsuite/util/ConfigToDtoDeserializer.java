package ru.nsu.fit.smolyakov.evaluationsuite.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.fit.smolyakov.evaluationsuite.Application;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.SubjectDataDto;

import java.io.File;
import java.io.IOException;

import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.CONFIGURATION_SCRIPT_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.COURSE_FILE_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.GROUP_FILE_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.PROGRESS_FILE_PATH;
import static ru.nsu.fit.smolyakov.evaluationsuite.Constants.SCHEDULE_FILE_PATH;

public class ConfigToDtoDeserializer {
    private final GroovyShell sh;

    public ConfigToDtoDeserializer() {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        sh = new GroovyShell(Application.class.getClassLoader(), new Binding(), cc);
    }

    public static SubjectDataDto deserialize() throws IOException {
        var deserializer = new ConfigToDtoDeserializer();
        var subjectDataDto = new SubjectDataDto();

        deserializer.parseDto(subjectDataDto.getConfigurationDto(), CONFIGURATION_SCRIPT_PATH);
        deserializer.parseDto(subjectDataDto.getGroupDto(), GROUP_FILE_PATH);
        deserializer.parseDto(subjectDataDto.getScheduleDto(), SCHEDULE_FILE_PATH);
        deserializer.parseDto(subjectDataDto.getCourseDto(), COURSE_FILE_PATH);
        deserializer.parseDto(subjectDataDto.getAcademicProgressDto(), PROGRESS_FILE_PATH);

        return subjectDataDto;
    }

    private void parseDto(Object dto, String path) throws IOException {
        DelegatingScript script = (DelegatingScript) sh.parse(new File(path));
        script.setDelegate(dto);
        script.run();
    }
}
