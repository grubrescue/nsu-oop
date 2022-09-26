package ru.nsu.fit.smolyakov.stack;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


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

    static String lonelyString = "haha steak is not empty haha";
} 


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
}


class StackMethodsTest {
    Stack<String> stack;    

    @BeforeEach
    void newStack() {
        stack = new Stack<>();
    }
    
    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.stack.TestLists#someUsualLists")
    void pushPeeksizePopUsualTest(List<String> list) {
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