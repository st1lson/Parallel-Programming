package task1;

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
        return CounterHelper.crateDocumentMap(document);
    }

    private Long occurrencesCount(Document document) {
        var count = 0L;
        for (var line : document.lines()) {
            count += CounterHelper.wordsIn(line).length;
        }

        return count;
    }
}
