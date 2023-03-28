package ru.nsu.fit.smolyakov.pizzeria.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.assertThat;

public class ProducerConsumerQueueTest {
    @Test
    @Timeout(value = 10)
    public void threadTest() throws InterruptedException {
        var shop = new ConsumerProducerQueue<Integer>(10);

        assertThat(shop.isEmpty()).isTrue();

        var consumer = new Thread(() -> {
            for (int i = 0; i < 19; i++) {
                try {
                    var item = shop.take();
                    System.err.println("recieved " + item + "\n");
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
    }
}
