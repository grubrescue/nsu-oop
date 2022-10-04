package ru.nsu.fit.smolyakov.stack;

import java.util.List;
import java.util.stream.Stream;

class TestLists {
    static Stream<List<String>> someUsualLists() {
        return Stream.of(
            List.of(
                "these", "are", "some", "strings", 
                "for", "the", "test", "of", "the",
                "s", "t", "a", "c", "k", "!!!"
            ),
            List.of(
                "1", "4", "0", "5", "d"
            )
        );
    }


    static Stream<Stack<String>> someUsualStacks() {
        return someUsualLists()
            .map((list) -> list.toArray(new String[list.size()]))
            .map((arr) -> new Stack<String>(arr));
    }

    static String lonelyString = ":<";
}