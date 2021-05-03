package ru.alefilas;


import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentsDao {

    Document save(Document document);

    Directory save(Directory directory);

    DocumentVersion save(DocumentVersion version, Long documentId);

    List<AbstractEntity> findEntityByDirectory(Directory directory);

    void deleteById(Long id);

    Document findDocumentById(Long id);

    DocumentVersion findVersionById(Long id);

    Directory findDirectoryById(Long id);

    List<DocumentVersion> findAllVersionByDocumentId(Long id);

    DocumentType findDocumentTypeByName(String name);

    List<DocumentType> findAllDocumentTypes();

}
