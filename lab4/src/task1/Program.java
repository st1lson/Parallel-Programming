package task1;

import common.Folder;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        var filesPath = new File("src/files").getAbsolutePath();
        var file = new File(filesPath);
        var folder = Folder.fromDirectory(file);

        var wordCounter = new WordCounter();
        var map = wordCounter.countOccurrencesInParallel(folder);

        System.out.println("===== ITEMS =====");
        for (var item : map.entrySet()) {
            System.out.printf("%s: %s%n", item.getKey(), item.getValue());
        }

        System.out.println("===== STATISTICS =====");
        var totalWords = 0d;
        var wordsCount = 0;
        for (var item : map.entrySet()) {
            totalWords += (item.getKey() * item.getValue());
            wordsCount += item.getValue();
        }
        var mean = totalWords / wordsCount;

        var sum = 0d;
        for (var item : map.entrySet()) {
            sum += (Math.pow(item.getKey(), 2) * item.getValue());
        }

        var D =sum / wordsCount - Math.pow(mean, 2);
        var standardDeviation = Math.sqrt(D);

        System.out.printf("Mean length: %s%n", mean);
        System.out.printf("D: %s%n", D);
        System.out.printf("Standard deviation: %s%n", standardDeviation);
    }
}
