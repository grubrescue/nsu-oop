package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Console command annotation.
 * Used to mark methods as console commands.
 * Methods marked with this annotation may have any arity in range of 0 and infinity,
 * but only {@link String} arguments.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsoleCommand {
    /**
     * Description of command.
     *
     * @return description of command
     */
    String description() default "no description, sorry";
}
