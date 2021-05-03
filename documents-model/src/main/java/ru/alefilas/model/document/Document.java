package ru.alefilas.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "document")
public class Document extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "current_version_id")
    private DocumentVersion currentVersion;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "document_id")
    private List<DocumentVersion> versions;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private DocumentPriority documentPriority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ModerationStatus status;

    public Document() {
        versions = new ArrayList<>();
    }

    public void addFile(String path) {
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
