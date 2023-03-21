package mkr.cars;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TrafficLight implements Runnable {
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
