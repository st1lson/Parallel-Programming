package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public record Document(String fileName, ArrayList<String> lines) {
    public static Document fromFile(File file) throws IOException {
        var lines = new ArrayList<String>();
        try (var reader = new BufferedReader(new FileReader(file))) {
            var line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }

        return new Document(file.getName(), lines);
    }
}
