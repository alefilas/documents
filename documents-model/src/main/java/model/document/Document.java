package model.document;

import lombok.Data;
import model.directory.Directory;
import model.user.User;

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
