package ru.nsu.fit.smolyakov.tree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TreeCollectionMethodsTest {
    Tree<String> tree;

    @BeforeEach
    void init() {
        tree = TestLists.someUsualTree();
    }

    @Test
    void removeExistTest() {
        boolean res = tree.remove("are");

        var list = tree.stream()
                       .collect(Collectors.toList());   

        assertThat(res).isTrue();
        assertThat(list).isEqualTo(TestLists.bfsOrderForSomeUsualTreeWithoutAre());
    }

    @Test
    void removeNonexistTest() {
        boolean res = tree.remove("item that is not contained in this tree");

        assertThat(res).isFalse();
        assertThat(tree).isEqualTo(TestLists.someUsualTree());
    }

    @Test
    void removeNullTest() {
        assertThat(tree.remove(null)).isFalse();
    }

    @Test
    void removeAllTest() {
        tree.removeAll(List.of("are", "item that is not contained in this tree"));

        var list = tree.stream()
                       .collect(Collectors.toList());

        assertThat(list).isEqualTo(TestLists.bfsOrderForSomeUsualTreeWithoutAre());

        tree.removeAll(List.of("item that is not contained in this tree"));
    }

    @Test
    void removeAllTree() {
        tree.removeAll(TestLists.bfsOrderForSomeUsualTree());
        assertThat(tree).isEmpty();
    }

    @Test
    void removeAllNullTest() {
        assertThatThrownBy(() -> tree.removeAll(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void retainsAllTest() {
        boolean res = tree.retainAll(List.of("are", "item that is not contained in this tree"));

        var list = tree.stream()
                       .collect(Collectors.toList());

        assertThat(res).isTrue();
        assertThat(list).isEqualTo(List.of("are", "are"));
    }

    @Test
    void retainAllNullTest() {
        assertThatThrownBy(() -> tree.retainAll(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isEmptySizeTest() {
        var someTree = new Tree<Integer>();

        assertThat(someTree.isEmpty()).isTrue();
        assertThat(someTree.size()).isEqualTo(0);

        someTree.add(666);

        assertThat(someTree.isEmpty()).isFalse();
        assertThat(someTree.size()).isEqualTo(1);

        someTree.remove(666);

        assertThat(someTree.isEmpty()).isTrue();
        assertThat(someTree.size()).isEqualTo(0);
    }

    @Test
    void containsTest() {
        assertThat(tree.contains("are")).isTrue();
        assertThat(tree.contains("item that is not contained in this tree")).isFalse();
    }

    @Test
    void containsNullTest() {
        assertThat(tree.contains(null)).isFalse();
    }

    @Test
    void containsAllTest() {
        assertThat(tree.containsAll(List.of("-999 social credit", "idk"))).isTrue();
        assertThat(tree.containsAll(TestLists.bfsOrderForSomeUsualTree())).isTrue();
        assertThat(tree.containsAll(
            List.of("-999 social credit", "item that is not contained in this tree"))
        ).isFalse();
    }

    @Test
    void containsAllNullTest() {
        assertThatThrownBy(() -> tree.containsAll(null))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void clearTest() {
        assertThat(tree.isEmpty()).isFalse();

        tree.clear();

        assertThat(tree.isEmpty()).isTrue();
        assertThat(tree.contains("are")).isFalse();
    }

    @Test 
    void toArrayTest() {
        Object[] arrObj = tree.toArray();
        String[] arrSmallArg = tree.toArray(new String[0]);
        String[] arrEnoughSizeArg = tree.toArray(new String[666]);

        assertThat(arrObj.length).isEqualTo(arrSmallArg.length);

        for (int i = 0; i < arrObj.length; i++) {
            assertThat((String) arrObj[i]).isEqualTo(arrSmallArg[i])
                                 .isEqualTo(arrEnoughSizeArg[i]);
        }

        assertThat(arrEnoughSizeArg[arrObj.length]).isNull();
    }

    @Test
    void anotherGenericTypeToArrayTest() {
        assertThatThrownBy(() -> tree.toArray(new Integer[0]))
            .isInstanceOf(ArrayStoreException.class);
        assertThatThrownBy(() -> tree.toArray(new Integer[100500]))
            .isInstanceOf(ArrayStoreException.class);
    }
} 
