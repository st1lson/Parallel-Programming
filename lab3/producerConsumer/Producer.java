package lab3.producerConsumer;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final int iterations;

    public Producer(Buffer buffer, int iterations) {
        this.buffer = buffer;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        try {
            for (var i = 0; i < iterations; i++) {
                var value = Math.random();
                buffer.put(value);
                System.out.println(String.format("The value %s has been added to the buffer", value));   
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
