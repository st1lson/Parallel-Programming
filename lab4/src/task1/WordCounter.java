package task1;

import common.Folder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static void mergeMaps(Map<Integer, Integer> firstMap, Map<Integer, Integer> secondMap) {
        for (var key : secondMap.keySet()) {
            firstMap.merge(key, secondMap.get(key), Integer::sum);
        }
    }

    public Map<Integer, Integer> countOccurrencesInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }

    public Map<Integer, Integer> findOccurences(Folder folder) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (var subFolder : folder.subFolders()) {
            mergeMaps(map, findOccurences(subFolder));
        }

        for (var document : folder.documents()) {
            mergeMaps(map, CounterHelper.createDocumentMap(document));
        }

        return map;
    }
}
