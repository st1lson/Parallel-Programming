package task3;

import common.Document;

import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Set<String>> {
    private final Document document;

    public DocumentSearchTask(Document document) {
        super();
        this.document = document;
    }

    @Override
    protected Set<String> compute() {
        return CounterHelper.getUniqueWords(document);
    }
}
