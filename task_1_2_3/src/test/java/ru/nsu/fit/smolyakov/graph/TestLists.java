package ru.nsu.fit.smolyakov.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestLists {
    public static Set<Edge<String>> someGraphEdgesSet = 
        Set.of(
            new Edge<>("a", "b", 1),
            new Edge<>("b", "c", 5),
            new Edge<>("c", "a", 2),
            new Edge<>("c", "d", 4),
            new Edge<>("d", "c", 4),
            new Edge<>("d", "b", 0)
        );

    public static Set<Edge<String>> someGraphEdgesSetWithoutC = 
        Set.of(
            new Edge<>("a", "b", 1),
            new Edge<>("d", "b", 0)
        );

    public static Set<String> verticesSet() {
        var set = new HashSet<String>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add(null);

        return set;
    }

    public static Set<String> verticesSetNoC() {
        var set = verticesSet();
        set.remove("c");

        return set;
    }

    public static Set<String> verticesSetNoTerminal = 
        Set.of("a", "b", "c", "d");

    public static List<String> verticesList() {
        var list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add(null);

        return list;
    }

    public static Integer[][] someGraphMatrix = 
        new Integer[][] {
    //from\to   a     b     c     d  null
            {null,    1, null, null, null}, // a
            {null, null,    5, null, null}, // b
            {   2, null, null,    4, null}, // c
            {null,    0,    4, null, null}, // d
            {null, null, null, null, null}  // null
        };
    
    public static Map<String, Integer> shortestPathsFromC = 
        Map.of(
            "a", 2,
            "b", 3,
            "c", 0,
            "d", 4,
            "imagine i am null", Integer.MAX_VALUE
        );

    public static Set<Edge<String>> edgesAdjacentToC = 
        Set.of(
            new Edge<>("c", "a", 2),
            new Edge<>("c", "d", 4)
        );
}
