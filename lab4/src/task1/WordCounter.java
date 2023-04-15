package task1;

import java.util.concurrent.*;

public class WordCounter {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Long countOccurrencesInParallel(Folder folder, String searchedWord) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
    }
}