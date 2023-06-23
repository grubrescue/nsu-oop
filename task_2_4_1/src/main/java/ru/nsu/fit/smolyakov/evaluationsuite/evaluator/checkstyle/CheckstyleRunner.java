package ru.nsu.fit.smolyakov.evaluationsuite.evaluator.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.JavaParser;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import ru.nsu.fit.smolyakov.evaluationsuite.Constants;
import ru.nsu.fit.smolyakov.evaluationsuite.util.SourcesUtil;

import java.util.Properties;

/**
 * A class to run checkstyle.
 */
@Getter
@Log4j2
public class CheckstyleRunner { // TODO rename
    public CheckstyleRunner(@NonNull String projectPath) {
        this.projectPath = projectPath;
    }

    private final String projectPath;

    /**
     * Runs checkstyle.
     *
     * @return true on success
     */
    public CheckstyleResult runCheckstyle() {
        log.info("Running checkstyle for {}", projectPath);

        var codeStyleChecker = new Checker();
        var result = new CheckstyleResult();
        try {
            Configuration config = ConfigurationLoader.loadConfiguration(
                Constants.CHECKSTYLE_CONFIGURATION,
                new PropertiesExpander(new Properties())
            );
            codeStyleChecker.setLocaleLanguage("en");
            codeStyleChecker.setModuleClassLoader(JavaParser.class.getClassLoader());
            codeStyleChecker.configure(config);
            codeStyleChecker.addListener(new CheckstyleAuditListener(result));

            codeStyleChecker.process(SourcesUtil.allSourceFilesList(projectPath));

            log.info("Checkstyle finished successfully");
            return result;
        } catch (CheckstyleException e) {
            log.fatal("Checkstyle failed; cause: {}", e.getMessage());
            return new CheckstyleResult();
        }
    }
}