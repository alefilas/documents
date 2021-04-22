package ru.alefilas.model.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;

import java.nio.file.Path;


@EqualsAndHashCode(callSuper = true)
@Data
public class Document extends AbstractEntity {

    private DocumentVersion currentVersion;
    private DocumentPriority documentPriority;
    private User user;
    private String type;
    private ModerationStatus status;

    public void addFile(Path path) {
        currentVersion.addFile(path);
    }

    @Override
    public boolean isDocument() {
        return true;
    }
}
