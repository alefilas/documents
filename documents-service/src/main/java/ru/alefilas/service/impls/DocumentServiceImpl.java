package ru.alefilas.service.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.moderation.ModerationTicket;
import ru.alefilas.model.permit.PermitType;
import ru.alefilas.model.user.User;
import ru.alefilas.notification.NotificationService;
import ru.alefilas.notification.model.MessageType;
import ru.alefilas.repository.DocumentRepository;
import ru.alefilas.repository.DocumentTypeRepository;
import ru.alefilas.repository.ModerationRepository;
import ru.alefilas.repository.UserRepository;
import ru.alefilas.service.DocumentService;
import ru.alefilas.service.access.AccessHelper;
import ru.alefilas.service.exception.AccessDeniedException;
import ru.alefilas.service.exception.DocumentNotFoundException;
import ru.alefilas.service.exception.DocumentTypeNotFoundException;
import ru.alefilas.service.mapper.DocumentMapper;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private ModerationRepository moderationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public OutputDocumentDto save(InputDocumentDto dto) {

        Document document = DocumentMapper.dtoToModel(dto);

        AccessHelper.checkAccess(document.getParentDirectory(), PermitType.WRITE);

        if (document.getId() == null) {
            document.setCreationDate(LocalDate.now());

            User user = userRepository.findByUsername(AccessHelper.getCurrentUser()).orElseThrow();
            document.setUser(user);
        } else {
            notificationService.send(document.getUser(), document.getId(), MessageType.CHANGE);
        }

        DocumentVersion version = document.getCurrentVersion();
        if (version.getId() == null) {
            document.setStatus(ModerationStatus.ON_MODERATION);
            document.addVersion(version);
        }

        Document savedDocument = documentRepository.save(document);

        if  (document.getStatus() == ModerationStatus.ON_MODERATION) {
            createModerationTicket(savedDocument);
        }

        return DocumentMapper.modelToDto(savedDocument);
    }

    @Override
    @Transactional
    public OutputDocumentVersionDto save(InputDocumentVersionDto version, Long documentId) {
        DocumentVersion documentVersion = DocumentMapper.versionDtoToModel(version, documentId);
        Document document = findDocumentById(documentId);

        AccessHelper.checkAccess(document.getParentDirectory(), PermitType.WRITE);

        document.setCurrentVersion(documentVersion);

        if (documentVersion.getId() == null) {
            document.setStatus(ModerationStatus.ON_MODERATION);
            document.addVersion(documentVersion);
            notificationService.send(document.getUser(), documentId, MessageType.CHANGE);
        }

        DocumentVersion savedVersion = documentRepository.save(document).getCurrentVersion();

        return DocumentMapper.modelToVersionDto(savedVersion);
    }

    @Override
    @Transactional
    public OutputDocumentDto getDocumentById(Long id) {
        Document document = findDocumentById(id);
        AccessHelper.checkAccess(document.getParentDirectory(), PermitType.READ);
        return DocumentMapper.modelToDto(document);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Document document = findDocumentById(id);

        AccessHelper.checkAccess(document.getParentDirectory(), PermitType.WRITE);

        notificationService.send(document.getUser(), document.getId(), MessageType.DELETE);

        documentRepository.delete(document);
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

    @Override
    @Transactional
    public DocumentType save(String type) {
        DocumentType documentType = new DocumentType();
        documentType.setType(type.toUpperCase());
        return documentTypeRepository.save(documentType);
    }

    @Override
    @Transactional
    public List<OutputDocumentVersionDto> getDocumentVersionsById(Long id) {
        Document document = findDocumentById(id);
        AccessHelper.checkAccess(document.getParentDirectory(), PermitType.READ);
        return document.getVersions()
                .stream()
                .map(DocumentMapper::modelToVersionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<OutputDocumentDto> getAllDocuments(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("documentPriority"));

        List<Document> visibleDocuments = documentRepository.findAllByStatus(ModerationStatus.CONFIRMED)
                .stream()
                .filter(doc -> AccessHelper.checkAccessBoolean(doc.getParentDirectory(), PermitType.READ))
                .collect(Collectors.toList());

        Page<Document> documents = new PageImpl<>(visibleDocuments, pageable, visibleDocuments.size());

        return documents.map(DocumentMapper::modelToDto);
    }

    @Override
    @Transactional
    public Page<OutputDocumentDto> getDocumentsByType(String type, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        List<Document> visibleDocuments = documentRepository.findAllByTypeTypeAndStatus(type.toUpperCase(), ModerationStatus.CONFIRMED)
                .stream()
                .filter(doc -> AccessHelper.checkAccessBoolean(doc.getParentDirectory(), PermitType.READ))
                .collect(Collectors.toList());

        Page<Document> documents = new PageImpl<>(visibleDocuments, pageable, visibleDocuments.size());
        return documents.map(DocumentMapper::modelToDto);
    }

    private Document findDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    private void createModerationTicket(Document document) {
        if (moderationRepository.findFirstByDocument(document).isEmpty()) {
            ModerationTicket ticket = new ModerationTicket();
            ticket.setDocument(document);
            moderationRepository.save(ticket);
        }
    }
}
