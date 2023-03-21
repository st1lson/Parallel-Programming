package mkr.arrays;

import java.util.ArrayList;
import java.util.Arrays;

public class Program {
    private static final int N = 2;

    public static void main(String[] args) {

        var threads = new Thread[N];
        var text = new ArrayList<String>();
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(new TextPusher(new ArrayList<>(Arrays.asList("a", "b", "c")), text));
        }

        for (var threadPusher : threads) {
            threadPusher.start();
        }

        for (var threadPusher : threads) {
            try {
                threadPusher.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (var resultStr : text) {
            System.out.println(resultStr);
        }
    }
}
