package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.events.task.TaskFailureResult;
import org.gradle.tooling.events.task.TaskFinishEvent;
import org.gradle.tooling.events.task.TaskSuccessResult;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class GradleRunner { // TODO rename
    @Getter
    private String projectPath;

    /**
     *
     * @param connection
     * @param task
     * @return true on success
     */
    private boolean runTask(ProjectConnection connection, String task) {
        AtomicBoolean result = new AtomicBoolean(false);
        ProgressListener listener = progressEvent -> {
            if (progressEvent instanceof TaskFinishEvent taskFinishEvent) {
                var actualTaskName = taskFinishEvent.getDescriptor().getName().substring(1);

                if (!actualTaskName.equals(task)) {
                    return;
                }

                if (taskFinishEvent.getResult() instanceof TaskFailureResult) {
                    result.set(true);
                } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult) {
                    result.set(false);
                } else {
                    throw new RuntimeException("это што спришвается выбило" + taskFinishEvent.getResult().getClass()); //TODO
                }
            }
        };

        connection.newBuild().forTasks("clean").run();

        try {
            connection.newBuild()
                .forTasks(task)
                .addProgressListener(listener, OperationType.TASK)
                .run();
        } catch (BuildException ignored) {
        }

        return result.get();
    }

    public Map<String, Boolean> run(String... tasks) {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()) {

            var resultMap = new HashMap<String, Boolean>();

            Arrays.stream(tasks)
                .forEach(task -> resultMap.put(task, runTask(connection, task)));

            return resultMap;
        }
    }
}
