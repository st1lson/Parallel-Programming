package task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Map<Integer, Integer>> {
    private final Folder folder;

    FolderSearchTask(Folder folder) {
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
            map.putAll(task.join());
        }

        return map;
    }
}
