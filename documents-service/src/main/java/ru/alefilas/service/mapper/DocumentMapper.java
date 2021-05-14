package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.model.document.*;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.repository.DocumentRepository;
import ru.alefilas.repository.DocumentTypeRepository;
import ru.alefilas.repository.UserRepository;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.service.access.AccessHelper;
import ru.alefilas.service.exception.DirectoryNotFoundException;
import ru.alefilas.service.exception.DocumentNotFoundException;
import ru.alefilas.service.exception.DocumentTypeNotFoundException;
import ru.alefilas.service.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DocumentMapper {

    private static DirectoryRepository directoryRepository;

    private static DocumentRepository documentRepository;

    private static UserRepository userRepository;

    private static DocumentTypeRepository typeRepository;

    @Autowired
    public DocumentMapper(DocumentRepository documentRepository, DirectoryRepository directoryRepository, UserRepository userRepository, DocumentTypeRepository typeRepository) {
        DocumentMapper.documentRepository = documentRepository;
        DocumentMapper.directoryRepository = directoryRepository;
        DocumentMapper.userRepository = userRepository;
        DocumentMapper.typeRepository = typeRepository;
    }

    public static Document dtoToModel(InputDocumentDto dto) {

        Document document = new Document();

        Long id = dto.getId() == 0 ? null : dto.getId();

        Long directoryId = dto.getDirectoryId() == 0 ? null : dto.getDirectoryId();

        DocumentVersion version = versionDtoToModel(dto.getCurrentVersion(), id);

        DocumentPriority priority = dto.getDocumentPriority();

        DocumentType type = typeRepository.findByType(
                dto.getType().toUpperCase())
                .orElseThrow(
                        () -> new DocumentTypeNotFoundException(dto.getType().toUpperCase())
                );

        Directory parentDirectory = null;

        List<DocumentVersion> versions = new ArrayList<>();

        ModerationStatus status = ModerationStatus.ON_MODERATION;

        LocalDate creationDate = null;

        User user = null;

        if(id != null) {
            Document doc = documentRepository.findById(id)
                    .orElseThrow(() -> new DocumentNotFoundException(id));

            versions = doc.getVersions();
            status = doc.getStatus();
            creationDate = doc.getCreationDate();
            user = doc.getUser();
        }

        if (directoryId != null) {
            parentDirectory = directoryRepository.findById(directoryId)
                    .orElseThrow(() -> new DirectoryNotFoundException(directoryId));
        }

        document.setId(id);
        document.setCurrentVersion(version);
        document.setDocumentPriority(priority);
        document.setUser(user);
        document.setType(type);
        document.setVersions(versions);
        document.setParentDirectory(parentDirectory);
        document.setStatus(status);
        document.setCreationDate(creationDate);

        return document;
    }

    public static OutputDocumentDto modelToDto(Document document) {

        OutputDocumentDto dto = new OutputDocumentDto();

        dto.setId(document.getId());
        dto.setCreationDate(document.getCreationDate());
        dto.setDocumentPriority(document.getDocumentPriority());
        dto.setUsername(document.getUser().getUsername());
        dto.setType(document.getType().getType());
        dto.setStatus(document.getStatus());

        if (document.getParentDirectory() != null) {
            dto.setDirectoryId(directoryRepository.findById(document.getParentDirectory().getId())
                    .orElseThrow(() -> new DirectoryNotFoundException(dto.getDirectoryId()))
                    .getId());
        }

        if (document.getCurrentVersion() != null) {
            dto.setCurrentVersion(modelToVersionDto(document.getCurrentVersion()));
        }

        return dto;
    }

    public static DocumentVersion versionDtoToModel(InputDocumentVersionDto dto, Long documentId) {

        Optional<DocumentVersion> versionOptional = documentRepository.findDocumentVersion(
                dto.getTitle(),
                dto.getDescription(),
                documentId
        );

        if (versionOptional.isPresent() &&
                Objects.deepEquals(versionOptional.get().getFiles().toArray(), dto.getFiles().toArray())) {
            return versionOptional.get();
        }

        DocumentVersion version = new DocumentVersion();

        version.setFiles(dto.getFiles());
        version.setTitle(dto.getTitle());
        version.setDescription(dto.getDescription());

        return version;
    }

    public static OutputDocumentVersionDto modelToVersionDto(DocumentVersion version) {

        OutputDocumentVersionDto dto = new OutputDocumentVersionDto();

        dto.setId(version.getId());
        dto.setFiles(version.getFiles());
        dto.setDescription(version.getDescription());
        dto.setTitle(version.getTitle());

        return dto;
    }

}
