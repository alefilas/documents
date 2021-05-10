package ru.alefilas.service;

import ru.alefilas.dto.InputDocumentDto;
import ru.alefilas.dto.InputDocumentVersionDto;
import ru.alefilas.dto.OutputDocumentDto;
import ru.alefilas.dto.OutputDocumentVersionDto;
import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentService {

    OutputDocumentDto save(InputDocumentDto document);

    OutputDocumentVersionDto save(InputDocumentVersionDto version, Long documentId);

    OutputDocumentDto getDocumentById(Long id);

    void deleteById(Long id);

    List<DocumentVersion> getAllVersionByDocumentId(Long id);

    DocumentType getDocumentTypeByName(String name);

    List<DocumentType> getAllDocumentTypes();


}
