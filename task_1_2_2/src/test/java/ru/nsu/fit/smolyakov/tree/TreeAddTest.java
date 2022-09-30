package ru.nsu.fit.smolyakov.tree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


class TreeAddTest {
    @Test
    void oneItemTreesAddNodeTest() {
        var oneTree = new Tree<Integer>();
        oneTree.addNode(3);
        var anotherTree = new Tree<Integer>();
        anotherTree.addNode(3);

        assertThat(oneTree).isEqualTo(anotherTree);

        var thirdTree = new Tree<Integer>();
        thirdTree.addNode(4);
        assertThat(oneTree).isNotEqualTo(thirdTree);
        assertThat(anotherTree).isNotEqualTo(thirdTree);
    }
    
    @ParameterizedTest
    @MethodSource("ru.nsu.fit.smolyakov.tree.TestLists#someUsualLists")
    void manyRootSonsAddNodeTest(List<String> list) {
        var oneTree = new Tree<String>();
        var anotherTree = new Tree<String>();
        var thirdTree = new Tree<String>(list);


        for (var val : list) {
            oneTree.addNode(val);
            assertThat(oneTree).isNotEqualTo(anotherTree);
            anotherTree.addNode(val);
            assertThat(oneTree).isEqualTo(anotherTree);
        }

        assertThat(oneTree).isEqualTo(thirdTree);
    }

    @Test
    void deepTreeAddNodeTest() {
        var oneTree = new Tree<Integer>();
        var anotherTree = new Tree<Integer>();

        var node1 = oneTree.addNode(4);
        assertThat(oneTree).isNotEqualTo(anotherTree);
        var node2 = anotherTree.addNode(4);
        assertThat(oneTree).isEqualTo(anotherTree);

        var node3 = oneTree.addNode(node1, 5);
        assertThat(oneTree).isNotEqualTo(anotherTree);
        var node4 = anotherTree.addNode(node2, 5);
        assertThat(oneTree).isEqualTo(anotherTree);

        oneTree.addNode(node1, 6);
        assertThat(oneTree).isNotEqualTo(anotherTree);
        anotherTree.addNode(node2, 6);
        assertThat(oneTree).isEqualTo(anotherTree);

        oneTree.addNode(node3, 7);
        assertThat(oneTree).isNotEqualTo(anotherTree);
        anotherTree.addNode(node4, 7);
        assertThat(oneTree).isEqualTo(anotherTree);
    }

    @Test 
    void nullAddTest() {
        var tree = new Tree<Double>();
        assertThatThrownBy(() -> tree.add(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test 
    void wrongTreeAddTest() {
        var tree = new Tree<String>();
        var nodeFromFirstTree = tree.addNode("my successor wants to go to another tree((");
        var secondTree = new Tree<String>();

        assertThatThrownBy(() -> secondTree.addNode(nodeFromFirstTree, "i'm finally free"))
            .isInstanceOf(IllegalArgumentException.class);
    }
} 
