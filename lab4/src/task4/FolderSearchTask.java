package task4;

import common.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<List<Occurrence>> {
    private final Folder folder;
    private final String[] wordsToFind;

    public FolderSearchTask(Folder folder, String[] wordsToFind) {
        super();
        this.folder = folder;
        this.wordsToFind = wordsToFind;
    }

    @Override
    protected List<Occurrence> compute() {
        var occurrences = new ArrayList<Occurrence>();
        var forks = new ArrayList<RecursiveTask<List<Occurrence>>>();
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
            occurrences.addAll(fork.join());
        }

        return occurrences;
    }
}
