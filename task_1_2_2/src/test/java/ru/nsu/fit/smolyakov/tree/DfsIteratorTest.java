package ru.nsu.fit.smolyakov.tree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DfsIteratorTest {
    Tree<String> tree;
    Iterator<String> iter;

    @BeforeEach
    void init() {
        tree = TestLists.someUsualTree();
        iter = tree.dfsIterator();
    }

    @Test
    void dfsRunTest() {
        int i = 0;
        while (iter.hasNext()) {
            var val = iter.next();

            assertThat(val)
                .isEqualTo(TestLists.dfsOrderForSomeUsualTree().get(i++));
        }

        assertThat(TestLists.dfsOrderForSomeUsualTree().size()).isEqualTo(i);
    }

    @Test
    void removeTest() {
        var list = new ArrayList<String>();

        while (iter.hasNext()) {
            var val = iter.next();

            if (val == "are") {
                iter.remove();
            } 
        }

        iter = tree.dfsIterator();

        while (iter.hasNext()) {
            list.add(iter.next());
        }

        assertThat(list)
            .isEqualTo(TestLists.dfsOrderForSomeUsualTreeWithoutAre());
    }

    @Test
    void nextWithoutCheckTest() {
        while (iter.hasNext()) {
            iter.next();
        }

        assertThatThrownBy(() -> iter.next())
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void removeWithoutNextTest() {
        assertThatThrownBy(() -> iter.remove())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void multipleRemoveTest() {
        iter.next();
        iter.remove();

        assertThatThrownBy(() -> iter.remove())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void concurrentModificationExceptionTest() {
        // modern computers operate so fast that
        // iterator creation and last modification
        // may happen at the same millisecond
        // idle cicle fixes this issue
        long a = 1;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            a += 1;
        }
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            a += 1;
        }
        System.out.println(a);

        tree.addNode("tree won't work now");

        assertThatThrownBy(() -> iter.next())
            .isInstanceOf(ConcurrentModificationException.class);
    }
}
