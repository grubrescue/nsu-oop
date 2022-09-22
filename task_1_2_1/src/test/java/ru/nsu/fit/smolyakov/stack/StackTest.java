package ru.nsu.fit.smolyakov.stack;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


class StackConstructorsTest {
    @Test
    void specifiedCapacityConstructorTest () {
        new Stack<Object>(100500);
    }

    @Test
    void nonSpecifiedCapacityConstructorTest () {
        new Stack<Integer>();
    }

    @ParameterizedTest
    @ValueSource(ints = { Integer.MIN_VALUE, -1, 0})
    void incorrectCapacityConstructorTest (int capacity) {
        assertThatThrownBy(() -> new Stack<>(capacity))
        .isInstanceOf(IllegalArgumentException.class);
    }
}


class TestLists {
    static Stream<List<String>> someUsualLists() {
        return Stream.of(
            List.of(
                "these", "are", "some", "strings", 
                "for", "the", "test", "of", "the",
                "s", "t", "a", "c", "k", "!!!"
            ),
            List.of(
                "1", "4", "0", "5"
            )
        );
    }
} 


class StackMethodsTest {
    Stack<String> stack;

    @BeforeEach
    void newStack() {
        stack = new Stack<>();
    }

    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void countPushPopPeekNormalTest(List<String> list) {
        list.stream()
            .forEachOrdered(stack::push);

        assertThat(stack.count()).isEqualTo(list.size());
        assertThat(stack.peek().get()).isEqualTo(list.get(list.size()-1));
        assertThat(stack.pop().get()).isEqualTo(list.get(list.size()-1));

        assertThat(stack.count()).isEqualTo(list.size()-1);
        assertThat(stack.peek().get()).isEqualTo(list.get(list.size()-2));
        assertThat(stack.pop().get()).isEqualTo(list.get(list.size()-2));

        assertThat(stack.count()).isEqualTo(list.size()-2);
    }

    @Test
    void pushPopPeekProblematicTest() {
        
    }

    @Test
    void isEmptyTest() {

    }

}