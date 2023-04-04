package ru.nsu.fit.smolyakov.pizzeria.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProducerConsumerQueueTest {
    @Test
    @Timeout(value = 10)
    public void classicConsumerProducerTest() throws InterruptedException {
        var shop = new ConsumerProducerQueue<Integer>(10);

        var put = new BitSet(19);
        var got = new BitSet(19);

        assertThat(shop.isEmpty()).isTrue();

        var consumer = new Thread(() -> {
            for (int i = 0; i < 19; i++) {
                try {
                    var item = shop.take();
                    System.err.println("recieved " + item + "\n");
                    got.set(item);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        );

        var producer = new Thread(() -> {
            for (int i = 0; i < 19; i++) {
                try {
                    shop.put(i);
                    System.err.println("put " + i + "\n");
                    put.set(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        );

        consumer.start();
        Thread.sleep(4123);
        assertThat(shop.isEmpty()).isTrue();
        producer.start();
        Thread.sleep(4123);
        assertThat(shop.isEmpty()).isTrue();

        assertThat(put.stream().distinct().count()).isEqualTo(19);
        assertThat(got.stream().distinct().count()).isEqualTo(19);

    }

    @Test
    @Timeout(value = 10)
    void takeMultipleTest() throws InterruptedException {
        var shop = new ConsumerProducerQueue<Integer>(10);

        var consumer = new Thread(() -> {
            Queue<Integer> items;
            try {
                items = shop.takeMultiple(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            assertThat(items.size()).isEqualTo(10);

            try {
                items = shop.takeMultiple(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            assertThat(items.size()).isEqualTo(5);
            assertThat(shop.isEmpty()).isTrue();
        }
        );

        var producer = new Thread(() -> {
            for (int i = 0; i < 15; i++) {
                try {
                    shop.put(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        );

        assertThatThrownBy(() -> shop.takeMultiple(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void putNullNpeTest() {
        assertThatThrownBy(() -> new ConsumerProducerQueue<Object>(666).put(null))
            .isInstanceOf(NullPointerException.class);
    }
}
