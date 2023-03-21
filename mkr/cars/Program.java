package mkr.cars;

import java.util.concurrent.locks.ReentrantLock;

public class Program {
    private static final int NUMBER_OF_CARS = 100;

    public static void main(String[] args) {
        var locker = new ReentrantLock();
        var green = locker.newCondition();
        var yellow = locker.newCondition();
        var red = locker.newCondition();
        var threads = new Thread[NUMBER_OF_CARS];
        var trafficLight = new Thread(new TrafficLight(locker, green, yellow, red));
        trafficLight.start();
        for (int i = 0; i < NUMBER_OF_CARS; i++) {
            threads[i] = new Thread(new Car(locker, green));
            threads[i].start();
        }

        try {
            trafficLight.join();
            for (var thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed");
    }
}
