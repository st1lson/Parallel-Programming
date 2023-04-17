package task4;

import common.Folder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Map<String, ArrayList<Occurrence>> countOccurrencesInParallel(Folder folder, String... wordsToFind) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, wordsToFind));
    }
}
