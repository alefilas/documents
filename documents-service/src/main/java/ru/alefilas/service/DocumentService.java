package ru.alefilas.service;

import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.dto.EntityDto;
import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentService {

    DocumentDto save(DocumentDto document);

    DocumentVersion save(DocumentVersion version, Long documentId);

    DocumentDto getDocumentById(Long id);

    void deleteById(Long id);

    List<DocumentVersion> getAllVersionByDocumentId(Long id);

    DocumentType getDocumentTypeByName(String name);

    List<DocumentType> getAllDocumentTypes();


}
