package task3;

import common.Folder;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        var filesPath = new File("src/files").getAbsolutePath();
        var file = new File(filesPath);
        var folder = Folder.fromDirectory(file);

        var wordCounter = new WordCounter();
        var set = wordCounter.countOccurrencesInParallel(folder);

        System.out.println(set);
        System.out.println(set.size());
    }
}
