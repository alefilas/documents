package ru.alefilas.service.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.moderation.ModerationTicket;
import ru.alefilas.model.user.User;
import ru.alefilas.notification.NotificationService;
import ru.alefilas.notification.impls.EmailSender;
import ru.alefilas.notification.model.MessageType;
import ru.alefilas.repository.DocumentRepository;
import ru.alefilas.repository.DocumentTypeRepository;
import ru.alefilas.repository.ModerationRepository;
import ru.alefilas.service.DocumentService;
import ru.alefilas.service.exception.DocumentNotFoundException;
import ru.alefilas.service.exception.DocumentTypeNotFoundException;
import ru.alefilas.service.mapper.DocumentMapper;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private ModerationRepository moderationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private NotificationService notificationService;


    @Override
    @Transactional
    public OutputDocumentDto save(InputDocumentDto dto) {

        Document document = DocumentMapper.dtoToModel(dto);

        if (document.getId() == null) {
            document.setCreationDate(LocalDate.now());
        } else {
            sendNotification(document.getUser(), document.getId(), MessageType.CHANGE);
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
        document.setCurrentVersion(documentVersion);

        if (documentVersion.getId() == null) {
            document.setStatus(ModerationStatus.ON_MODERATION);
            document.addVersion(documentVersion);
            sendNotification(document.getUser(), documentId, MessageType.CHANGE);
        }

        DocumentVersion savedVersion = documentRepository.save(document).getCurrentVersion();

        return DocumentMapper.modelToVersionDto(savedVersion);
    }

    @Override
    @Transactional
    public OutputDocumentDto getDocumentById(Long id) {
        return DocumentMapper.modelToDto(findDocumentById(id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Document document = findDocumentById(id);

        sendNotification(document.getUser(), document.getId(), MessageType.DELETE);

        documentRepository.delete(document);
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
        return findDocumentById(id).getVersions()
                .stream()
                .map(DocumentMapper::modelToVersionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<OutputDocumentDto> getAllDocuments(int page) {
        return documentRepository.findAllByStatus(ModerationStatus.CONFIRMED, PageRequest.of(page, 10, Sort.by("documentPriority")))
                .map(DocumentMapper::modelToDto);
    }

    @Override
    @Transactional
    public Page<OutputDocumentDto> getDocumentsByType(String type, int page) {
        return documentRepository.findAllByTypeTypeAndStatus(type.toUpperCase(), ModerationStatus.CONFIRMED, PageRequest.of(page, 5))
                .map(DocumentMapper::modelToDto);
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

    private void sendNotification(User user, Long id, MessageType type) {
        try {
            notificationService.send(user, id, type);
        } catch (MessagingException e) {
            log.error("Can't sent email", e);
        }
    }
}
