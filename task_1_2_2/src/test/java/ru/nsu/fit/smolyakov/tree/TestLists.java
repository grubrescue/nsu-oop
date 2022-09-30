package ru.nsu.fit.smolyakov.tree;

import java.util.List;
import java.util.stream.Stream;

class TestLists {
    static Stream<List<String>> someUsualLists() {
        return Stream.of(
            List.of(
                "these", "are", "some", "strings", 
                "for", "the", "test", "of", "the",
                "t", "r", "e", "e", "!!!"
            ),
            List.of(
                "1", "4", "0", "5", "d",
                "a", "a", "a", "a", "a"
            )
        );
    }

    static Tree<String> someUsualTree() {
        var someTree = new Tree<>(List.of("these", "are", "first", "level", "children"));
        var son1 = someTree.addNode("are");
        var son2 = someTree.addNode("dont forget about son2...");

        someTree.addNode(son2, "who is son2");
        someTree.addNode(son2, "idk");
        var son1_1 = someTree.addNode(son1, "me too!");
        someTree.addNode(son1_1, "-999 social credit");
        someTree.addNode(son1_1, "are");

        return someTree;
    }

    static List<String> dfsOrderForSomeUsualTree() {
        return List.of(
            "these",
            "are",
            "first",
            "level",
            "children",
            "-999 social credit",
            "are",
            "me too!",
            "are",
            "who is son2",
            "idk",
            "dont forget about son2..."
        );
    }

    static List<String> dfsOrderForSomeUsualTreeWithoutAre() {
        return List.of(
            "these",
            "first",
            "level",
            "children",
            "who is son2",
            "idk",
            "dont forget about son2..."
        );
    }

    static List<String> bfsOrderForSomeUsualTree() {
        return List.of(
            "these",
            "are",
            "first",
            "level",
            "children",
            "are",
            "dont forget about son2...",
            "me too!",
            "who is son2",
            "idk",
            "-999 social credit",
            "are"
        );
    }

    static List<String> bfsOrderForSomeUsualTreeWithoutAre() {
        return List.of(
            "these",
            "first",
            "level",
            "children",
            "dont forget about son2...",
            "who is son2",
            "idk"
        );
    }
}
