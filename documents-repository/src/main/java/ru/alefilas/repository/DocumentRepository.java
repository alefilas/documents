package ru.alefilas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;

import javax.print.Doc;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT v FROM Document d JOIN d.versions v WHERE v.title = :title AND v.description = :description AND d.id = :id")
    Optional<DocumentVersion> findDocumentVersion(String title, String description, Long id);

    Page<Document> findAllByStatus(ModerationStatus status, Pageable pageable);

    Page<Document> findAllByTypeTypeAndStatus(String type, ModerationStatus status, Pageable pageable);
}
