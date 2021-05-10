package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.InputDocumentDto;
import ru.alefilas.dto.InputDocumentVersionDto;
import ru.alefilas.dto.OutputDocumentDto;
import ru.alefilas.dto.OutputDocumentVersionDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.repository.DocumentRepository;
import ru.alefilas.repository.DocumentTypeRepository;
import ru.alefilas.service.DocumentService;
import ru.alefilas.service.exception.DocumentNotFoundException;
import ru.alefilas.service.exception.DocumentTypeNotFoundException;
import ru.alefilas.service.mapper.DocumentMapper;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
    }


    @Override
    @Transactional
    public OutputDocumentDto save(InputDocumentDto document) {

        Document savedDocument = DocumentMapper.dtoToModel(document);

        if (savedDocument.getId() == null) {
            savedDocument.setCreationDate(LocalDate.now());
            savedDocument.setStatus(ModerationStatus.ON_MODERATION);
        } else {
            Document docFromDb = findDocumentById(savedDocument.getId());
            savedDocument.setStatus(docFromDb.getStatus());
            savedDocument.setCreationDate(docFromDb.getCreationDate());
        }

        DocumentVersion version = savedDocument.getCurrentVersion();
        if (version.getId() == null) {
            version.setStatus(ModerationStatus.ON_MODERATION);
            savedDocument.addVersion(version);
        }

        documentRepository.save(savedDocument);

        return DocumentMapper.modelToDto(savedDocument);
    }

    @Override
    @Transactional
    public OutputDocumentVersionDto save(InputDocumentVersionDto version, Long documentId) {
        DocumentVersion documentVersion = DocumentMapper.versionDtoToModel(version, documentId);
        Document document = findDocumentById(documentId);
        document.setCurrentVersion(documentVersion);

        if (documentVersion.getId() == null) {
            documentVersion.setStatus(ModerationStatus.ON_MODERATION);
            document.addVersion(documentVersion);
        }

        documentRepository.save(document);

        return DocumentMapper.modelToVersionDto(documentVersion);
    }

    @Override
    @Transactional
    public OutputDocumentDto getDocumentById(Long id) {
        return DocumentMapper.modelToDto(findDocumentById(id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<DocumentVersion> getAllVersionByDocumentId(Long id) {
        return findDocumentById(id).getVersions();
    }

    @Override
    @Transactional
    public DocumentType getDocumentTypeByName(String name) {
        return documentTypeRepository.findByType(name)
                .orElseThrow(() -> new DocumentTypeNotFoundException(name));
    }

    @Override
    @Transactional
    public List<DocumentType> getAllDocumentTypes() {
        return (List<DocumentType>) documentTypeRepository.findAll();
    }

    private Document findDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }
}
