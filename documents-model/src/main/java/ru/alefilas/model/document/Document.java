package ru.alefilas.model.document;

import lombok.Data;
import ru.alefilas.model.directory.Directory;
import ru.alefilas.model.user.User;

import java.nio.file.Path;
import java.util.List;

@Data
public class Document {

    private Long id;

    private String title;

    private String description;

    private List<Path> files;

    private Integer version;

    private DocumentType documentType;

    private DocumentPriority documentPriority;

    private User user;

    private Directory directory;

    public void addFile(Path path) {
        files.add(path);
    }
}
