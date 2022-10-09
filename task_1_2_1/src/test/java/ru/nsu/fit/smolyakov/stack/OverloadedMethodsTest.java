package ru.nsu.fit.smolyakov.stack;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class OverloadedMethodsTest {
    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void equalsTest(List<String> list) {
        var someStack = new Stack<String>();
        var anotherStack = new Stack<String>();

        list.stream().forEachOrdered(someStack::push);
        list.stream().forEachOrdered(anotherStack::push);

        assertThat(someStack).isEqualTo(anotherStack);


        someStack.pop();
        someStack.push("value that anotherStack doesn't contain");
        assertThat(someStack).isNotEqualTo(anotherStack);

        someStack.pop(); // different size
        assertThat(someStack).isNotEqualTo(anotherStack);

        someStack.push(null);
        assertThat(someStack).isNotEqualTo(anotherStack);
    }

    @Test
    void equalsNullAndEmptyTest() {
        var someStack = new Stack<String>();
        assertThat(someStack).isNotEqualTo(null);

        var anotherStack = new Stack<String>();
        assertThat(someStack).isEqualTo(anotherStack);

        someStack.push(null);
        assertThat(someStack).isNotEqualTo(anotherStack);

        anotherStack.push(null);
        assertThat(someStack).isEqualTo(anotherStack);
    }

    @Test
    void equalsDifferentClass() {
        assertThat(new Stack<String>()).isNotEqualTo(new Object());
    }

    @Test 
    void equalsTheSameObject() {
        var stack = new Stack<Object>();
        assertThat(stack).isEqualTo(stack);
    }


    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualStacks")
    void cloneTest(Stack<String> someStack) {
        Stack<String> clonedStack = someStack.clone();
        assertThat(someStack).isEqualTo(clonedStack);
    }

    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void ensureCapacityTest(List<String> list) {
        Stack<String> someStack = new Stack<>();
        Stack<String> anotherStack = new Stack<>();

        someStack.ensureCapacity(666);
        list.stream()
            .peek(someStack::push)
            .peek(anotherStack::push);

        assertThat(someStack).isEqualTo(anotherStack);
    }
}