package ru.alefilas.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;

import java.nio.file.Path;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class Document extends AbstractEntity {

    private DocumentVersion currentVersion;
    private List<DocumentVersion> versions;
    private DocumentPriority documentPriority;
    private User user;
    private String type;
    private ModerationStatus status;

    public void addFile(Path path) {
        currentVersion.addFile(path);
    }

    public void addVersion(DocumentVersion version) {
        versions.add(version);
    }

    @Override
    @JsonIgnore
    public boolean isDocument() {
        return true;
    }
}
