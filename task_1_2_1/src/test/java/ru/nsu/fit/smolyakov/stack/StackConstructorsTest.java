package ru.nsu.fit.smolyakov.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class StackConstructorsTest {
    @Test
    void nonSpecifiedCapacityConstructorTest() {
        new Stack<Integer>();
    }


    @Test
    void specifiedCapacityConstructorTest() {
        new Stack<Object>(100500);
    }

    @ParameterizedTest
    @ValueSource(ints = { Integer.MIN_VALUE, -1, 0 })
    void incorrectCapacityConstructorTest(int capacity) {
        assertThatThrownBy(() -> new Stack<>(capacity))
        .isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void fromArrayConstructorTest(List<String> list) {
        var arr = (String[]) list.toArray(new String[list.size()]);

        var stackToPush = new Stack<>();
        var stackFromArray = new Stack<String>(arr);

        list.stream().forEachOrdered(stackToPush::push);
        assertThat(stackFromArray).isEqualTo(stackToPush);
        // equals() is tested separately
    }


    @Test
    void fromNullConstructorTest() {
        assertThatThrownBy(() -> new Stack<>(null))
        .isInstanceOf(IllegalArgumentException.class);
    }
}