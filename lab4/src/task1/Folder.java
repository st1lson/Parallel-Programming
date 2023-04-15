package task1;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public record Folder(List<Folder> subFolders, List<Document> documents) {
    public static Folder fromDirectory(File dir) throws IOException {
        List<Document> documents = new LinkedList<>();
        List<Folder> subFolders = new LinkedList<>();
        for (File entry : dir.listFiles()) {
            if (entry.isDirectory()) {
                subFolders.add(Folder.fromDirectory(entry));
            } else {
                documents.add(Document.fromFile(entry));
            }
        }
        return new Folder(subFolders, documents);
    }
}
