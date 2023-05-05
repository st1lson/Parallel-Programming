import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {
    private static final int NUMBER_OF_WORDS = 10000;

    public static void main(String[] args) {
        var array = new ArrayList<String>();
        for (int i = 0; i < NUMBER_OF_WORDS; i++) {
            array.add(Integer.toString(i));
        }

        var forkJoinPool = new ForkJoinPool();
        var result = forkJoinPool.invoke(new StringProcessor(array));

        System.out.println(result);
    }

    public static class StringProcessor extends RecursiveTask<Result> {

        private final static int MAX_CHUNK_SIZE = 100;

        private final List<String> array;

        public StringProcessor(List<String> array) {
            this.array = array;
        }

        @Override
        protected Result compute() {

            var forks = new ArrayList<RecursiveTask<Result>>();
            if (array.size() <= MAX_CHUNK_SIZE) {
                var totalLength = 0f;
                for (var word : array) {
                    totalLength += word.length();
                }

                return new Result(totalLength / array.size(), 1);
            } else {
                var firstTask = new StringProcessor(array.subList(0, array.size() / 2));
                var secondTask = new StringProcessor(array.subList(array.size() / 2, array.size()));

                firstTask.fork();
                secondTask.fork();

                forks.add(firstTask);
                forks.add(secondTask);
            }

            var finalResult = new Result();
            for (var fork : forks) {
                var result = fork.join();
                finalResult = Result.merge(finalResult, result);
            }

            return finalResult;
        }
    }

    public static class Result {

        private final float total;
        private final int numberOfProcessors;

        public Result(float totalWords, int numberOfProcessors) {
            this.total = totalWords;
            this.numberOfProcessors = numberOfProcessors;
        }

        public Result() {
            total = 0;
            numberOfProcessors = 0;
        }

        public float total() {
            return total;
        }

        public int numberOfProcessors() {
            return numberOfProcessors;
        }

        public static Result merge(Result firstResult, Result secondResult) {
            return new Result(firstResult.total() + secondResult.total(), firstResult.numberOfProcessors() + secondResult.numberOfProcessors());
        }

        @Override
        public String toString() {
            return String.format("Number of processors: %s%nMean length: %s%n", numberOfProcessors, total / numberOfProcessors);
        }
    }
}