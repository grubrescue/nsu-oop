package ru.nsu.fit.smolyakov.evaluationsuite.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for working with source files.
 */
public class SourcesUtil {
    /**
     * Returns a list of all source files in the given directory.
     *
     * @param rootPathUri root directory URI
     * @return list of source files
     */
    public static List<File> allSourceFilesList(String rootPathUri) {
        try (Stream<Path> tree = Files.walk(Path.of(rootPathUri))) {
            return tree.filter(file -> file.getFileName().toString().endsWith(".java"))
                .map(path -> new File(path.toUri()))
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
