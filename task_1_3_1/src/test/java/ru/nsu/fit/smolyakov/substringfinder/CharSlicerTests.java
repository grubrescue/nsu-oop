package ru.nsu.fit.smolyakov.substringfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// also abstract methods tested
class CharSlicerTests {
    File file;
    List<String> slicesList =
        List.of(
            "абобу",
            "бобус",
            "обус ",
            "бус д",
            "ус ду",
            "с дуб"
        );

    @BeforeEach 
    void init() {
        file = new File("src/test/resources/ru/nsu/fit/smolyakov/substring_finder/SlicerTest.txt");
    }
    
    @Test
    void iteratorTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);
        var list = new ArrayList<String>();

        do {
            var tmpArr = new char[5];
            int i = 0;
            for (var item : haystack) {
                tmpArr[i] = item;
                i++;
            }
            list.add(new String(tmpArr));
        } while (haystack.shift() != null);

        assertThat(list).isEqualTo(slicesList);
    }

    @Test
    void toCharArrayTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);
        var list = new ArrayList<String>();

        do {
            list.add(new String(haystack.toCharArray()));
        } while (haystack.shift() != null);

        assertThat(list).isEqualTo(slicesList);
    }

    @Test
    void getItemTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);
        var list = new ArrayList<String>();

        do {
            var tmpArr = new char[5];
            
            for (int i = 0; i < 5; i++) {
                tmpArr[i] = haystack.get(i);
            }
            list.add(new String(tmpArr));
        } while (haystack.shift() != null);

        assertThat(list).isEqualTo(slicesList);
    }

    @Test
    void getAbsolutePositionTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);

        assertThat(haystack.getAbsolutePosition()).isEqualTo(0);
        haystack = haystack.shift(2);
        assertThat(haystack.getAbsolutePosition()).isEqualTo(2);
    }

    @Test
    void haystackIsEndedTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);
        var isItNull = haystack.shift(100500);

        assertThat(isItNull).isNull();

        assertThatThrownBy(() -> haystack.get(0))
            .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> haystack.iterator())
            .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> haystack.toCharArray())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getUnexistantItemTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);

        assertThatThrownBy(() -> haystack.get(100500))
            .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test 
    void tooBigNeedleTest() throws IOException {
        assertThatThrownBy(() ->
            new CharSlicer(new InputStreamReader(new FileInputStream(file)), 100500)
        ).isInstanceOf(InputMismatchException.class);
    }

    @Test 
    void tooSmallNeedleTest() throws IOException {
        assertThatThrownBy(() ->
            new CharSlicer(new InputStreamReader(new FileInputStream(file)), 0)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shiftLeftTest() throws IOException {
        var haystack = new CharSlicer(new InputStreamReader(new FileInputStream(file)), 5);

        assertThatThrownBy(() -> haystack.shift(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
