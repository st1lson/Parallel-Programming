package task4;

import common.Document;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<List<Occurrence>> {
    private final Document document;
    private final String[] wordsToFind;

    public DocumentSearchTask(Document document, String[] wordsToFind) {
        super();
        this.document = document;
        this.wordsToFind = wordsToFind;
    }

    @Override
    protected List<Occurrence> compute() {
        return CounterHelper.findOccurrences(document, wordsToFind);
    }
}
