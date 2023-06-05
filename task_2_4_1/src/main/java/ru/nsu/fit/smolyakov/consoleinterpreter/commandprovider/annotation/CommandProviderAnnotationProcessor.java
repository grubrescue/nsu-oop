package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.NoArgsCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandProviderAnnotationProcessor {
    public static void registerAnnotatedCommands(AbstractCommandProvider commandProvider)
        throws TypeParametersUnsupportedByAnnotationException {
        var clazz = commandProvider.getClass();

        Method[] methods = clazz.getDeclaredMethods();
        StringBuilder helpMessage
            = new StringBuilder("Help message for " + commandProvider.getRepresentation() + "\n");

        for (Method method : methods) {
            ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);
            Class<?>[] parameterTypes = method.getParameterTypes();

            boolean allAreStrings =
                Arrays.stream(parameterTypes)
                    .allMatch(parameterType -> parameterType.equals(String.class));

            if (annotation != null && !allAreStrings) {
                throw new TypeParametersUnsupportedByAnnotationException();
            }

            if (annotation != null) {
                helpMessage.append(
                    "%s[%d] - %s\n"
                        .formatted(
                            method.getName(),
                            parameterTypes.length,
                            annotation.description()
                        )
                );

                method.setAccessible(true);

                Command<String> command = new Command<>(
                    parameterTypes.length,
                    args -> {
                        try {
                            method.invoke(
                                commandProvider,
                                (Object[]) args.toArray(new String[parameterTypes.length])
                            );
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new InternalCommandException(e.getCause().getMessage());
                        }
                    }
                );

                commandProvider.registerCommand(method.getName(), command);
            }
        }

        helpMessage.append("help[0] - show this message\n");
        helpMessage.append("done[0] - go block down\n");
        helpMessage.append("exit[0] - exit the application\n\n");

        commandProvider.registerCommand(
            "help",
            new NoArgsCommand<>(() -> System.out.println(helpMessage))
        );
    }
}
