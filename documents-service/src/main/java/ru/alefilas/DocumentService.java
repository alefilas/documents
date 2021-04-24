package ru.alefilas;

import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.DocumentVersion;

import java.util.List;

public interface DocumentService {

    Document save(Document document);

    Directory save(Directory directory);

    DocumentVersion save(DocumentVersion version, Long documentId);

    List<AbstractEntity> getEntitiesByDirectory(Directory directory);

    Document findDocumentById(Long id);

    DocumentVersion findVersionById(Long id);

    Directory findDirectoryById(Long id);

    void deleteById(String table, Long id);

    List<DocumentVersion> findAllVersionByDocumentId(Long id);


}
