package task3;

import common.Folder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Set<String>> {
    private final Folder folder;

    public FolderSearchTask(Folder folder) {
        super();
        this.folder = folder;
    }

    @Override
    protected Set<String> compute() {
        Set<String> set = new HashSet<>();
        var forks = new ArrayList<RecursiveTask<Set<String>>>();
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

        for (var fork : forks) {
            if (set.size() == 0) {
                set.addAll(fork.join());
            } else {
                set.retainAll(fork.join());
            }
        }

        return set;
    }
}
