package ru.nsu.fit.smolyakov.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


class StackMethodsTest {
    Stack<String> stack;    

    @BeforeEach
    void newStack() {
        stack = new Stack<>();
    }
    
    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void pushPeekSizePopUsualTest(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            var elem = list.get(i);

            stack.push(elem);
            assertThat(stack.peek().get()).isEqualTo(elem);
            assertThat(stack.size()).isEqualTo(i+1);
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            var elem = list.get(i);

            assertThat(stack.pop().get()).isEqualTo(elem);
            assertThat(stack.size()).isEqualTo(i);
        }
    }

    @Test
    void pushPeekPopNullTest() {
        stack.push(null);
        assertThat(stack.peek()).isEqualTo(Optional.empty());
        assertThat(stack.pop()).isEqualTo(Optional.empty());
        assertThat(stack.pop()).isEqualTo(Optional.empty());
    }

    @Test
    void isEmptyTest() {
        assertThat(stack.isEmpty()).isTrue();
        stack.push(TestLists.lonelyString);
        assertThat(stack.isEmpty()).isFalse();
        stack.pop();
        assertThat(stack.isEmpty()).isTrue();
    }


    @Test
    void popPeekProblematicTest() {
        assertThat(stack.peek()).isEqualTo(Optional.empty());
        assertThat(stack.pop()).isEqualTo(Optional.empty());

        stack.push(TestLists.lonelyString);

        assertThat(stack.peek()).isEqualTo(Optional.of(TestLists.lonelyString));
        assertThat(stack.pop()).isEqualTo(Optional.of(TestLists.lonelyString));

        stack.pop();

        assertThat(stack.peek()).isEqualTo(Optional.empty());
        assertThat(stack.pop()).isEqualTo(Optional.empty());
    }


    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualStacks")
    void pushStackTest(Stack<String> anotherStack) {
        stack.pushStack(anotherStack);
        assertThat(stack).isEqualTo(anotherStack);
    }

    @Test
    void pushStackNullArgTest() {
        assertThatThrownBy(() -> stack.pushStack(null))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualStacks")
    void popStackTest(Stack<String> anotherStack) {
        stack.pushStack(anotherStack);

        stack.popStack(0);
        assertThat(stack).isEqualTo(anotherStack);

        var thirdStack = stack.popStack(1);
        assertThat(thirdStack.peek()).isEqualTo(anotherStack.pop());

        assertThat(stack).isEqualTo(anotherStack);

        stack.popStack(100500);
        assertThat(stack.size()).isEqualTo(0);
    }

    @Test
    void incorrectPopStackTest() {
        assertThatThrownBy(() -> stack.popStack(-9000))
        .isInstanceOf(IllegalArgumentException.class);
    }
}