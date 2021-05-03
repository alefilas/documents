package ru.alefilas;

import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentService {

    DocumentDto save(DocumentDto document);

    DirectoryDto save(DirectoryDto directory);

    DocumentVersion save(DocumentVersion version, Long documentId);

    List<AbstractEntity> getEntitiesByDirectory(Directory directory);

    DocumentDto getDocumentById(Long id);

    DocumentVersion getVersionById(Long id);

    DirectoryDto getDirectoryById(Long id);

    void deleteById(Long id);

    List<DocumentVersion> getAllVersionByDocumentId(Long id);

    DocumentType findDocumentTypeByName(String name);

    List<DocumentType> findAllDocumentTypes();


}
