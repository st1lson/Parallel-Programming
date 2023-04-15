package task3;

import common.Document;

import java.util.HashSet;
import java.util.Set;

public final class CounterHelper {

    private CounterHelper() {
    }

    public static String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }


    public static Set<String> getUniqueWords(Document document) {
        var set = new HashSet<String>();
        for (var line : document.lines()) {
            for (var word : wordsIn(line)) {
                if (word.length() == 0) continue;

                set.add(word.toLowerCase());
            }
        }

        return set;
    }
}
