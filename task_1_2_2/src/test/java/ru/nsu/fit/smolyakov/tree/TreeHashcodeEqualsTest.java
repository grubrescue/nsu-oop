package ru.nsu.fit.smolyakov.tree;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TreeHashcodeEqualsTest {
    // more equals tests are connected to add tests
    @Test
    void emptyTreesTest() {
        var oneTree = new Tree<Object>();
        var anotherTree = new Tree<Object>();

        assertThat(oneTree.hashCode()).isEqualTo(anotherTree.hashCode());
        assertThat(oneTree).isEqualTo(anotherTree);
    }

    @Test 
    void deepTreesTest() {
        var oneTree = TestLists.someUsualTree();
        var anotherTree = TestLists.someUsualTree();

        assertThat(oneTree.hashCode()).isEqualTo(anotherTree.hashCode());
        assertThat(oneTree).isEqualTo(anotherTree);
    }

    @Test
    void sameObjEquals() {
        var tree = new Tree<Object>();
        assertThat(tree).isEqualTo(tree);
    }

    @Test
    void nullObjEquals() {
        assertThat(new Tree<Double>()).isNotEqualTo(null);
    }

    @Test
    void wrongObjClassEquals() {
        assertThat(new Tree<Object>()).isNotEqualTo(new Object());
    }
}
