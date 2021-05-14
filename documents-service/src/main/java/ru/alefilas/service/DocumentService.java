package ru.alefilas.service;

import org.springframework.data.domain.Page;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.model.document.*;

import java.util.List;

public interface DocumentService {

    OutputDocumentDto save(InputDocumentDto document);

    OutputDocumentVersionDto save(InputDocumentVersionDto version, Long documentId);

    OutputDocumentDto getDocumentById(Long id);

    void deleteById(Long id);

    DocumentType getDocumentTypeByName(String name);

    List<DocumentType> getAllDocumentTypes();

    DocumentType save(String type);

    List<OutputDocumentVersionDto> getDocumentVersionsById(Long id);

    Page<OutputDocumentDto> getAllDocuments(int page);

    Page<OutputDocumentDto> getDocumentsByType(String type, int page);


}
