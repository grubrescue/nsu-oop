package ru.nsu.fit.smolyakov.evaluationsuite;

/**
 * Constants used in the project.
 */
public class Constants {
    /**
     * Path to the folder with DSL scripts.
     */
    public static final String DSL_FOLDER_PATH = "dsl_config/";

    /**
     * Path to the configuration script.
     */
    public static final String CONFIGURATION_SCRIPT_PATH = DSL_FOLDER_PATH + "configuration.groovy";

    /**
     * Path to the group script.
     */
    public static final String GROUP_FILE_PATH = DSL_FOLDER_PATH + "group.groovy";

    /**
     * Path to the schedule script.
     */
    public static final String SCHEDULE_FILE_PATH = DSL_FOLDER_PATH + "schedule.groovy";

    /**
     * Path to the course script.
     */
    public static final String COURSE_FILE_PATH = DSL_FOLDER_PATH + "course.groovy";

    /**
     * Path to the progress script.
     */
    public static final String PROGRESS_FILE_PATH = DSL_FOLDER_PATH + "progress.groovy";

    /**
     * Path to the dump file.
     */
    public static final String DUMP_FILE_PATH = "dumps/dump";

    /**
     * Path to the backup dump file.
     */
    public static final String DUMP_FILE_BACKUP_PATH = DUMP_FILE_PATH + ".bak";

    /**
     * Path to the folder with HTML tables.
     */
    public static final String HTML_TABLES_PATH = "html/";

    /**
     * Path to the HTML table with evaluation information.
     */
    public static final String HTML_EVALUATION = HTML_TABLES_PATH + "evaluation.html";

    /**
     * Path to the HTML table with attendance information.
     */
    public static final String HTML_ATTENDANCE = HTML_TABLES_PATH + "attendance.html";
}
