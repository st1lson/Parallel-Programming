package task4;

import common.Folder;

import java.io.File;
import java.io.IOException;

public class Program {
    private final static String[] wordsToSearch = {"hello", "world"};

    public static void main(String[] args) throws IOException {
        var filesPath = new File("src/files").getAbsolutePath();
        var file = new File(filesPath);
        var folder = Folder.fromDirectory(file);

        var wordCounter = new WordCounter();
        var result = wordCounter.countOccurrencesInParallel(folder, wordsToSearch);

        var builder = new StringBuilder();
        var totalOccurrences = 0;
        for (var occurrences : result.entrySet()) {
            for (var occurrence : occurrences.getValue()) {
                builder.append(occurrence);
                totalOccurrences++;
            }
        }

        System.out.println(builder);
        System.out.printf("Total occurrences: %s%n", totalOccurrences);
    }
}
