package lab3.producerConsumer;

public class Program {
    private static final int ITERATIONS = 2500;

    public static void main(String[] args) {
        var buffer = new Buffer();

        var producer = new Thread(new Producer(buffer, ITERATIONS));
        var consumer = new Thread(new Consumer(buffer, ITERATIONS));

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
