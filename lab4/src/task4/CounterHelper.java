package task4;

import common.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CounterHelper {

    private CounterHelper() {
    }

    public static String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }


    public static List<Occurrence> findOccurrences(Document document, String... wordsToSearch) {
        var occurrences = new ArrayList<Occurrence>();
        var enumerable = Arrays.asList(wordsToSearch);

        ArrayList<String> lines = document.lines();
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            for (var word : wordsIn(line)) {
                var formattedWord = word.toLowerCase();
                if (enumerable.stream().noneMatch(formattedWord::equalsIgnoreCase)) continue;

                occurrences.add(new Occurrence(document.fileName(), formattedWord, i));
            }
        }

        return occurrences;
    }
}
