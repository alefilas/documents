package ru.alefilas;


import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentVersion;

import java.util.List;

public interface DocumentsDao {

    Document save(Document document);

    Directory save(Directory directory);

    DocumentVersion save(DocumentVersion version, Long documentId);

    List<AbstractEntity> findEntityByDirectory(Directory directory);

    void deleteById(String table, Long id);

    Document findDocumentById(Long id);

    DocumentVersion findVersionById(Long id);

    Directory findDirectoryById(Long id);

    List<DocumentVersion> findAllVersionByDocumentId(Long id);

}
