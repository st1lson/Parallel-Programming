package mkr.cars;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    public static int counter = 0;

    private static int idCounter = 0;

    private final int id = ++idCounter;

    private final Lock locker;

    private final Condition greenLight;

    private static final int NUM_ROUNDS = 1000;

    public static boolean completed = false;


    public Car(Lock locker, Condition greenLight) {
        this.locker = locker;
        this.greenLight = greenLight;
    }

    private void go() {
        while (Car.counter < NUM_ROUNDS) {
            try {
                locker.lock();
                while (TrafficLight.signalIndex != 0) {
                    greenLight.await();
                }

                Car.counter++;
                System.out.println(String.format("Car %d moves", this.id));
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                locker.unlock();
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Car.completed = true;
    }

    @Override
    public void run() {
        go();
    }
}
