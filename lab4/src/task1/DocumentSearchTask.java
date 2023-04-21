package task1;

import common.Document;

import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Map<Integer, Integer>> {
    private final Document document;

    public DocumentSearchTask(Document document) {
        super();
        this.document = document;
    }

    @Override
    protected Map<Integer, Integer> compute() {
        return CounterHelper.createDocumentMap(document);
    }
}
