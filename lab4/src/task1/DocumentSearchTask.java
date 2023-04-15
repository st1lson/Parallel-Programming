package task1;

import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Long> {
    private final Document document;
    private final String searchedWord;

    DocumentSearchTask(Document document, String searchedWord) {
        super();
        this.document = document;
        this.searchedWord = searchedWord;
    }

    @Override
    protected Long compute() {
        return occurrencesCount(document);
    }

    private String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    private Long occurrencesCount(Document document) {
        long count = 0;
        for (String line : document.lines()) {
            for (String word : wordsIn(line)) {
                count++;
            }
        }
        return count;
    }

}
