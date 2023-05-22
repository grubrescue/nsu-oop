package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.events.task.TaskFailureResult;
import org.gradle.tooling.events.task.TaskFinishEvent;
import org.gradle.tooling.events.task.TaskSuccessResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Value
@Builder
public class GradleRunner { // TODO rename
    @Getter
    String projectPath;

    @Singular
    @Getter
    List<GradleTask> tasks;

    public record GradleTask(String name, Runnable runIfSuccess) {
    }

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
                    result.set(false);
                } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult) {
                    result.set(true);
                } else {
                    throw new RuntimeException("это што спришвается выбило" + taskFinishEvent.getResult().getClass()); //TODO
                }
            }
        };

        connection.newBuild().forTasks("clean").run(); // TODO настроить???? или не надо

        try {
            connection.newBuild()
                .forTasks(task)
                .addProgressListener(listener, OperationType.TASK)
                .run();
        } catch (BuildException ignored) {
        }

        return result.get();
    }

//    public Map<String, Boolean> run() {
//        try (var connection = GradleConnector.newConnector()
//            .forProjectDirectory(new File(projectPath))
//            .connect()) {
//
//            var resultMap = new HashMap<String, Boolean>();
//
//            tasks.forEach(task -> resultMap.put(task, runTask(connection, task)));
//
//            return resultMap;
//        }
//    }

    public void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()) {

            tasks.forEach(task -> {
                System.out.println("running task: " + task.name());
                if (runTask(connection, task.name())) {
                    task.runIfSuccess().run();
                } else {
                    System.err.println("task failed: " + task.name()); // TODO временно? надо логгер подключить
                }
            });
        }
    }
}
