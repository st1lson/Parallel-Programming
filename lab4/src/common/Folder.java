package common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public record Folder(ArrayList<Folder> subFolders, ArrayList<Document> documents) {
    public static Folder fromDirectory(File dir) throws IOException {
        var documents = new ArrayList<Document>();
        var subFolders = new ArrayList<Folder>();
        for (var entry : Objects.requireNonNull(dir.listFiles())) {
            if (entry.isDirectory()) {
                subFolders.add(Folder.fromDirectory(entry));
            } else {
                documents.add(Document.fromFile(entry));
            }
        }

        return new Folder(subFolders, documents);
    }
}
