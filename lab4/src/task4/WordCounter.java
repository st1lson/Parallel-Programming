package task4;

import common.Folder;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public List<Occurrence> countOccurrencesInParallel(Folder folder, String... wordsToFind) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, wordsToFind));
    }
}
