package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandProviderAnnotationProcessor {
    public static void annotate(AbstractCommandProvider commandProvider) {
        var clazz = commandProvider.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .forEach((method -> {
//                System.out.println(method.getName());
                ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);
                Class<?>[] parameterTypes = method.getParameterTypes();

                boolean allAreStrings =
                    Arrays.stream(parameterTypes)
                        .allMatch(parameterType -> parameterType.equals(String.class));

                if (annotation != null && !allAreStrings) {
                    throw new IllegalArgumentException("Only strings are allowed");
                }

                if (annotation != null) {
                    method.setAccessible(true);
                    commandProvider.registerCommand(
                        method.getName(),
                        new Command<>(
                            parameterTypes.length,
                            args -> {
                                try {
                                    method.invoke(commandProvider, (Object[]) args.toArray(new String[parameterTypes.length]));
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        )
                    );
                }
            }));
    }
}
