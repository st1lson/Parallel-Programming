package task1;

import java.util.Map;
import java.util.concurrent.*;

public class WordCounter {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Map<Integer, Integer> countOccurrencesInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }
}
