package lab3.producerConsumer;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int iterations;

    public Consumer(Buffer buffer, int iterations) {
        this.buffer = buffer;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        try {
            for (var i = 0; i < iterations; i++) {
                var value = (double)buffer.take();
                System.out.println(String.format("Thread #%s\nThe value %s has been removed to the buffer\n", Thread.currentThread().getId(), value));   
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
