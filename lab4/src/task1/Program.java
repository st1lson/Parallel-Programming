package task1;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        var file = new File("C:\\Users\\38093\\Downloads\\Telegram Desktop\\text");
        var folder = Folder.fromDirectory(file);

        var wordCounter = new WordCounter();
        var map = wordCounter.countOccurrencesInParallel(folder);
        for (var item : map.keySet()) {
            System.out.printf("%s: %s%n", item, map.get(item));
        }
    }
}
