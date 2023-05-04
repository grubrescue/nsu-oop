package ru.nsu.fit.smolyakov.snakegame.model;

import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.level.*;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a barrier that is located on {@link GameModel}.
 * The snake cannot pass through the barrier.
 */
public record Barrier(Set<Point> barrierPoints) {
    public static Barrier fromProperties(GameProperties properties) {
        Set<Point> points;

//        if (properties.level() instanceof BorderLevel borderLevel) {
//            points = points(properties, borderLevel);
//        } else if (properties.level() instanceof EmptyLevel emptyLevel) {
//            points = points(properties, emptyLevel);
//        } else if (properties.level() instanceof RandomLevel randomLevel) {
//            points = points(properties, randomLevel);
//        } else if (properties.level() instanceof CustomFileLevel customFileLevel) {
//            points = points(properties, customFileLevel);
//        } else {
//            throw new IllegalArgumentException("Unknown level type");
//        }

        try {
            Method method = Barrier.class.getDeclaredMethod("points", GameProperties.class, properties.level().getClass());
            points = (Set<Point>) method.invoke(null, properties, properties.level());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw new RuntimeException(e);
        }

        return new Barrier(points);
    }

    private static Set<Point> points(GameProperties properties, EmptyLevel level) {
        throw new IllegalArgumentException("empty level");
    }

    private static Set<Point> points(GameProperties properties, RandomLevel level) {
        throw new IllegalArgumentException("random level");
    }

    private static Set<Point> points(GameProperties properties, BorderLevel level) {
        return null;
    }


    private static Set<Point> points(GameProperties properties, CustomFileLevel level) {
        Set<Point> points = new HashSet<>();

        System.out.println(level.getFileName());
        GameData.INSTANCE.levelFileScanner(level.getFileName()).ifPresent(
            scanner -> {
                for (int y = 0; y < properties.height() && scanner.hasNextLine(); y++) {
                    String line = scanner.nextLine();

                    var columns = Integer.min(line.length(), properties.width());
                    for (int x = 0; x < columns; x++) {
                        if (line.charAt(x) == '*') {
                            points.add(new Point(x, y));
                        }
                    }
                }
            }
        );

        return points;
    }


    /**
     * Checks if the point is located on the barrier.
     *
     * @param point point
     * @return {@code true} if the point is located on the barrier, false otherwise
     */
    public boolean met(Point point) {
        return barrierPoints.contains(point);
    }
}
