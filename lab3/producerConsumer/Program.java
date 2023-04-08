package lab3.producerConsumer;

import java.util.ArrayList;

public class Program {
    private static final int ITERATIONS = 2500;
    private static final int NUMBER_OF_WORKERS = 2;

    public static void main(String[] args) {
        var buffer = new Buffer();

        var producers = new ArrayList<Thread>();
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            producers.add(new Thread(new Producer(buffer, ITERATIONS)));
        }

        var consumers = new ArrayList<Thread>();
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            consumers.add(new Thread(new Consumer(buffer, ITERATIONS)));
        }

        for (var producer : producers) {
            producer.start();
        }

        for (var consumer : consumers) {
            consumer.start();
        }

        try {
            for (var producer : producers) {
                producer.join();
            }
    
            for (var consumer : consumers) {
                consumer.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
