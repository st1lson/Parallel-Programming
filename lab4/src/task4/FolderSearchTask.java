package task4;

import common.Folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Map<String, ArrayList<Occurrence>>> {
    private final Folder folder;
    private final String[] wordsToFind;

    public FolderSearchTask(Folder folder, String[] wordsToFind) {
        super();
        this.folder = folder;
        this.wordsToFind = wordsToFind;
    }

    @Override
    protected Map<String, ArrayList<Occurrence>> compute() {
        var dictionary = new HashMap<String, ArrayList<Occurrence>>();
        var forks = new ArrayList<RecursiveTask<Map<String, ArrayList<Occurrence>>>>();
        for (var subFolder : folder.subFolders()) {
            var task = new FolderSearchTask(subFolder, wordsToFind);
            forks.add(task);
            task.fork();
        }

        for (var document : folder.documents()) {
            var task = new DocumentSearchTask(document, wordsToFind);
            forks.add(task);
            task.fork();
        }

        for (var fork : forks) {
            dictionary.putAll(fork.join());
        }

        return dictionary;
    }
}
