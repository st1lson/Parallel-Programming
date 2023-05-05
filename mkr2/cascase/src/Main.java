import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static java.util.Arrays.copyOfRange;

public class Main {

    private static final int ARRAY_SIZE = 10000;
    private static final int NUMBER_OF_WORKERS = 10;

    public static void main(String[] args) {
        var array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = i;
        }

        var forkJoinPool = new ForkJoinPool(NUMBER_OF_WORKERS);
        var result = forkJoinPool.invoke(new ArrayProcessor(array));

        System.out.println(result);
    }

    public static class ArrayProcessor extends RecursiveTask<Float> {

        private static double CHUNK_SIZE = 300;

        private final int[] array;

        public ArrayProcessor(int[] array) {
            this.array = array;
            ArrayProcessor.CHUNK_SIZE = array.length / (Math.log(array.length) / Math.log(2));
        }

        @Override
        protected Float compute() {

            var forks = new ArrayList<RecursiveTask<Float>>();
            if (array.length <= CHUNK_SIZE) {
                var sum = 0f;
                for (var item : array) {
                    sum += item;
                }

                return sum;
            } else {
                var firstProcessor = new ArrayProcessor(copyOfRange(array, 0, array.length / 2));
                var secondProcessor = new ArrayProcessor(copyOfRange(array, array.length / 2, array.length));

                firstProcessor.fork();
                secondProcessor.fork();

                forks.add(firstProcessor);
                forks.add(secondProcessor);
            }

            var sum = 0f;
            for (var fork : forks) {
                sum += fork.join();
            }

            return sum;
        }
    }
}