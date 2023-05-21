package ru.nsu.fit.smolyakov.labchecker.checker;


import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.task.TaskFailureResult;
import org.gradle.tooling.events.task.TaskFinishEvent;
import org.gradle.tooling.events.task.TaskSuccessResult;

import java.io.File;

public class GradleRunner { // TODO rename
    public static void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File("../task_1_1_1/"))
            .connect()) {

            ProgressListener listener = progressEvent -> {
                if (progressEvent instanceof TaskFinishEvent taskFinishEvent) {
                    System.out.println(taskFinishEvent.getDescriptor().getName());

                    if (taskFinishEvent.getResult() instanceof TaskFailureResult taskFailureResult) {
                        System.out.println("FAIL");
                    } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult taskSuccessResult) {
                        System.out.println("SUCC");
                    } else {
                        System.out.println(taskFinishEvent.getClass());
                    }
                }
            };



            connection.newBuild()
                .forTasks("clean", "build", "javadoc", "test")
                .addProgressListener(listener, OperationType.TASK)
                .run();
//            connection.
//            connection.newBuild()
        }
    }
}
