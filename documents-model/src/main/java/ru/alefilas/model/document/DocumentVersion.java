package ru.alefilas.model.document;

import lombok.Data;
import ru.alefilas.model.moderation.ModerationStatus;

import java.nio.file.Path;
import java.util.List;

@Data
public class DocumentVersion {

    private Long id;
    private String title;
    private String description;
    private List<Path> files;
    private Document document;
    private ModerationStatus status;

    public void addFile(Path path) {
        files.add(path);
    }

}
