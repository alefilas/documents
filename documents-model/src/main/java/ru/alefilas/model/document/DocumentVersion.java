package ru.alefilas.model.document;

import lombok.Data;
import ru.alefilas.model.moderation.ModerationStatus;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "version")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "file", joinColumns = @JoinColumn(name = "version_id"))
    @Column(name = "path")
    private List<String> files;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ModerationStatus status;

    public void addFile(String path) {
        files.add(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentVersion version = (DocumentVersion) o;
        return Objects.equals(id, version.id) &&
                Objects.equals(title, version.title) &&
                Objects.equals(description, version.description) &&
                Objects.deepEquals(files.toArray(), version.files.toArray()) &&
                status == version.status;
    }
}
