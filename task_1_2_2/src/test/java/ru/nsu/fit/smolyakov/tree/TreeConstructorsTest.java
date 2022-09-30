package ru.nsu.fit.smolyakov.tree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TreeConstructorsTest {
    @Test
    void valueConstructorTest() {
        var noArgsTree = new Tree<Double>();
        noArgsTree.add(3.14);

        var valueArgTree = new Tree<Double>(3.14);

        assertThat(noArgsTree).isEqualTo(valueArgTree);
    }

    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.tree.TestLists#someUsualLists")
    void collectionConstructorTest(List<String> list) {
        var collectionArgTree = new Tree<String>(list);

        var noArgsTree = new Tree<String>();
        list.stream()
            .forEachOrdered(noArgsTree::add);

        assertThat(noArgsTree).isEqualTo(collectionArgTree);
    }

    @Test
    void nullConstructorTest() {
        assertThatThrownBy(() -> new Tree<Object>(null))
            .isInstanceOf(IllegalArgumentException.class);
    }    
}
