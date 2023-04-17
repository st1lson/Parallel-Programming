package task4;

import common.Document;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Map<String, ArrayList<Occurrence>>> {
    private final Document document;
    private final String[] wordsToFind;

    public DocumentSearchTask(Document document, String[] wordsToFind) {
        super();
        this.document = document;
        this.wordsToFind = wordsToFind;
    }

    @Override
    protected Map<String, ArrayList<Occurrence>> compute() {
        return CounterHelper.findOccurrences(document, wordsToFind);
    }
}
