package task1;

import common.Folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Map<Integer, Integer>> {
    private final Folder folder;

    public FolderSearchTask(Folder folder) {
        super();
        this.folder = folder;
    }

    @Override
    protected Map<Integer, Integer> compute() {
        var map = new HashMap<Integer, Integer>();
        var forks = new ArrayList<RecursiveTask<Map<Integer, Integer>>>();
        for (var subFolder : folder.subFolders()) {
            var task = new FolderSearchTask(subFolder);
            forks.add(task);
            task.fork();
        }

        for (var document : folder.documents()) {
            var task = new DocumentSearchTask(document);
            forks.add(task);
            task.fork();
        }

        for (var task : forks) {
            var newMap = task.join();
            for (var key : newMap.keySet()) {
                map.merge(key, newMap.get(key), Integer::sum);
            }
        }

        return map;
    }
}
