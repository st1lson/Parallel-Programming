package mkr.cars;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CarsTask {
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

    public static class Car implements Runnable {
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
    
    public static class TrafficLight implements Runnable {
        private final Lock locker;
        private final Condition greenLight;
        private final Condition yellowLight;
        private final Condition redLight;
        public static int signalIndex = 0;
    
        public TrafficLight(Lock locker, Condition greenLight, Condition yellowLight, Condition redLight) {
            this.locker = locker;
            this.greenLight = greenLight;
            this.yellowLight = yellowLight;
            this.redLight = redLight;
        }
    
        @Override
        public void run() {
            try {
                while (true) {
                    if (Car.completed) {
                        break;
                    }
    
                    switch (TrafficLight.signalIndex) {
                        case 0:
                            System.out.println("Green light");
                            this.locker.lock();
                            try {
                                this.greenLight.signalAll();
                                Thread.sleep(60);
                            } finally {
                                this.locker.unlock();
                            }
                            signalIndex = 1;
                            break;
                        case 1:
                            System.out.println("Yellow light");
                            this.locker.lock();
                            try {
                                this.yellowLight.signalAll();
                                Thread.sleep(10);
                            } finally {
                                this.locker.unlock();
                            }
                            signalIndex = 2;
                            break;
                        case 2:
                            System.out.println("Red light");
                            this.locker.lock();
                            try {
                                this.redLight.signalAll();
                                Thread.sleep(40);
                            } finally {
                                this.locker.unlock();
                            }
                            signalIndex = 3;
                            break;
                        case 3:
                            System.out.println("Yellow light");
                            this.locker.lock();
                            try {
                                this.yellowLight.signalAll();
                                Thread.sleep(10);
                            } finally {
                                this.locker.unlock();
                            }
                            signalIndex = 0;
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
