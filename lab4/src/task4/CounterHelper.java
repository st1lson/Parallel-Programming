package task4;

import common.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CounterHelper {

    private CounterHelper() {
    }

    public static String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }


    public static Map<String, ArrayList<Occurrence>> findOccurrences(Document document, String... wordsToSearch) {
        var dictionary = new HashMap<String, ArrayList<Occurrence>>();
        var enumerable = Arrays.asList(wordsToSearch);
        for (var word : enumerable) {
            dictionary.put(word, new ArrayList<>());
        }

        ArrayList<String> lines = document.lines();
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            for (var word : wordsIn(line)) {
                var formattedWord = word.toLowerCase();
                if (enumerable.stream().noneMatch(formattedWord::equalsIgnoreCase)) continue;

                var array = dictionary.get(formattedWord);
                array.add(new Occurrence(document.fileName(), formattedWord, i));
            }
        }

        return dictionary;
    }
}
