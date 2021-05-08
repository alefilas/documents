package ru.alefilas.service;

import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.dto.EntityDto;
import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentService {

    DocumentDto save(DocumentDto document);

    DirectoryDto save(DirectoryDto directory);

    DocumentVersion save(DocumentVersion version, Long documentId);

    List<EntityDto> getEntitiesByDirectory(DirectoryDto directory);

    DocumentDto getDocumentById(Long id);

    DocumentVersion getVersionById(Long id);

    DirectoryDto getDirectoryById(Long id);

    void deleteById(Long id);

    List<DocumentVersion> getAllVersionByDocumentId(Long id);

    DocumentType getDocumentTypeByName(String name);

    List<DocumentType> getAllDocumentTypes();


}
