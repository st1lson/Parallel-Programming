package task1;

import common.Document;

import java.util.HashMap;

public final class CounterHelper {

    private CounterHelper() {
    }

    public static String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    public static HashMap<Integer, Integer> createDocumentMap(Document document) {
        var map = new HashMap<Integer, Integer>();
        for (var line : document.lines()) {
            for (var word : wordsIn(line)) {
                var wordLength = word.length();
                if (wordLength == 0) continue;

                if (map.containsKey(wordLength)) {
                    map.put(wordLength, map.get(wordLength) + 1);
                } else {
                    map.put(wordLength, 1);
                }
            }
        }

        return map;
    }
}
