package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.command.Command;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception.MultipleNameDefinitionAnnotationException;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception.UnsupportedSignatureAnnotationException;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception.UnsupportedTypeParametersException;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Annotation processor for {@link ConsoleCommand} annotation.
 * Registers annotated methods as commands in given command provider.
 *
 * <p>As an additional feature, it generates help message for given command provider,
 * basing on the signature of annotated methods and a {@link ConsoleCommand#description()}.
 */
public class CommandProviderAnnotationProcessor {
    private final AbstractCommandProvider commandProvider;
    private final List<MethodAnnotationPair> methodAnnotationPairList
        = new ArrayList<>();

    /**
     * Constructs a new annotation processor on a corresponding {@code commandProvider}.
     *
     * @param commandProvider command provider to register commands in
     *
     * @throws MultipleNameDefinitionAnnotationException if some methods in a class has the same name
     *          (unfortunately, for now this library does not support polymorphism...)
     * @throws UnsupportedSignatureAnnotationException if some or all parameters are not {@link String}s.
     */
    public CommandProviderAnnotationProcessor(@NonNull AbstractCommandProvider commandProvider) {
        this.commandProvider = commandProvider;
        registerAnnotatedCommands();
    }

    private void registerAnnotatedCommands() {
        var clazz = commandProvider.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);

            if (annotation == null) {
                return;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            boolean allAreStrings =
                Arrays.stream(parameterTypes)
                    .allMatch(parameterType -> parameterType.equals(String.class));

            boolean noMoreNameOccurrences =
                methodAnnotationPairList.stream()
                    .map(MethodAnnotationPair::method)
                    .map(Method::getName)
                    .noneMatch(name -> method.getName().equals(name));

            if (!allAreStrings) {
                throw new UnsupportedSignatureAnnotationException();
            }

            if (!noMoreNameOccurrences) {
                throw new UnsupportedTypeParametersException();
            }

            methodAnnotationPairList.add(new MethodAnnotationPair(method, annotation));
        }
    }

    private record MethodAnnotationPair(
        Method method,
        ConsoleCommand annotation
    ) {};

    /**
     * Generates help message according to annotated methods' {@link ConsoleCommand#description()}.
     *
     * @return a help message
     */
    public String generateHelpMessage() {
        StringBuilder helpMessage
            = new StringBuilder();

        var nameFieldLength = methodAnnotationPairList.stream()
            .map(MethodAnnotationPair::method)
            .map(Method::getName)
            .map(String::length)
            .max(Integer::compare)
            .orElse(0);

        methodAnnotationPairList
            .forEach(methodAnnotationPair ->
                helpMessage.append(
                    "%%%d.%d%s[%d] :: %s\n"
                        .formatted(
                            nameFieldLength,
                            nameFieldLength,
                            methodAnnotationPair.method.getName(),
                            methodAnnotationPair.method.getParameterTypes().length,
                            methodAnnotationPair.annotation.description()
                        )
                )
            );

        return helpMessage.toString();
    }

    /**
     * Generates commands map according to annotated methods signature.
     *
     * @return a commands map
     */
    public Map<String, Command<String>> generateCommandsMap() {
        var map = new HashMap<String, Command<String>>(methodAnnotationPairList.size());

        methodAnnotationPairList.stream()
            .map(MethodAnnotationPair::method)
            .forEach(method -> {
                Command<String> command
                    = new Command<>(
                        method.getParameterTypes().length,
                        args -> {
                            try {
                                method.invoke(
                                    commandProvider,
                                    (Object[]) args.toArray(new String[method.getParameterTypes().length])
                                );
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new InternalCommandException(e.getCause().getMessage());
                            }
                        }
                    );

                map.put(method.getName(), command);
            });

        return map;
    }
}
