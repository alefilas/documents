package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.repository.UserRepository;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;

@Component
public class DocumentMapper {

    private static DocumentService documentService;

    private static DirectoryService directoryService;

    private static UserRepository userRepository;

    @Autowired
    public DocumentMapper(DocumentService documentService, DirectoryService directoryService, UserRepository userRepository) {
        DocumentMapper.documentService = documentService;
        DocumentMapper.directoryService = directoryService;
        DocumentMapper.userRepository = userRepository;
    }

    public static Document dtoToModel(DocumentDto dto) {

        Document document = new Document();

        document.setId(dto.getId());
        document.setCreationDate(dto.getCreationDate());
        document.setCurrentVersion(dto.getCurrentVersion());
        document.setDocumentPriority(DocumentPriority.valueOf(dto.getDocumentPriority()));
        document.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
        document.setType(documentService.getDocumentTypeByName("FAX"));
        document.setStatus(ModerationStatus.valueOf(dto.getStatus()));

        if(dto.getId() != null) {
            document.setVersions(documentService.getAllVersionByDocumentId(dto.getId()));
        }

        if (dto.getDirectoryId() != null) {
            document.setParentDirectory(DirectoryMapper.dtoToModel(directoryService.getDirectoryById(dto.getDirectoryId())));
        }

        return document;
    }

    public static DocumentDto modelToDto(Document document) {

        DocumentDto dto = new DocumentDto();

        dto.setId(document.getId());
        dto.setCreationDate(document.getCreationDate());
        dto.setCurrentVersion(document.getCurrentVersion());
        dto.setDocumentPriority(document.getDocumentPriority().toString());
        dto.setUserId(document.getUser().getId());
        dto.setType(document.getType().getType());
        dto.setStatus(document.getStatus().toString());

        if (document.getParentDirectory() != null) {
            dto.setDirectoryId(DirectoryMapper.dtoToModel(directoryService.getDirectoryById(dto.getDirectoryId())).getId());
        }

        return dto;
    }

}
