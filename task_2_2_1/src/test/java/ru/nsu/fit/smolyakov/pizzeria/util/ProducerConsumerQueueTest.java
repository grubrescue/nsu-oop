package ru.nsu.fit.smolyakov.pizzeria.util;

import org.junit.jupiter.api.Test;

public class ProducerConsumerQueueTest {
    @Test
    public void threadTest() throws InterruptedException {
        var shop = new ConsumerProducerQueue<Integer>(10);

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
        producer.start();
    }
}
