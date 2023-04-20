package task4;

import common.Folder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Program {
    private final static String[] wordsToSearch = {"hello", "world"};

    public static void main(String[] args) throws IOException {
        var filesPath = new File("src/files").getAbsolutePath();
        var file = new File(filesPath);
        var folder = Folder.fromDirectory(file);

        var wordCounter = new WordCounter();
        var occurrences = wordCounter.countOccurrencesInParallel(folder, wordsToSearch);

        var builder = new StringBuilder();
        for (var occurrence : occurrences) {
            builder.append(occurrence);
        }

        System.out.println(builder);
        System.out.printf("Total occurrences: %s%n", occurrences.size());

        for (var word : wordsToSearch) {
            var wordMatches = occurrences.stream()
                    .filter(occurrence -> Objects.equals(occurrence.word(), word));

            System.out.printf("The word %s was found in the following files:%n", word);
            for (var fileName : wordMatches.filter(distinctByKey(Occurrence::file)).map(Occurrence::file).toList()) {
                System.out.println(fileName);
            }
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
